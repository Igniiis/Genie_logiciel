import java.sql.*;
import java.util.ArrayList;

public class ReservationDAO {

    public ArrayList<Reservation> getReservations(int id_client) throws SQLException {

        // Query to get all vehicles for the client
        String sql = "SELECT * FROM client_vehicule WHERE id_client = ?";

        // Query to get all upcoming reservations for these vehicles
        String sql2 = "SELECT * FROM reservation WHERE plaque_vehicule = ? AND date_fin_reservation > now()";

        ArrayList<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt1.setInt(1, id_client);
            ResultSet rsVehicule = pstmt1.executeQuery();

            while (rsVehicule.next()) {
                pstmt2.setString(1, rsVehicule.getString("plaque_vehicule"));
                ResultSet rsReservation = pstmt2.executeQuery();

                while (rsReservation.next()) {
                    reservations.add(searchReservation(rsReservation));
                }
                rsReservation.close();
            }

            rsVehicule.close();
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Reservation insertReservation(int id_borne, String plaque_vehicule, Timestamp date_debut_reservation, Timestamp date_fin_reservation) {
        String sql = "INSERT INTO reservation (id_borne, plaque_vehicule, date_debut_reservation, date_fin_reservation) VALUES ( ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_borne);
            pstmt.setString(2, plaque_vehicule);
            pstmt.setTimestamp(3, date_debut_reservation);
            pstmt.setTimestamp(4, date_fin_reservation);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            String sql2 = "SELECT * FROM reservation WHERE id_borne = ? AND plaque_vehicule = ? AND date_debut_reservation = ? AND date_fin_reservation = ?";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, id_borne);
            pstmt2.setString(2, plaque_vehicule);
            pstmt2.setTimestamp(3, date_debut_reservation);
            pstmt2.setTimestamp(4, date_fin_reservation);
            ResultSet rs = pstmt2.executeQuery();
            rs.next();

            return searchReservation(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reservation accederReservation(int num_reservation, int client_id) {
        String sql = "SELECT * FROM reservation " +
                "INNER JOIN client_vehicule cv ON reservation.plaque_vehicule = cv.plaque_vehicule " +
                "WHERE cv.id_client = ? " +
                "AND reservation.num_reservation = ? " +
                "AND reservation.date_fin_reservation > now() " +  // La date de fin de la réservation est dans le futur
                "AND reservation.date_debut_reservation <= now() + interval '5 minutes' " +  // La date de début de la réservation est passée ou dans les 5 prochaines minutes
                "AND reservation.date_debut_reservation >= now() - interval '10 minutes';";  // La date de début de la réservation est dans les 10 dernières minutes


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ArrayList<Reservation> reservations = new ArrayList<>();

            pstmt.setInt(1, client_id);
            pstmt.setInt(2, num_reservation);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return searchReservation(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Reservation> accederReservation(String plaque, int client_id) {
        String sql = "SELECT * FROM reservation " +
                "INNER JOIN client_vehicule cv ON reservation.plaque_vehicule = cv.plaque_vehicule " +
                "WHERE cv.id_client = ? " +
                "AND reservation.plaque_vehicule = ? " +
                "AND reservation.date_fin_reservation > now() " +  // La date de fin de la réservation est dans le futur
                "AND reservation.date_debut_reservation <= now() + interval '5 minutes' " +  // La date de début de la réservation est passée ou dans les 5 prochaines minutes
                "AND reservation.date_debut_reservation >= now() - interval '10 minutes';";  // La date de début de la réservation est dans les 10 dernières minutes

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, client_id);
            pstmt.setString(2, plaque);

            ArrayList<Reservation> reservations = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(searchReservation(rs));
                }
            }
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Reservation searchReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setNum_reservation(rs.getInt("num_reservation"));
        reservation.setId_borne(rs.getInt("id_borne"));
        reservation.setPlaque_vehicule(rs.getString("plaque_vehicule"));
        reservation.setDate_debut_reservation(rs.getTimestamp("date_debut_reservation"));
        reservation.setDate_fin_reservation(rs.getTimestamp("date_fin_reservation"));
        return reservation;
    }

    public void deleteReservation(int numReservation) {
        String sql = "DELETE FROM reservation WHERE num_reservation = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numReservation);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
