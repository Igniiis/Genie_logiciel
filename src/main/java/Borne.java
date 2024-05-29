import java.util.ArrayList;
import java.util.Scanner;

public class Borne {

    private int etat;
    private Scanner sc;
    private Client actualClient;
    private ClientDAO clientDAO;
    private VehiculeDAO vehiculeDAO;

    public Borne(){
        this.sc = new Scanner(System.in);
        this.etat = 1;
        this.actualClient = null;
        this.clientDAO = new ClientDAO();
        this.vehiculeDAO = new VehiculeDAO();
    }

    public int start(){
        System.out.println("Bonjour, bienvenue sur Elisabeth Borne");

        //démarrage
        this.choixHeader();

        return 1;
    }

    private int choixHeader(){
        if(this.actualClient == null){
            System.out.print("que voulez-vous faire ?");
            System.out.print("\n1. S'Enregistrer\n2. Connecter\n3. Quitter \n");

            switch(lireEntierAvecVerification(3,1)){
                case 1:
                    this.enregistrement();
                    this.choixHeader();
                    break;
                case 2:
                    this.connexion();
                    this.choixHeader();
                    break;
                case 3:
                    System.out.println("Au revoir");
                    return -1; //on quitte l'application
                default:
                    choixHeader();
            }

        }else{
            System.out.println("Bonjour " + this.actualClient.getNomClient() + " " + this.actualClient.getPrenomClient() + ", que voulez-vous faire ?");
            System.out.println("1. Réservations  \n2. Voitures \n3. Mon Compte \n4. Quitter");

            switch(lireEntierAvecVerification(4,1)){
                case 1:
                    this.choixReservation();
                    break;
                case 2:
                    this.choixVoitures();
                    this.choixHeader();
                    break;
                case 3:
                    this.choixCompte();
                    this.choixHeader();
                case 4:
                    return -1; //quitter l'application
                default:
                    choixHeader();
            }
        }
        System.out.println("Au revoir.");
        return 3;
    }

    private void choixVoitures(){
        System.out.println("-----Voitures-----");
        System.out.println("1. Ajouter une voiture \n2. Enlever une voiture TODO \n3. Retour");

        switch (lireEntierAvecVerification(3,1)){
            case 1:
                this.ajouter_voiture();
                this.choixVoitures();
                break;
            case 2:
                this.removeCars();
                this.choixVoitures();
                break;
            case 3:
                System.out.println("retour");
                break;
        }
    }

    private void choixCompte(){
        System.out.println("-----Compte ("+this.actualClient.getNomClient()+" " +this.actualClient.getPrenomClient()+")-----");
        System.out.println("1. Modifier nom \n2. Modifier prénom \n3. Modifier numéro de tel \n4. Modifier mot de passe \n5. Se déconnecter \n6. Retour");
        switch (lireEntierAvecVerification(6,1)){
            case 1:
                this.modifClient("nom");
                this.choixCompte();
                break;
            case 2:
                this.modifClient("prenom");
                this.choixCompte();
                break;
            case 3:
                this.modifClient("num");
                this.choixCompte();
                break;
            case 4:
                this.modifClient("password");
                this.choixCompte();
                break;
            case 5:
                System.out.println("déconnexion...");
                this.actualClient = null;
                System.out.println("déconnecté");
                break;
            case 6:
                System.out.println("retour");
                break;
        }
    }

    private void modifClient(String type){
        System.out.println(type);
        this.sc = new Scanner(System.in);
        switch (type){
            case "nom":
                this.actualClient.setNomClient(this.lireStringAvecVerification(50));
                break;
            case "prenom":
                this.actualClient.setPrenomClient(this.lireStringAvecVerification(50));
                break;
            case "num":
                this.actualClient.setNumTelClient(this.lireStringAvecVerification(15));
                break;
            case "password":
                this.actualClient.setPasswordClient(this.lireStringAvecVerification(255, 4));
                break;
        }
    }

    private void connexion() {
        System.out.println("----Page de connexion----");
        while(true){
            this.sc  = new Scanner(System.in);
            System.out.print("adresse mail : ");
            String mail = this.lireStringAvecVerification(255);
            System.out.print("mot de passe : ");
            String password = this.lireStringAvecVerification(255);
            Client client = this.clientDAO.getClient(mail, password) ;
            if(client != null){
                System.out.println("Vous êtes bien connecé");
                this.actualClient = client;
                return;
            }else{
                System.out.print("Ce client n'existe pas\n1. Réessayer \n2. Retour \n");
                int x = lireEntierAvecVerification(2,1);
                if(x == 2){
                    return;
                }
            }
        }
    }

    private int choixReservation(){
        System.out.println("----Page des réservations----");
        return -1;
    }

    private int removeCars(){
        System.out.println("Quel voiture supprimer ?");
        int size = this.actualClient.getListeVehicules().size();
        for (int i = 0; i < size; i++) {
            System.out.println((i+1) + ". plaque " + this.actualClient.getListeVehicules().get(i));
        }

        System.out.println("0. Retour");
        int res = this.lireEntierAvecVerification(size + 1, 0);

        if(res == 0){
            this.choixHeader();
        }else{
            this.actualClient = this.clientDAO.removeVehicule(this.actualClient, this.actualClient.getListeVehicules().get(res-1));
        }

        return -1;
    }

    private int ajouter_voiture(){
        this.sc = new Scanner(System.in);
        System.out.print("-----Page d'ajout d'une voiture-----");
        System.out.print("\nVeuillez donner la plaque d'immatriculation de la voiture : ");
        String plaque = "";

        int x = 0;
        while (plaque.length()!=9){
            if(x>0){
                System.out.print("\nVeuillez donner un numéro de plaque valide (exemple : 'AA-229-AA') : \n");
            }
            plaque = this.lireStringAvecVerification(9);
            x++;
        }

        System.out.println("bravo vous êtes sorti de la boucle, le véhicule va être ajouté à votre compte");

        if(this.vehiculeDAO.insertVehicule(new Vehicule(plaque), this.actualClient)){
            //si l'insertion fonctionne bien on ajoute la voiture à la liste du propriétaire
            this.actualClient.addVehicule(plaque);
            System.out.println("Le véhicule est ajouté à votre compte " + this.actualClient.getNomClient() + " " + this.actualClient.getPrenomClient());
        }else{
            System.out.println("/!\\ ALERTE ERREUR /!\\");
        }

        return -1;
    }


    private int enregistrement(){
        this.sc = new Scanner(System.in);
        Client newClient = new Client();
        System.out.print("------------Page d'enregistrement------------");
        System.out.print("\nVeuillez remplir les informations requises \nVotre nom : ");

        newClient.setNomClient(this.lireStringAvecVerification(50));
        System.out.print("Votre prénom : ");
        newClient.setPrenomClient(this.lireStringAvecVerification(50));
        System.out.print("Votre adresse mail (login) : ");
        newClient.setAdresseClient(this.lireStringAvecVerification(255));
        System.out.print("Votre numéro de téléphone : ");
        newClient.setNumTelClient(this.lireStringAvecVerification(15));
        System.out.print("Votre mot de passe : ");
        newClient.setPasswordClient(this.lireStringAvecVerification(255));

        Client reelClient = this.clientDAO.getClient(newClient.getAdresseClient());
        if(reelClient == null){
            //si il n'existe pas déjà de client avec cette adresse mail
            Integer new_id = this.clientDAO.insertClient(newClient);
            if(new_id != null){
                //si l'insertion fonctionne parfaitement, alors on actualise le client actuel
                newClient.setId(new_id);
                this.actualClient = newClient;
                System.out.println("Enregistrement effectué");
                return 1;
            }
            System.out.println("/!\\ Erreur lors de l'enregistrement /!\\");
            return -1;
        }

        System.out.println("L'adresse mail est déjà utilisé pour un compte, veuillez vous connecter");
        return -1;
    }

    //fonctions de vérifications
    private int lireEntierAvecVerification(int max){
        return lireEntierAvecVerification( max, 0);
    }

    private int lireEntierAvecVerification(int max, int min) {
        while (true){ //boucle tant qu'on a une erreur
            if (this.sc.hasNextInt()) {
                int res = this.sc.nextInt();
                if(res<min || res > max){
                    if(res > max){
                        System.out.println("Valeur numérique trop grande");
                    }else{
                        System.out.println("Valeur numérique trop petite");
                    }
                }else{
                    return res;
                }
            } else {
                // Consommer l'entrée incorrecte pour éviter une boucle infinie
                System.out.println("valeur non numérique : " + this.sc.next());
            }
        }
    }

    private String lireStringAvecVerification(int maxLength, int minLength) {
        while (true) { // boucle tant qu'on a une erreur
            String res = this.sc.nextLine();

            if (res.length() > maxLength) {
                System.out.print("Valeur trop longue, maximum autorisé : " + maxLength + " caractères\n");
            } else if (res.isEmpty()) {
                System.out.print("Valeur non valide, veuillez entrer une chaîne non vide\n");
            } else if (res.length() < minLength){
                System.out.print("Valeur trop courte, minimum autorisé : " + minLength + " caractères\n");
            }else {
                return res;
            }
        }
    }


    private String lireStringAvecVerification(int maxLength) {
        return this.lireStringAvecVerification(maxLength, 1);
    }

}