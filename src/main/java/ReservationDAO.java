import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {

    public String recupReservation(int id_client) {

        StringBuilder res = new StringBuilder(); // Initialise un StringBuilder

        String sql = "SELECT id_reservation,id_client,id_borne,plaque_vehicule,date_debut_reservation,date_fin_reservation FROM reservation WHERE id_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_client);
            ResultSet rs = pstmt.executeQuery();

            // Parcourt le résultat de la requête
            while (rs.next()) {
                int id_reservation = rs.getInt("id_reservation");
                int id_borne = rs.getInt("id_borne");
                String plaque_vehicule = rs.getString("plaque_vehicule");
                String date_debut_reservation = rs.getString("date_debut_reservation");
                String date_fin_reservation = rs.getString("date_fin_reservation");

                // Ajoute les informations à la chaîne de caractères
                res.append("ID réservation: ").append(id_reservation)
                        .append(", ID borne: ").append(id_borne)
                        .append(", Plaque véhicule: ").append(plaque_vehicule)
                        .append(", Date début réservation: ").append(date_debut_reservation)
                        .append(", Date fin réservation: ").append(date_fin_reservation)
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Retourne la chaîne de caractères résultante
        return res.toString();
    }
}
