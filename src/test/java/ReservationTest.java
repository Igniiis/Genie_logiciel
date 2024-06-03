import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
    }

    @Test
    public void testSetAndGetNum_reservation() {
        int numReservation = 123;
        reservation.setNum_reservation(numReservation);
        assertEquals(numReservation, reservation.getNum_reservation());
    }

    @Test
    public void testSetAndGetId_borne() {
        int idBorne = 456;
        reservation.setId_borne(idBorne);
        assertEquals(idBorne, reservation.getId_borne());
    }

    @Test
    public void testSetAndGetPlaque_vehicule() {
        String plaqueVehicule = "ABC-123";
        reservation.setPlaque_vehicule(plaqueVehicule);
        assertEquals(plaqueVehicule, reservation.getPlaque_vehicule());
    }

    @Test
    public void testSetAndGetDate_debut_reservation() {
        Timestamp dateDebut = Timestamp.valueOf("2024-06-03 10:00:00");
        reservation.setDate_debut_reservation(dateDebut);
        assertEquals(dateDebut, reservation.getDate_debut_reservation());
    }

    @Test
    public void testSetAndGetDate_fin_reservation() {
        Timestamp dateFin = Timestamp.valueOf("2024-06-03 12:00:00");
        reservation.setDate_fin_reservation(dateFin);
        assertEquals(dateFin, reservation.getDate_fin_reservation());
    }
}
