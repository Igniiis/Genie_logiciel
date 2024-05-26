import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);
        client.setNomClient("Doe");
        client.setPrenomClient("John");
        client.setAdresseClient("123 Main St");
        client.setNumTelClient("1234567890");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, client.getId());
        assertEquals("Doe", client.getNomClient());
        assertEquals("John", client.getPrenomClient());
        assertEquals("123 Main St", client.getAdresseClient());
        assertEquals("1234567890", client.getNumTelClient());
        assertTrue(client.getListeVehicules().isEmpty());
    }

    @Test
    void testAddVehicule() {
        client.addVehicule("AA-229-AA");
        assertEquals(1, client.getListeVehicules().size());
        assertTrue(client.getListeVehicules().contains("AA-229-AA"));
    }

    @Test
    void testRemoveVehicule() {
        client.addVehicule("AA-229-AA");
        client.removeVehicule("AA-229-AA");
        assertFalse(client.getListeVehicules().contains("AA-229-AA"));
    }
}
