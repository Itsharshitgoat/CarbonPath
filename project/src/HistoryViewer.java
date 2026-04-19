import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class HistoryViewer extends JFrame {
    private static final Color BG_MAIN = new Color(252, 249, 244);
    private static final Color BG_CARD = new Color(246, 243, 238);
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color TEXT_GREEN = new Color(40, 120, 40);
    private static final Color TEXT_AMBER = new Color(200, 120, 40);

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public HistoryViewer() {
        setTitle("Carbon Path - History");
        setSize(420, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        setupNavBar();

        List<Trip> trips = DatabaseManager.getAllTrips();

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
        mainPanel.setBorder(new EmptyBorder(24, 40, 24, 16));

        // Top Summary Panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(BG_CARD);
        summaryPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel("Summary");
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
        savingsLbl.setForeground(TEXT_GREEN);

        summaryPanel.add(titleLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(carbonLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(possibleSavLbl);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(savingsLbl);

        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 28)));

        // Trip List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_MAIN);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Trip trip : trips) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(BG_CARD);
            card.setBorder(new EmptyBorder(16, 16, 16, 16));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            JLabel dateLbl = new JLabel(sdf.format(trip.getDate()));
            dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            dateLbl.setForeground(Color.GRAY);

            JLabel infoLbl = new JLabel(trip.getDistance() + " km via " + trip.getTransport());
            infoLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            infoLbl.setForeground(TEXT_DARK);

            JLabel emissionLbl = new JLabel("Emitted: " + df.format(trip.getCarbon()) + " kg");
            emissionLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emissionLbl.setForeground(TEXT_DARK);

            card.add(dateLbl);
            card.add(Box.createRigidArea(new Dimension(0, 4)));
            card.add(infoLbl);
            card.add(Box.createRigidArea(new Dimension(0, 4)));
            card.add(emissionLbl);
            card.add(Box.createRigidArea(new Dimension(0, 10)));

            // Savings breakdown in card
            JPanel savingsMiniPanel = new JPanel();
            savingsMiniPanel.setLayout(new GridLayout(1, 2, 10, 0));
            savingsMiniPanel.setBackground(BG_CARD);
            savingsMiniPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel leftCard = new JPanel();
            leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
            leftCard.setBackground(new Color(230, 245, 230)); // Soft Green tone
            leftCard.setBorder(new EmptyBorder(6, 6, 6, 6));
            JLabel ecoTitle = new JLabel("Saved");
            ecoTitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
            ecoTitle.setForeground(TEXT_GREEN);

            boolean madeSavings = trip.getTransport().equals(trip.getSuggestedTransport()) && trip.getPotentialSaving() > 0;
            String ecoVal = madeSavings ? df.format(trip.getPotentialSaving()) : "0.00";
            JLabel ecoSavingsLabel = new JLabel(ecoVal + " kg");
            ecoSavingsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            ecoSavingsLabel.setForeground(TEXT_GREEN);
            leftCard.add(ecoTitle);
            leftCard.add(ecoSavingsLabel);

            JPanel rightCard = new JPanel();
            rightCard.setLayout(new BoxLayout(rightCard, BoxLayout.Y_AXIS));
            rightCard.setBackground(new Color(255, 245, 230)); // Soft Amber tone
            rightCard.setBorder(new EmptyBorder(6, 6, 6, 6));
            JLabel missedTitle = new JLabel("Missed");
            missedTitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
            missedTitle.setForeground(TEXT_AMBER);

            String missedVal = (!madeSavings && trip.getPotentialSaving() > 0) ? df.format(trip.getPotentialSaving()) : "0.00";
            JLabel missedPotentialLabel = new JLabel(missedVal + " kg");
            missedPotentialLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            missedPotentialLabel.setForeground(TEXT_AMBER);
            rightCard.add(missedTitle);
            rightCard.add(missedPotentialLabel);

            savingsMiniPanel.add(leftCard);
            savingsMiniPanel.add(rightCard);

            card.add(savingsMiniPanel);

            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(listPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupNavBar() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(BG_CARD);
        navPanel.setBorder(new EmptyBorder(10, 40, 10, 16));

        String[] navOptions = {"Carbon Path \u25BC", "Home", "View History"};
        JComboBox<String> navMenu = new JComboBox<>(navOptions);
        navMenu.setBackground(BG_CARD);
        navMenu.setForeground(TEXT_DARK);
        navMenu.setBorder(null);
        navMenu.setFocusable(false);
        navMenu.setFont(new Font("SansSerif", Font.BOLD, 16));
        navMenu.setSelectedIndex(2); // Currently on history

        navMenu.addActionListener(e -> {
            String selection = (String) navMenu.getSelectedItem();
            if ("Home".equals(selection) || selection.startsWith("Carbon")) {
                new UIFrame().setVisible(true);
                this.dispose();
            }
        });

        navPanel.add(navMenu);
        add(navPanel, BorderLayout.NORTH);
    }
}
