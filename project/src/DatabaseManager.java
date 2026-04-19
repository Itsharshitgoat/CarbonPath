import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class DatabaseManager {
    // MySQL connection settings
    // Update the USER and PASSWORD variables according to your local MySQL server setup
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "carbon_path";
    private static final String URL = BASE_URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = ""; // <-- Enter your MySQL password here

    public static void initialize() {
        try {
            // Ensure the JDBC driver is loaded (mostly optional in modern JDBC, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure mysql-connector-j.jar is in the lib/ folder.");
            e.printStackTrace();
            return;
        }

        // First, connect to base MySQL server to ensure the database exists
        try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database '" + DB_NAME + "' checked/created successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL server. Check your username and password in DatabaseManager.java.");
            e.printStackTrace();
            return;
        }

        // Now connect specifically to the carbon_path database to create the table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS trips ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "distance DOUBLE, "
                + "transport VARCHAR(50), "
                + "carbon DOUBLE, "
                + "suggested_transport VARCHAR(50), "
                + "potential_saving DOUBLE, "
                + "date DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'trips' checked/created successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to create table.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveTrip(Trip trip) {
        String insertSQL = "INSERT INTO trips(distance, transport, carbon, suggested_transport, potential_saving, date) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setDouble(1, trip.getDistance());
            pstmt.setString(2, trip.getTransport());
            pstmt.setDouble(3, trip.getCarbon());
            pstmt.setString(4, trip.getSuggestedTransport());
            pstmt.setDouble(5, trip.getPotentialSaving());
            pstmt.setTimestamp(6, trip.getDate() != null ? new Timestamp(trip.getDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save trip to database.");
            e.printStackTrace();
        }
    }

    public static List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String query = "SELECT * FROM trips ORDER BY date DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Trip trip = new Trip(
                        rs.getDouble("distance"),
                        rs.getString("transport"),
                        rs.getDouble("carbon"),
                        rs.getString("suggested_transport"),
                        rs.getDouble("potential_saving"),
                        rs.getTimestamp("date")
                );
                trip.setId(rs.getInt("id"));
                trips.add(trip);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch history. Ensure the database server is running.");
            e.printStackTrace();
        }
        return trips;
    }
}
