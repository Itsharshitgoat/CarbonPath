import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;

public class UIFrame extends JFrame {
    private static final Color BG_MAIN = new Color(252, 249, 244);
    private static final Color BG_CARD = new Color(246, 243, 238);
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color BTN_PRIMARY = new Color(40, 150, 140);
    private static final Color BTN_ECO = new Color(120, 180, 120);
    private static final Color BTN_NEUTRAL = new Color(200, 200, 200);
    private static final Color TEXT_AMBER = new Color(200, 120, 40);
    private static final Color TEXT_GREEN = new Color(40, 120, 40);

    private JTextField distanceField;
    private JComboBox<String> transportCombo;

    private JPanel resultPanel;
    private JLabel carbonLabel;
    private JLabel suggestionLabel;

    private JPanel savingsCardPanel;
    private JLabel ecoSavingsLabel;
    private JLabel missedPotentialLabel;

    private JButton saveEcoBtn;
    private JButton saveNormalBtn;
    private JPanel buttonsPanel;

    private double currentDistance = 0;
    private String currentTransport = "";
    private double currentCarbon = 0;
    private String currentSuggestedTransport = "";
    private double currentSavings = 0;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private JPanel mainContainer;

    public UIFrame() {
        setTitle("Carbon Path");
        setSize(420, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        // Top Navigation Bar
        setupNavBar();

        mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(BG_MAIN);
        mainContainer.setBorder(new EmptyBorder(24, 40, 24, 16));

        setupInputSection();
        setupResultSection();

        add(mainContainer, BorderLayout.CENTER);
    }

    private void setupNavBar() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(BG_CARD); // Rounded visual in Swing without custom painting is limited, using contrast color
        navPanel.setBorder(new EmptyBorder(10, 40, 10, 16));

        String[] navOptions = {"Carbon Path \u25BC", "Home", "View History"};
        JComboBox<String> navMenu = new JComboBox<>(navOptions);
        navMenu.setBackground(BG_CARD);
        navMenu.setForeground(TEXT_DARK);
        navMenu.setBorder(null);
        navMenu.setFocusable(false);
        navMenu.setFont(new Font("SansSerif", Font.BOLD, 16));

        navMenu.addActionListener(e -> {
            String selection = (String) navMenu.getSelectedItem();
            if ("View History".equals(selection)) {
                new HistoryViewer().setVisible(true);
                this.dispose();
            }
            navMenu.setSelectedIndex(0);
        });

        navPanel.add(navMenu);
        add(navPanel, BorderLayout.NORTH);
    }

    private void setupInputSection() {
        JPanel inputCard = new JPanel();
        inputCard.setLayout(new BoxLayout(inputCard, BoxLayout.Y_AXIS));
        inputCard.setBackground(BG_CARD);
        inputCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        inputCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel distanceLbl = new JLabel("Distance (km)");
        distanceLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        distanceLbl.setForeground(TEXT_DARK);
        distanceLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        distanceField = new JTextField(10);
        distanceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        distanceField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel transportLbl = new JLabel("Transport");
        transportLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        transportLbl.setForeground(TEXT_DARK);
        transportLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] transports = {"Walking", "Bicycle", "Bike", "Car", "Bus"};
        transportCombo = new JComboBox<>(transports);
        transportCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        transportCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton calcBtn = new JButton("Calculate");
        calcBtn.setBackground(BTN_PRIMARY);
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFocusPainted(false);
        calcBtn.setBorderPainted(false);
        calcBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        calcBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        calcBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        calcBtn.addActionListener(e -> calculateCarbon());

        inputCard.add(distanceLbl);
        inputCard.add(Box.createRigidArea(new Dimension(0, 8)));
        inputCard.add(distanceField);
        inputCard.add(Box.createRigidArea(new Dimension(0, 16)));
        inputCard.add(transportLbl);
        inputCard.add(Box.createRigidArea(new Dimension(0, 8)));
        inputCard.add(transportCombo);
        inputCard.add(Box.createRigidArea(new Dimension(0, 24)));
        inputCard.add(calcBtn);

        mainContainer.add(inputCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 28)));
    }

    private void setupResultSection() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(BG_CARD);
        resultPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultPanel.setVisible(false);

        carbonLabel = new JLabel("Carbon Output: --");
        carbonLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        carbonLabel.setForeground(TEXT_DARK);

        suggestionLabel = new JLabel("Suggested Transport: --");
        suggestionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        suggestionLabel.setForeground(TEXT_DARK);

        // Savings Card Panel - Left (Green) and Right (Amber)
        savingsCardPanel = new JPanel();
        savingsCardPanel.setLayout(new GridLayout(1, 2, 10, 0));
        savingsCardPanel.setBackground(BG_CARD);
        savingsCardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftCard = new JPanel();
        leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
        leftCard.setBackground(new Color(230, 245, 230)); // Soft Green tone
        leftCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel ecoTitle = new JLabel("Eco Savings Achieved");
        ecoTitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        ecoTitle.setForeground(TEXT_GREEN);
        ecoSavingsLabel = new JLabel("0.00 kg");
        ecoSavingsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        ecoSavingsLabel.setForeground(TEXT_GREEN);
        leftCard.add(ecoTitle);
        leftCard.add(Box.createRigidArea(new Dimension(0, 4)));
        leftCard.add(ecoSavingsLabel);

        JPanel rightCard = new JPanel();
        rightCard.setLayout(new BoxLayout(rightCard, BoxLayout.Y_AXIS));
        rightCard.setBackground(new Color(255, 245, 230)); // Soft Amber tone
        rightCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel missedTitle = new JLabel("Missed Potential");
        missedTitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        missedTitle.setForeground(TEXT_AMBER);
        missedPotentialLabel = new JLabel("0.00 kg");
        missedPotentialLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        missedPotentialLabel.setForeground(TEXT_AMBER);
        rightCard.add(missedTitle);
        rightCard.add(Box.createRigidArea(new Dimension(0, 4)));
        rightCard.add(missedPotentialLabel);

        savingsCardPanel.add(leftCard);
        savingsCardPanel.add(rightCard);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setBackground(BG_CARD);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        saveEcoBtn = new JButton("Save Eco Choice");
        saveEcoBtn.setBackground(BTN_ECO);
        saveEcoBtn.setForeground(Color.WHITE);
        saveEcoBtn.setFocusPainted(false);
        saveEcoBtn.setBorderPainted(false);
        saveEcoBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveEcoBtn.addActionListener(e -> saveTrip(true));

        saveNormalBtn = new JButton("Save Current Choice");
        saveNormalBtn.setBackground(BTN_NEUTRAL);
        saveNormalBtn.setForeground(TEXT_DARK);
        saveNormalBtn.setFocusPainted(false);
        saveNormalBtn.setBorderPainted(false);
        saveNormalBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveNormalBtn.addActionListener(e -> saveTrip(false));

        buttonsPanel.add(saveEcoBtn);
        buttonsPanel.add(saveNormalBtn);

        resultPanel.add(carbonLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        resultPanel.add(suggestionLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        resultPanel.add(savingsCardPanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(buttonsPanel);

        mainContainer.add(resultPanel);
    }

    private void calculateCarbon() {
        try {
            currentDistance = Double.parseDouble(distanceField.getText());
            currentTransport = (String) transportCombo.getSelectedItem();

            currentCarbon = CarbonCalculator.calculateEmission(currentDistance, currentTransport);
            currentSuggestedTransport = SuggestionEngine.getSuggestedTransport(currentDistance);
            double optimalEmission = SuggestionEngine.calculateOptimalEmission(currentDistance);

            currentSavings = currentCarbon - optimalEmission;
            if (currentSavings < 0) currentSavings = 0; // Already optimal or better

            carbonLabel.setText("Carbon Output: " + df.format(currentCarbon) + " kg CO₂");
            suggestionLabel.setText("Suggested Transport: " + currentSuggestedTransport);

            ecoSavingsLabel.setText("0.00 kg"); // Pre-choice state
            missedPotentialLabel.setText(df.format(currentSavings) + " kg"); // Shows what they are missing currently

            if (currentSavings > 0) {
                saveEcoBtn.setVisible(true);
            } else {
                saveEcoBtn.setVisible(false); // No eco alternative better than current
                missedPotentialLabel.setText("0.00 kg");
            }

            resultPanel.setVisible(true);
            revalidate();
            repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric distance.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTrip(boolean isEcoChoice) {
        Trip trip;
        if (isEcoChoice && currentSavings > 0) {
            double ecoCarbon = CarbonCalculator.calculateEmission(currentDistance, currentSuggestedTransport);
            trip = new Trip(currentDistance, currentSuggestedTransport, ecoCarbon, currentSuggestedTransport, currentSavings);
        } else {
            trip = new Trip(currentDistance, currentTransport, currentCarbon, currentSuggestedTransport, currentSavings);
        }

        DatabaseManager.saveTrip(trip);
        JOptionPane.showMessageDialog(this, "Trip saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);

        resultPanel.setVisible(false);
        distanceField.setText("");
        revalidate();
        repaint();
    }
}
