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
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "carbon_path";

    // We add serverTimezone=UTC to prevent common MySQL connection crash on some machines
    private static final String URL = BASE_URL + DB_NAME + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // <-- Enter your MySQL password here

    public static void initialize() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in lib/ folder.");
        }

        try (Connection conn = DriverManager.getConnection(BASE_URL + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

        }

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
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveTrip(Trip trip) throws SQLException {
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
        }
    }

    public static List<Trip> getAllTrips() throws SQLException {
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
        }
        return trips;
    }
}
