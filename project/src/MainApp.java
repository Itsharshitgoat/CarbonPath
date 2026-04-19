import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        DatabaseManager.initialize();

        SwingUtilities.invokeLater(() -> {
            UIFrame frame = new UIFrame();
            frame.setVisible(true);
        });
    }
}
