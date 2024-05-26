import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public Integer insertClient(Client client) {
        String sql = "INSERT INTO client (nom_client, prenom_client, adresse_client, password_client, num_tel_client) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, client.getNomClient());
            pstmt.setString(2, client.getPrenomClient());
            pstmt.setString(3, client.getAdresseClient());
            pstmt.setString(4, client.getPasswordClient());
            pstmt.setString(5, client.getNumTelClient());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'insertion du client a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("L'insertion du client a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client removeVehicule(Client client, String plaque) {
        String sql = "DELETE FROM client_vehicule WHERE id_client = ? AND plaque_vehicule = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, client.getId());
            pstmt.setString(2, plaque);

            pstmt.executeUpdate();

            // Remove the vehicle from the client's list of vehicles
            client.removeVehicule(plaque);
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getClient(String mail){
        String sql = "SELECT * FROM client WHERE adresse_client = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mail);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return searchClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getClient(String mail, String password) {
        String sql = "SELECT * FROM client WHERE adresse_client = ? AND password_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mail);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return searchClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Client getClient(int id_client) {
        String sql = "SELECT * FROM client WHERE id_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_client);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return searchClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Client searchClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id_client"));
        client.setNomClient(rs.getString("nom_client"));
        client.setPrenomClient(rs.getString("prenom_client"));
        client.setAdresseClient(rs.getString("adresse_client"));
        client.setNumTelClient(rs.getString("num_tel_client"));
        client.setPasswordClient(rs.getString("password_client"));

        // Assuming there's a list of vehicles associated with the client
        String sqlVehicules = "SELECT plaque_vehicule FROM client_vehicule WHERE id_client = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtVehicules = conn.prepareStatement(sqlVehicules)) {

            pstmtVehicules.setInt(1, client.getId());

            try (ResultSet rsVehicules = pstmtVehicules.executeQuery()) {
                ArrayList<String> vehicules = new ArrayList<>();
                while (rsVehicules.next()) {
                    vehicules.add(rsVehicules.getString("plaque_vehicule"));
                }
                client.setListeVehicules(vehicules);
            }
        }

        return client;
    }
}
