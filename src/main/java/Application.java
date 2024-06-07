import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Application {

    private int etat;
    private Scanner sc;
    private Client actualClient;
    private ClientDAO clientDAO;
    private VehiculeDAO vehiculeDAO;
    private ReservationDAO reservationDAO;
    private BorneDAO borneDAO;

    public Application(){
        this.sc = new Scanner(System.in);
        this.etat = 1;
        this.actualClient = null;
        this.clientDAO = new ClientDAO();
        this.vehiculeDAO = new VehiculeDAO();
        this.reservationDAO = new ReservationDAO();
        this.borneDAO = new BorneDAO();
    }

    public int start(){
        System.out.println("Bonjour, bienvenue sur Elisabeth Borne");

        //démarrage
        this.choixHeader();

        return 1;
    }

    private int choixHeader(){
        boolean test = true;
        while (test){
            if(this.actualClient == null){
                System.out.print("que voulez-vous faire ?");
                System.out.print("\n1. S'Enregistrer\n2. Connecter\n3. Quitter \n");

                switch(lireEntierAvecVerification(3,1)){
                    case 1:
                        this.enregistrement();
                        break;
                    case 2:
                        this.connexion();
                        break;
                    case 3:
                        System.out.println("Au revoir");
                        test = false;
                        return -1; //on quitte l'application
                    default:
                        System.out.println("Erreur");
                }

            }else{
                System.out.println("Bonjour " + this.actualClient.getNomClient() + " " + this.actualClient.getPrenomClient() + ", que voulez-vous faire ?");
                System.out.println("1. Réservations  \n2. Voitures \n3. Accéder à une borne libre \n4. Mon Compte \n5. Quitter");

                switch(lireEntierAvecVerification(5,1)){
                    case 1:
                        this.choixReservation();
                        break;
                    case 2:
                        this.choixVoitures();
                        break;
                    case 3:
                        this.utiliserBorne();
                        break;
                    case 4:
                        this.choixCompte();
                        break;
                    case 5:
                        System.out.println("Au revoir.");
                        test = false;
                        return -1; //quitter l'application
                    default:
                        System.out.println("Erreur");
                }
            }
        }
        return 1;
    }
    private void utiliserBorne(){
        System.out.println("Pendant combien de temps voulez-vous utiliser la borne ?");

        Timestamp now = Timestamp.from(Instant.now());

        Timestamp end = this.lireHeureAvecVerification(now);


        //récupération des bornes dispo pour ce temps
        ArrayList<Borne> bornes = this.borneDAO.getBornes(now, end);

        if(bornes.size()==0){
            System.out.println("Aucune bornes disponibles pour vos horaires");
            return;
        }

        System.out.println("Voici les bornes disponibles : ");
        int i = 1;
        for (Borne borne: bornes) {
            System.out.println(i + ". Borne " + borne.getId_borne());
            i++;
        }

        System.out.println("0. Retour");
        int res = lireEntierAvecVerification(i);
        if(res > 0){
            System.out.println("Veuillez sélectionner votre voiture :");
            String voiture = this.listeVehicule();

            if(voiture == null){
                System.out.println("Annulé");
                return;
            }

            this.reserverBorne(bornes.get(res-1).getId_borne(), now, end, voiture);
        }
    }

    private void choixVoitures(){
        System.out.println("-----Voitures-----");
        System.out.println("1. Ajouter une voiture \n2. Enlever une voiture \n3. Retour");

        switch (lireEntierAvecVerification(3,1)){
            case 1:
                this.ajouter_voiture();
                break;
            case 2:
                this.removeCars();
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
        System.out.println("----Page des réservations de " + this.actualClient.getNomClient() + " " + this.actualClient.getPrenomClient() + "----");
        System.out.println("1. Réserver une borne \n2. Accéder à ma Réservation \n3. Retour");
        switch(lireEntierAvecVerification(2,1)){
            case 1:
                this.afficherBornes();
                break;
            case 2:
                this.accederAMaReservation();
                break;
            case 3:
                break;
        }
        return -1;
    }

    public int afficherBornes(){
        //demander le temps de réservation

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        System.out.println("Entrez la date d'arrivé (yyyy-MM-dd HH:mm):");
        Timestamp arrive = this.lireDateAvecVerification();

        System.out.println("Entrez la date de départ (yyyy-MM-dd HH:mm):");
        Timestamp depart = this.lireDateAvecVerification();

        ArrayList<Borne> bornes = this.borneDAO.getBornes(arrive, depart);

        if (arrive != null && depart != null) {
            long differenceInMillis = depart.getTime() - arrive.getTime();
            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);

            if (arrive.before(depart) && differenceInHours <= 6) {

                System.out.println("----Choisir votre borne----");
                int sizeBorne = bornes.size();
                for (int i = 0; i < sizeBorne; i++) {
                    System.out.println((i+1) + ". Borne " + bornes.get(i).getId_borne());
                }

                System.out.println("0. Retour");
                this.sc = new Scanner(System.in);
                int resBorne = this.lireEntierAvecVerification(sizeBorne, 0);

                if(resBorne == 0) {
                    return 1;
                }

                System.out.println("----Choisir votre véhicule pour la réservation----");
                int sizeVehicule = this.actualClient.getListeVehicules().size();
                for (int i = 0; i < sizeVehicule; i++) {
                    System.out.println((i+1) + ". plaque " + this.actualClient.getListeVehicules().get(i));
                }

                System.out.println("0. Retour");
                this.sc = new Scanner(System.in);
                int resVehicule = this.lireEntierAvecVerification(sizeVehicule + 1, 0);

                if(resVehicule == 0) {
                    return 1;
                }

                System.out.println("Vous avez choisi la borne " + bornes.get(resBorne-1).getId_borne() + " et le véhicule " + this.actualClient.getListeVehicules().get(resVehicule-1) + " pour la réservation.");
                System.out.println("Voulez-vous confirmer la réservation ? (1. Oui / 2. Non)");
                int res = this.lireEntierAvecVerification(2, 1);

                int num_res;

                if(res == 2) {
                    return 1;
                } else {
                    num_res = this.reserverBorne(bornes.get(resBorne-1).getId_borne(), arrive, depart, this.actualClient.getListeVehicules().get(resVehicule-1));
                    if(num_res != -1) {
                        System.out.println("Réservation effectuée, votre numéro de réservation est " + num_res + ".");
                    }
                }

            } else {
                System.out.println("Problème de date entrée : vérifier si la date de départ est après la date d'arrivée et si la durée de la réservation est inférieure à 6 heures.");
                this.choixReservation();
            }
        } else {
            System.out.println("Date Invalide.");
            this.choixReservation();
        }

        return -1;
    }

    public int reserverBorne(int id_borne, Timestamp arrive, Timestamp depart, String plaque) {

        Reservation reservation = this.reservationDAO.insertReservation(id_borne, plaque, arrive, depart);

        if (reservation != null) {
            return reservation.getNum_reservation();
        } else {
            System.out.println("Reservation failed.");
            return -1;
        }
    }

    public int accederAMaReservation() {
        System.out.println("----Accéder à ma réservation----");
        System.out.println("Souhaitez vous rentrer votre plaque ou votre numéro de réservation ? (1. Plaque / 2. Numéro de réservation)");
        int num_res = this.lireEntierAvecVerification(Integer.MAX_VALUE, 1);

        Reservation reservation;

        if (num_res == 1) {
            System.out.println("Entrez votre plaque :");
            this.sc = new Scanner(System.in);
            String plaque = this.lireStringAvecVerification(9);
            ArrayList<Reservation> reservations = this.reservationDAO.accederReservation(plaque, this.actualClient.getId());

            if(reservations.isEmpty()) {
                System.out.println("Aucune réservation trouvée.");
                return 1;
            }

            //afficher les réservations
            System.out.println("----Liste des réservations à cette plaque----");
            for (int i = 0; i < reservations.size(); i++) {
                System.out.println((i+1) + ". Réservation " + reservations.get(i).getNum_reservation() + " : "
                        + reservations.get(i).getDate_debut_reservation() + " - " + reservations.get(i).getDate_fin_reservation());
            }

            System.out.println("0. Retour");
            System.out.print("Choisissez une réservation : ");

            int res = this.lireEntierAvecVerification(reservations.size()+1, 0);

            if(res == 0) {
                return 1;
            }

            reservation = reservations.get(res-1);

        } else {
            System.out.println("Entrez votre numéro de réservation :");
            System.out.println("0. Retour");
            this.sc = new Scanner(System.in);
            int res  = this.lireEntierAvecVerification(Integer.MAX_VALUE, 0);

            if(res == 0) {
                return 1;
            }

            System.out.println("êtes-vous sûr de vouloir accéder à la réservation " + res + " ? (1. Oui / 2. Non)");

            int confirm = this.lireEntierAvecVerification(2, 1);

            if(confirm == 2) {
                return 1;
            }
            reservation = this.reservationDAO.accederReservation(res, this.actualClient.getId());


            if(reservation == null) {
                System.out.println("Aucune réservation trouvée.");
                return 1;
            }
        }

        String testReserve = this.borneDAO.getBorne(reservation.getId_borne()).getEtat_borne();

        if (testReserve.equals("reservé")) {
            System.out.println("La borne est déjà réservée.");
            return 1;
        }

        Borne b = this.borneDAO.updateBorne(reservation.getId_borne(), "reservé");

        System.out.println("----Accès à la réservation " + reservation.getNum_reservation() + " - Borne : " + b.getId_borne() + "- de " + reservation.getDate_debut_reservation() + " à " + reservation.getDate_fin_reservation() + "----");
        return 1;
    }

    private String listeVehicule(){
        int size = this.actualClient.getListeVehicules().size();
        for (int i = 0; i < size; i++) {
            System.out.println((i+1) + ". plaque " + this.actualClient.getListeVehicules().get(i));
        }
        System.out.println("0. Retour");
        int res = this.lireEntierAvecVerification(size + 1, 0);

        if(res == 0){
            return null;
        }

        return this.actualClient.getListeVehicules().get(res-1);
    }

    private int removeCars(){
        System.out.println("Quel voiture supprimer ?");

        String voiture = this.listeVehicule();

        this.actualClient = this.clientDAO.removeVehicule(this.actualClient, voiture);
        return 1;
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

    private Timestamp lireDateAvecVerification(){
        this.sc = new Scanner(System.in);


        Timestamp timestamp = null;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print("\nDonnez le jour (1 à 31) : ");
                int day = lireEntierAvecVerification(31, 1);
                System.out.print("\nDonnez le mois (1 à 12) : ");
                int month = lireEntierAvecVerification(12, 1) - 1; // Month is 0-based in Calendar
                System.out.print("\nDonnez l'heure (0 à 23) : ");
                int hour = lireEntierAvecVerification( 23);
                System.out.print("\nDonnez les minutes (0 à 59) : ");
                int minutes = lireEntierAvecVerification( 59);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                timestamp = new Timestamp(calendar.getTimeInMillis());
                valid = true; // If we reach this point, the timestamp is valid
            } catch (Exception e) {
                System.out.println("La date fournie est invalide. Veuillez réessayer.");
            }
        }

        return timestamp;
    }

    private Timestamp lireHeureAvecVerification(Timestamp now){
        this.sc = new Scanner(System.in);

        Timestamp timestamp = null;
        boolean valid = false;

        while(!valid){

            try{
                System.out.print("nombre d'heure (4 max): ");
                int hour = lireEntierAvecVerification(4, 1);
                int minute = 0;
                if(hour < 4){
                    System.out.print("nombre de minute : ");
                    minute = lireEntierAvecVerification(59, 0);
                }

                long millisToAdd = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute);

                timestamp = new Timestamp(now.getTime() + millisToAdd);
                valid = true;
            }catch (Exception e) {
                System.out.println("L'heure fournie est invalide. Veuillez réessayer.");
            }
        }

        return timestamp;
    }
}
