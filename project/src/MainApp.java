import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Initialize Database
        DatabaseManager.initialize();

        // Launch UI
        SwingUtilities.invokeLater(() -> {
            UIFrame frame = new UIFrame();
            frame.setVisible(true);
        });
    }
}
