import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        try {
            DatabaseManager.initialize();
        } catch (SQLException e) {
            System.err.println("Database Initialization Failed: " + e.getMessage());
            // We don't crash, we just let the UI start and show errors when trying to read/write
        }

        SwingUtilities.invokeLater(() -> {
            UIFrame frame = new UIFrame();
            frame.setVisible(true);
        });
    }
}
