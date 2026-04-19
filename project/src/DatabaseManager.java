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
    private static final String URL = "jdbc:sqlite:carbon_tracker.db";

    public static void initialize() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS trips ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "distance REAL, "
                + "transport TEXT, "
                + "carbon REAL, "
                + "suggested_transport TEXT, "
                + "potential_saving REAL, "
                + "date DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
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
            e.printStackTrace();
        }
        return trips;
    }
}
