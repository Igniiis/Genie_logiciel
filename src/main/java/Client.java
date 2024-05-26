import java.util.ArrayList;

public class Client {
    private int id;
    private String nomClient;
    private String prenomClient;
    private String adresseClient;
    private String numTelClient;
    private String passwordClient;
    private ArrayList<String> listeVehicules;

    public Client (){
        this.listeVehicules = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getNumTelClient() {
        return numTelClient;
    }

    public void setNumTelClient(String numTelClient) {
        this.numTelClient = numTelClient;
    }

    public ArrayList<String> getListeVehicules() {
        return listeVehicules;
    }

    public void addVehicule(String plaque){
        this.listeVehicules.add(plaque);
    }

    public void removeVehicule(String plaque){
        this.getListeVehicules().remove(plaque);
    }

    public void setPasswordClient(String passwordClient) {
        this.passwordClient = passwordClient;
    }

    public String getPasswordClient() {
        return passwordClient;
    }

    public void setListeVehicules(ArrayList<String> listeVehicules) {
        this.listeVehicules = listeVehicules;
    }
}
