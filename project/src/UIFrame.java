import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;

public class UIFrame extends JFrame {
    private static final Color BG_MAIN = new Color(252, 249, 244); // #fcf9f4
    private static final Color BG_CARD = new Color(246, 243, 238); // #f6f3ee
    private static final Color BG_TOP = new Color(229, 226, 221); // #e5e2dd
    private static final Color BG_INPUT = new Color(240, 237, 232); // #f0ede8
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color BTN_PRIMARY = new Color(40, 150, 140); // Teal
    private static final Color BTN_SAVE_ECO = new Color(120, 180, 120); // Soft green
    private static final Color BTN_SAVE_NORMAL = new Color(200, 200, 200);

    private JTextField distanceField;
    private JComboBox<String> transportCombo;
    private JLabel resultLabel;
    private JLabel suggestionLabel;
    private JLabel savingsLabel;
    private JButton saveEcoBtn;
    private JButton saveNormalBtn;

    private double currentDistance = 0;
    private String currentTransport = "";
    private double currentCarbon = 0;
    private String currentSuggestedTransport = "";
    private double currentSavings = 0;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public UIFrame() {
        setTitle("Carbon Footprint Tracker");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        // Top Panel (Branding)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BG_TOP);
        topPanel.setBorder(new EmptyBorder(20, 40, 20, 16));
        JLabel titleLabel = new JLabel("🌿 Carbon Tracker & Advisor");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_DARK);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(24, 40, 24, 16));

        // Input Section
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(BG_CARD);
        inputPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel distanceLbl = new JLabel("Distance (km):");
        distanceLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        distanceLbl.setForeground(TEXT_DARK);
        distanceField = new JTextField(10);
        distanceField.setBackground(BG_INPUT);
        distanceField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        distanceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel transportLbl = new JLabel("Transport:");
        transportLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        transportLbl.setForeground(TEXT_DARK);
        String[] transports = {"Walking", "Bicycle", "Bike", "Car", "Bus"};
        transportCombo = new JComboBox<>(transports);
        transportCombo.setBackground(BG_INPUT);
        transportCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton calcBtn = new JButton("Calculate");
        calcBtn.setBackground(BTN_PRIMARY);
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);
        calcBtn.setBorderPainted(false);
        calcBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        calcBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        calcBtn.addActionListener(e -> calculateCarbon());

        inputPanel.add(distanceLbl);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        inputPanel.add(distanceField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        inputPanel.add(transportLbl);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        inputPanel.add(transportCombo);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        inputPanel.add(calcBtn);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // Result Section
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(BG_CARD);
        resultPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultLabel = new JLabel("Carbon Output: --");
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        resultLabel.setForeground(TEXT_DARK);

        suggestionLabel = new JLabel("Suggestion: --");
        suggestionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        suggestionLabel.setForeground(TEXT_DARK);

        savingsLabel = new JLabel("Savings Potential: --");
        savingsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        savingsLabel.setForeground(new Color(40, 120, 40));

        saveEcoBtn = new JButton("✅ Save as Eco Choice");
        saveEcoBtn.setBackground(BTN_SAVE_ECO);
        saveEcoBtn.setForeground(Color.WHITE);
        saveEcoBtn.setFocusPainted(false);
        saveEcoBtn.setBorderPainted(false);
        saveEcoBtn.setVisible(false);
        saveEcoBtn.addActionListener(e -> saveTrip(true));

        saveNormalBtn = new JButton("⚪ Proceed Anyway");
        saveNormalBtn.setBackground(BTN_SAVE_NORMAL);
        saveNormalBtn.setForeground(TEXT_DARK);
        saveNormalBtn.setFocusPainted(false);
        saveNormalBtn.setBorderPainted(false);
        saveNormalBtn.setVisible(false);
        saveNormalBtn.addActionListener(e -> saveTrip(false));

        resultPanel.add(resultLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        resultPanel.add(suggestionLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        resultPanel.add(savingsLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        resultPanel.add(saveEcoBtn);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        resultPanel.add(saveNormalBtn);

        mainPanel.add(resultPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // History Button
        JButton historyBtn = new JButton("View History");
        historyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyBtn.addActionListener(e -> {
            new HistoryViewer().setVisible(true);
        });
        mainPanel.add(historyBtn);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void calculateCarbon() {
        try {
            currentDistance = Double.parseDouble(distanceField.getText());
            currentTransport = (String) transportCombo.getSelectedItem();

            currentCarbon = CarbonCalculator.calculateEmission(currentDistance, currentTransport);

            String suggestion = SuggestionEngine.getSuggestion(currentDistance);
            currentSuggestedTransport = SuggestionEngine.getSuggestedTransport(currentDistance);

            double optimalEmission = SuggestionEngine.calculateOptimalEmission(currentDistance);
            currentSavings = SuggestionEngine.calculateSavings(currentCarbon, optimalEmission);

            resultLabel.setText("Carbon Output: " + df.format(currentCarbon) + " kg CO₂");
            suggestionLabel.setText("Suggestion: " + suggestion);

            if (currentSavings > 0) {
                savingsLabel.setText("Savings Potential: " + df.format(currentSavings) + " kg CO₂");
                saveEcoBtn.setText("✅ Save as Eco Choice (" + currentSuggestedTransport + ")");
                saveEcoBtn.setVisible(true);
                saveNormalBtn.setVisible(true);
            } else {
                savingsLabel.setText("You are making an optimal choice!");
                saveEcoBtn.setText("✅ Save Eco Trip");
                saveEcoBtn.setVisible(true);
                saveNormalBtn.setVisible(false);
            }

            revalidate();
            repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid distance.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTrip(boolean ecoChoice) {
        Trip trip;
        if (ecoChoice && currentSavings > 0) {
            // User chose the eco alternative
            // We'll store potentialSaving as a negative value or separate field, but for DB schema given,
            // Let's store potential_saving as what they SAVED if they chose eco.
            // If they DID NOT choose eco, let's store potential_saving as what they COULD HAVE SAVED, but somehow distinguish it.
            // Wait, we can't change the schema easily right now without dropping. Let's just follow history requirement:
            // "Total carbon emitted, Total possible savings, Total eco savings made".
            // Since the DB has distance, transport, carbon, suggested_transport, potential_saving.
            // Wait, potential_saving should be the SAVINGS. If they chose eco, carbon is lower.
            // Let's re-calculate in HistoryViewer based on distance, transport, carbon!
            double ecoCarbon = CarbonCalculator.calculateEmission(currentDistance, currentSuggestedTransport);
            trip = new Trip(currentDistance, currentSuggestedTransport, ecoCarbon, currentSuggestedTransport, currentSavings);
        } else {
            // User proceeds anyway or already optimal
            trip = new Trip(currentDistance, currentTransport, currentCarbon, currentSuggestedTransport, currentSavings); // always store potential savings!
        }

        DatabaseManager.saveTrip(trip);
        JOptionPane.showMessageDialog(this, "Trip saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Reset UI
        distanceField.setText("");
        resultLabel.setText("Carbon Output: --");
        suggestionLabel.setText("Suggestion: --");
        savingsLabel.setText("Savings Potential: --");
        saveEcoBtn.setVisible(false);
        saveNormalBtn.setVisible(false);
    }
}
