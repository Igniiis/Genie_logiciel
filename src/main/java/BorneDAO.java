import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class BorneDAO {

    public ArrayList<Borne> getBorne (String etat_borne){

        String sql = "SELECT * FROM borne WHERE etat_borne = ?";
        ArrayList<Borne> borne = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ){

            pstmt.setString(1, etat_borne);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                borne.add(createBorne(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return borne;
    }

    public ArrayList<Borne> getBorne (String etat_borne, Timestamp arrive, Timestamp depart){

            String sql = "WITH reservations_conflict AS (\n" +
                    "    SELECT\n" +
                    "        id_borne\n" +
                    "    FROM\n" +
                    "        Reservation\n" +
                    "    WHERE\n" +
                    "        (date_debut_reservation < ? AND date_fin_reservation > ?) OR\n" +
                    "        (date_debut_reservation < ? AND date_fin_reservation > ?) OR\n" +
                    "        (date_debut_reservation >= ? AND date_fin_reservation <= ?)\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    id_borne, etat_borne\n" +
                    "FROM\n" +
                    "    Borne\n" +
                    "WHERE\n" +
                    "    id_borne NOT IN (SELECT id_borne FROM reservations_conflict) AND etat_borne = ?;";

            ArrayList<Borne> borne = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
            ){

                pstmt.setTimestamp(1, arrive);
                pstmt.setTimestamp(2, arrive);
                pstmt.setTimestamp(3, depart);
                pstmt.setTimestamp(4, depart);
                pstmt.setTimestamp(5, arrive);
                pstmt.setTimestamp(6, depart);
                pstmt.setString(7, etat_borne);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    borne.add(createBorne(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return borne;
    }


    public Borne createBorne (ResultSet rs) {
        Borne b = new Borne();
        try {
            b.setId_borne(rs.getInt("id_borne"));
            b.setEtat_borne(rs.getString("etat_borne"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }



}
