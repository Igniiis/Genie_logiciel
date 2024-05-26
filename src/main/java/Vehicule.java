import java.util.ArrayList;

public class Vehicule {

    private String plaque;
    private ArrayList<Integer> listeClients;

    public Vehicule(String plaque){
        this.plaque = plaque;
        this.listeClients = new ArrayList<>();
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public ArrayList<Integer> getListeClients() {
        return listeClients;
    }

    public void addClient(int id_client){
        this.listeClients.add(id_client);
    }

    public void removeClient(int id_client){
        this.listeClients.remove(Integer.valueOf(id_client));
    }
}