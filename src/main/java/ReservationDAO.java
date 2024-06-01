import java.sql.*;

public class ReservationDAO {

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

            String sql2 = "SELECT num_reservation FROM reservation WHERE id_borne = ? AND plaque_vehicule = ? AND date_debut_reservation = ? AND date_fin_reservation = ?";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, id_borne);
            pstmt2.setString(2, plaque_vehicule);
            pstmt2.setTimestamp(3, date_debut_reservation);
            pstmt2.setTimestamp(4, date_fin_reservation);
            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            int num_reservation = rs.getInt("num_reservation");


            Reservation reservation = new Reservation();
            reservation.setNum_reservation(num_reservation);
            reservation.setId_borne(id_borne);
            reservation.setPlaque_vehicule(plaque_vehicule);
            reservation.setDate_debut_reservation(date_debut_reservation);
            reservation.setDate_fin_reservation(date_fin_reservation);
            return reservation;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
