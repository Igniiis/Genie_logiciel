import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationTest {

    private Application app;


    @BeforeEach
    void setUp() {
        app = new Application();
    }


    @Test
    @DisplayName("Test enregistrement d'un nouveau client")
    void testEnregistrementClient() {
        // Préparation des données de test
        String nom = "LEYLI";
        String prenom = "Manon";
        String email = "manon@example.com";
        String numTel = "2121212121";
        String password = "password";

        String input = nom + "\n" + prenom + "\n" + email + "\n" + numTel + "\n" + password + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Exécution de la méthode d'enregistrement directement
        int result = app.enregistrement();

        // Vérification des résultats
        assertEquals(1, result, "Le retour de la méthode enregistrement devrait être 1");

        // Vérification si le client a bien été enregistré
        Client actualClient = app.getActualClient();
        assertNotNull(actualClient, "Le client enregistré ne devrait pas être null");
        assertEquals(nom, actualClient.getNomClient(), "Le nom du client enregistré devrait être " + nom);
        assertEquals(prenom, actualClient.getPrenomClient(), "Le prénom du client enregistré devrait être " + prenom);
        assertEquals(email, actualClient.getAdresseClient(), "L'adresse email du client enregistré devrait être " + email);
        assertEquals(numTel, actualClient.getNumTelClient(), "Le numéro de téléphone du client enregistré devrait être " + numTel);
        assertEquals(password, actualClient.getPasswordClient(), "Le mot de passe du client enregistré devrait être " + password);

        // Vérification de la liste de véhicules vide
        assertTrue(actualClient.getListeVehicules().isEmpty(), "La liste de véhicules du client devrait être vide");
    }


    @Test
    @DisplayName("Informations clients manquantes")
    void EnregistrementInfoManquantes() {
        // Préparation des données de test avec des informations manquantes (par exemple, pas de nom)
        String nom = ""; // Nom vide
        String prenom = "Manon";
        String email = "manon@example.com";
        String numTel = "21212121";
        String password = "password";

        // Simuler les entrées utilisateur dans la console
        String input = nom + "\n" + prenom + "\n" + email + "\n" + numTel + "\n" + password + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Exécution de la méthode d'enregistrement directement et capture de l'exception
        assertThrows(NoSuchElementException.class, () -> app.enregistrement(),
                "L'enregistrement devrait lever une NoSuchElementException lorsque des informations sont manquantes");
    }

    @Test
    @DisplayName("Test ajout d'un véhicule")
    void testAjoutVehicule() {
        // Création d'un client pour utiliser dans le test
        Client client = new Client();
        client.setId(1); // Assurez-vous que l'ID du client est défini

        app.setActualClient(client);

        // Préparation des données de test pour le véhicule
        String plaque = "ML-271-TH"; // Plaque d'immatriculation de test

        // Simuler les entrées utilisateur dans la console
        String input = plaque + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Exécution de la méthode d'ajout de véhicule
        int result = app.ajouter_voiture();

        // Vérification des résultats
        assertEquals(-1, result, "Le retour de la méthode ajouter_voiture devrait être -1");

        // Vérification si le véhicule a bien été ajouté au client
        assertNotNull(client.getListeVehicules(), "La liste de véhicules du client ne devrait pas être null");
        assertEquals(1, client.getListeVehicules().size(), "Le client devrait avoir un véhicule dans sa liste");
        assertEquals(plaque, client.getListeVehicules().get(0), "La plaque du véhicule ajouté devrait être " + plaque);
    }



}
