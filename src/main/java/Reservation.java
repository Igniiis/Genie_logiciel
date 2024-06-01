import java.sql.Timestamp;

public class Reservation {

    //créer les attributs
    private int num_reservation;
    private int id_borne;
    private String plaque_vehicule;
    private Timestamp date_debut_reservation;
    private Timestamp date_fin_reservation;

    //créer le constructeur
    public Reservation() {
    }

    //créer les getters et setters
public int getNum_reservation() {
        return num_reservation;
    }

public void setNum_reservation(int num_reservation) {
        this.num_reservation = num_reservation;
    }

public int getId_borne() {
        return id_borne;
    }

public void setId_borne(int id_borne) {
        this.id_borne = id_borne;
    }

public String getPlaque_vehicule() {
        return plaque_vehicule;
    }

public void setPlaque_vehicule(String plaque_vehicule) {
        this.plaque_vehicule = plaque_vehicule;
    }

public Timestamp getDate_debut_reservation() {
        return date_debut_reservation;
    }

public void setDate_debut_reservation(Timestamp date_debut_reservation) {
        this.date_debut_reservation = date_debut_reservation;
    }

public Timestamp getDate_fin_reservation() {
        return date_fin_reservation;
    }

public void setDate_fin_reservation(Timestamp date_fin_reservation) {
        this.date_fin_reservation = date_fin_reservation;
    }
}
