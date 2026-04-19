import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class HistoryViewer extends JFrame {
    private static final Color BG_MAIN = new Color(252, 249, 244);
    private static final Color BG_CARD = new Color(246, 243, 238);
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color TEXT_GREEN = new Color(40, 120, 40);
    private static final Color TEXT_AMBER = new Color(200, 120, 40);
    private static final Color BG_GREEN = new Color(230, 245, 230);
    private static final Color BG_AMBER = new Color(255, 245, 230);
    private static final Color BTN_NEUTRAL = new Color(200, 200, 200);

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public HistoryViewer() {
        setTitle("Carbon Path - History");
        setSize(460, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        setupNavBar();

        List<Trip> trips = new ArrayList<>();
        try {
            trips = DatabaseManager.getAllTrips();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Failed to load history from database!\n\nReason: " + e.getMessage() + "\n\nEnsure MySQL is running and your password in DatabaseManager is correct.",
                "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        double totalCarbon = 0;
        double totalPossibleSavings = 0;
        double totalEcoSavingsMade = 0;

        for (Trip t : trips) {
            totalCarbon += t.getCarbon();
            totalPossibleSavings += t.getPotentialSaving();

            if (t.getTransport().equals(t.getSuggestedTransport()) && t.getPotentialSaving() > 0) {
                totalEcoSavingsMade += t.getPotentialSaving();
            }
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Top Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(BG_CARD);
        summaryPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel("Summary");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(TEXT_DARK);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel summaryCardsPanel = new JPanel();
        summaryCardsPanel.setLayout(new GridLayout(1, 3, 10, 0));
        summaryCardsPanel.setBackground(BG_CARD);
        summaryCardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel emittedCard = createMiniSummaryCard("Emitted", df.format(totalCarbon) + " kg", new Color(235, 230, 220), TEXT_DARK);
        JPanel savedCard = createMiniSummaryCard("Saved", df.format(totalEcoSavingsMade) + " kg", BG_GREEN, TEXT_GREEN);
        JPanel missedCard = createMiniSummaryCard("Missed", df.format(totalPossibleSavings - totalEcoSavingsMade) + " kg", BG_AMBER, TEXT_AMBER);

        summaryCardsPanel.add(emittedCard);
        summaryCardsPanel.add(savedCard);
        summaryCardsPanel.add(missedCard);

        summaryPanel.add(titleLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        summaryPanel.add(summaryCardsPanel);

        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 28)));

        // Trip List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_MAIN);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (trips.isEmpty()) {
            JLabel emptyLbl = new JLabel("No history found. Try calculating and saving a trip!");
            emptyLbl.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLbl.setForeground(Color.GRAY);
            emptyLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(emptyLbl);
        } else {
            for (Trip trip : trips) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(BG_CARD);
                card.setBorder(new EmptyBorder(16, 16, 16, 16));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

                JLabel dateLbl = new JLabel(sdf.format(trip.getDate()));
                dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
                dateLbl.setForeground(Color.GRAY);
                dateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel infoLbl = new JLabel(trip.getDistance() + " km via " + trip.getTransport());
                infoLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
                infoLbl.setForeground(TEXT_DARK);
                infoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel emissionLbl = new JLabel("Emitted: " + df.format(trip.getCarbon()) + " kg");
                emissionLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
                emissionLbl.setForeground(TEXT_DARK);
                emissionLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                card.add(dateLbl);
                card.add(Box.createRigidArea(new Dimension(0, 4)));
                card.add(infoLbl);
                card.add(Box.createRigidArea(new Dimension(0, 4)));
                card.add(emissionLbl);
                card.add(Box.createRigidArea(new Dimension(0, 16)));

                // Savings breakdown in card
                JPanel savingsMiniPanel = new JPanel();
                savingsMiniPanel.setLayout(new GridLayout(1, 2, 10, 0));
                savingsMiniPanel.setBackground(BG_CARD);
                savingsMiniPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                boolean madeSavings = trip.getTransport().equals(trip.getSuggestedTransport()) && trip.getPotentialSaving() > 0;
                String ecoVal = madeSavings ? df.format(trip.getPotentialSaving()) : "0.00";
                String missedVal = (!madeSavings && trip.getPotentialSaving() > 0) ? df.format(trip.getPotentialSaving()) : "0.00";

                JPanel leftCard = createMiniCard("Saved", ecoVal + " kg", BG_GREEN, TEXT_GREEN);
                JPanel rightCard = createMiniCard("Missed", missedVal + " kg", BG_AMBER, TEXT_AMBER);

                savingsMiniPanel.add(leftCard);
                savingsMiniPanel.add(rightCard);

                card.add(savingsMiniPanel);

                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(listPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMiniCard(String title, String value, Color bg, Color textCol) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        tLbl.setForeground(textCol);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        vLbl.setForeground(textCol);

        panel.add(tLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));
        panel.add(vLbl);
        return panel;
    }

    private JPanel createMiniSummaryCard(String title, String value, Color bg, Color textCol) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tLbl.setForeground(textCol);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        vLbl.setForeground(textCol);

        panel.add(tLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(vLbl);
        return panel;
    }

    private void setupNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(BG_MAIN);
        navPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titleLabel = new JLabel("Carbon Path");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_DARK);

        JButton homeBtn = new JButton("Home");
        homeBtn.setBackground(BTN_NEUTRAL);
        homeBtn.setForeground(TEXT_DARK);
        homeBtn.setFocusPainted(false);
        homeBtn.setBorderPainted(false);
        homeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeBtn.addActionListener(e -> {
            new UIFrame().setVisible(true);
            this.dispose();
        });

        navPanel.add(titleLabel, BorderLayout.WEST);
        navPanel.add(homeBtn, BorderLayout.EAST);
        add(navPanel, BorderLayout.NORTH);
    }
}
