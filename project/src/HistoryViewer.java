import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class HistoryViewer extends JFrame {
    private static final Color BG_MAIN = new Color(252, 249, 244);
    private static final Color BG_CARD = new Color(246, 243, 238);
    private static final Color BG_TOP = new Color(229, 226, 221);
    private static final Color TEXT_DARK = new Color(50, 50, 50);

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public HistoryViewer() {
        setTitle("Trip History");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        List<Trip> trips = DatabaseManager.getAllTrips();

        double totalCarbon = 0;
        double totalPossibleSavings = 0;
        double totalEcoSavingsMade = 0;

        for (Trip t : trips) {
            totalCarbon += t.getCarbon();
            totalPossibleSavings += t.getPotentialSaving();

            // If they chose the eco transport, they made savings!
            // E.g. t.getTransport() equals t.getSuggestedTransport() AND t.getPotentialSaving() > 0
            if (t.getTransport().equals(t.getSuggestedTransport()) && t.getPotentialSaving() > 0) {
                totalEcoSavingsMade += t.getPotentialSaving();
            }
        }

        // Top Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(BG_TOP);
        summaryPanel.setBorder(new EmptyBorder(20, 40, 20, 16));

        JLabel titleLbl = new JLabel("📊 Your Environmental Impact");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(TEXT_DARK);

        JLabel carbonLbl = new JLabel("Total Carbon Emitted: " + df.format(totalCarbon) + " kg");
        carbonLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        carbonLbl.setForeground(TEXT_DARK);

        JLabel possibleSavLbl = new JLabel("Total Possible Savings: " + df.format(totalPossibleSavings) + " kg");
        possibleSavLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        possibleSavLbl.setForeground(TEXT_DARK);

        JLabel savingsLbl = new JLabel("Total Eco Savings Made: " + df.format(totalEcoSavingsMade) + " kg");
        savingsLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        savingsLbl.setForeground(new Color(40, 120, 40));

        summaryPanel.add(titleLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(carbonLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(possibleSavLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(savingsLbl);

        add(summaryPanel, BorderLayout.NORTH);

        // Trip List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_MAIN);
        listPanel.setBorder(new EmptyBorder(16, 40, 16, 16));

        for (Trip trip : trips) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(BG_CARD);
            card.setBorder(new EmptyBorder(10, 10, 10, 10));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

            JLabel dateLbl = new JLabel(sdf.format(trip.getDate()));
            dateLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
            dateLbl.setForeground(Color.GRAY);

            JLabel infoLbl = new JLabel(trip.getDistance() + " km via " + trip.getTransport());
            infoLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            infoLbl.setForeground(TEXT_DARK);

            JLabel emissionLbl = new JLabel("Emitted: " + df.format(trip.getCarbon()) + " kg CO₂");
            emissionLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emissionLbl.setForeground(TEXT_DARK);

            card.add(dateLbl);
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(infoLbl);
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(emissionLbl);

            boolean madeSavings = trip.getTransport().equals(trip.getSuggestedTransport()) && trip.getPotentialSaving() > 0;
            if (trip.getPotentialSaving() > 0) {
                String savText = madeSavings ? "Eco Choice! Saved: " : "Missed Potential: ";
                JLabel savLbl = new JLabel(savText + df.format(trip.getPotentialSaving()) + " kg CO₂");
                savLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                savLbl.setForeground(madeSavings ? new Color(40, 120, 40) : Color.ORANGE);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(savLbl);
            }

            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
}
