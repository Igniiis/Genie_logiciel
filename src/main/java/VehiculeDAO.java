import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehiculeDAO {

    public boolean insertVehicule(Vehicule vehicule) {
        String sql = "INSERT INTO vehicule (plaque_vehicule) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicule.getPlaque());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertVehicule(Vehicule vehicule, Client client) {
        String checkSql = "SELECT 1 FROM vehicule WHERE plaque_vehicule = ?";
        String insertSql = "INSERT INTO vehicule (plaque_vehicule) VALUES (?)";
        String linkSql = "INSERT INTO client_vehicule (id_client, plaque_vehicule) VALUES (?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            // Début de la transaction
            conn.setAutoCommit(false);

            PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
            checkPstmt.setString(1, vehicule.getPlaque());

            try (ResultSet rs = checkPstmt.executeQuery()) {
                if (!rs.next()) {
                    // Le véhicule n'existe pas, on l'insère
                    PreparedStatement insertPstmt = conn.prepareStatement(insertSql);
                    insertPstmt.setString(1, vehicule.getPlaque());
                    insertPstmt.executeUpdate();
                }
            }

            PreparedStatement linkPstmt = conn.prepareStatement(linkSql);
            linkPstmt.setInt(1, client.getId());
            linkPstmt.setString(2, vehicule.getPlaque());
            linkPstmt.executeUpdate();

            // Commit de la transaction
            conn.commit();
            conn.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    // Rollback de la transaction en cas d'erreur
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}