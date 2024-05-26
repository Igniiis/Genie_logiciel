import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehiculeTest {

    private Vehicule vehicule;

    @BeforeEach
    void setUp() {
        vehicule = new Vehicule("AA-229-AA");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals("AA-229-AA", vehicule.getPlaque());
        assertTrue(vehicule.getListeClients().isEmpty());
    }

    @Test
    void testAddClient() {
        vehicule.addClient(1);
        assertEquals(1, vehicule.getListeClients().size());
        assertTrue(vehicule.getListeClients().contains(1));
    }

    @Test
    void testRemoveClient() {
        vehicule.addClient(1);
        vehicule.removeClient(1);
        assertFalse(vehicule.getListeClients().contains(1));
    }
}
