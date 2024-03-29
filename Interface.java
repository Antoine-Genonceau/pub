/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pub;

import java.util.ArrayList;
import java.util.Scanner; 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Toine
 * 
 * L'objet interface represente l'interaction avec l'utilisateur
 */
public class Interface {
    
    Charge charge = new Charge();
    Scanner keyboard = new Scanner(System.in);
    
    /**
     * Cette methode est le coeur de l'interface
     * 
     */
    
    public void processus() throws IOException, StopCommandeException{
        
        boolean power = true;
        
        notice();
        
        while(power){
          
            menuGeneral();
            
        }
        
        
    }
    
    /**
     * Affiche le mode d'emploi de l'interface
     * 
     * @throws IOException 
     */
    
    public void notice() throws IOException{
        
        System.out.println("Mode d'emploi :");
        System.out.println("Vous pouvez quitter le programme à tout moment en entrant 'quit'");
        System.out.println("Vous pouvez sauvegarder vos modifications lorsque vous quittez le programme, après avoir chargé une sauvegarde.");
        System.out.println("Pour naviguer dans les menus vous devez entrer une des propositions.");
        System.out.println("Exemple : Dans le menu suivant, si vous souhaitez sélectionner l'option 'serveur' tapez '1'");
        System.out.println("| serveur - 1 | client - 2 |");
        String enter = demandeChaine("Pour démarer pressez la touche entrée...");
        
    }
        
    /**
     * Chargement d'une sauvegarde
     * 
     * @param nom nom de la sauvegarde
     * @throws IOException 
     */
    
    public void chargerSauvegarde(String nom) throws IOException{
        
        ObjectInputStream ois;
        
        ois = new ObjectInputStream(
              new BufferedInputStream(
                new FileInputStream(
                  new File("./src./pub./sauvegardes/" + nom))));
            
      try {
          
          charge = (Charge)ois.readObject();
          
          System.out.println("La sauvegarde : " + nom + " a bien été chargée !");
        
       
        
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
	
      ois.close();
        	
     
        
        
    }
    
    /**
     * Creation d'une sauvegarde
     * 
     */
    
    public void creerSauvegarde() throws IOException{
        
        String questionSauvegarde = demandeON("Voulez vous sauvegarder les modifications ? ( O = oui / N = non)");
        
        if (questionSauvegarde.equals("O")){
        
            String nom = demandeNomFichier("Quel nom voulez vous donner a cette sauvegarde ? (tapez '" + charge.nom +"' pour ecraser la sauvegarde actuelle.)");
        
            ObjectOutputStream oos;
            try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream(
                        new File("./src./pub./sauvegardes/" + nom + ".txt"))));
        	
                oos.writeObject(new Charge(charge.bar, charge.fournisseur, charge.boissons, nom));
            
                oos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        
        }
        
    }
    
    /**
     * Menu général on demande à l'utilisateur ce qu'il souhaite faire
     * 
     */
    
    
    public void menuGeneral() throws IOException, StopCommandeException{  
        
        while(!charge.chargement){
        
            try{
            
            System.out.println("Bonjour, pour commencer veulliez choisir une sauvegarde à charger !");
            File file = new File("./src./pub./sauvegardes");
            File[] files = file.listFiles();
            
            for (int i = 0;i < files.length; i++){
                
                System.out.println("| " + files[i].getName() + " - " + i + " |");
                
            }
            
            int choix = scanEntierBorne(0, files.length - 1);    
            
            chargerSauvegarde(files[choix].getName());
            
        }           
                
        catch(BorneException e){
                
                menuGeneral();
                
                }
           
        }
        
        System.out.println("Que voullez vous faire ?");
        System.out.println("| Afficher données - 1 | Créer données - 2 | Effectuer une action - 3 |");
        
        try{

        switch(scanEntierBorne(1,3)) {
                
            case 1:
                afficherDonnee();
                break;
            case 2:
                creerDonnee();
                break;
            case 3:
                action();
                break;
            
            }
        
        
        }
        
        catch(BorneException e){
            
            menuGeneral();
        }
        
           
    }
    
    /**
     * Menu pour afficher des données
     * 
     */
    
    public void afficherDonnee() throws IOException{
        
        System.out.println("Quelle type de donnée voulez vous afficher ?");
        System.out.println("| Bar - 1 | Fournisseur - 2 | Serveurs - 3 | Clients - 4 | Barman - 5 | Patronne - 6 |");
        
        try{
            
        
        
        switch(scanEntierBorne(1, 6)) {
                
            case 1:
                System.out.println(charge.bar.toString());
                break;
            case 2:
                System.out.println(charge.fournisseur.toString());
                break;
            case 3:
                
                for (int i = 0; i < charge.bar.getServeurs().size(); i++){
                    
                    System.out.println(charge.bar.getServeurs().get(i));
                    
                }
                
                break;
            case 4:
                
                for (int i = 0; i < charge.bar.getClients().size(); i++){
                    
                    System.out.println(charge.bar.getClients().get(i));
                    
                }
                
                break;
            case 5:
                System.out.println(charge.bar.getBarman());
                break;
            case 6:
                System.out.println(charge.bar.getPatronne());
                break;
            
            
            }
        }
        
        catch(BorneException e){
            
            afficherDonnee();
            
        }
    }
    
    /**
     * Menu pour créer des données
     * 
     */
    
    public void creerDonnee() throws IOException{
        
        System.out.println("Quelle type de donnée voulez vous créer ?");
        System.out.println("| Serveur - 1 | Client - 2 |");
        
        try{
            
        switch(scanEntierBorne(1,2)) { 
                
            case 1:
                creationServeur();
                break;
            case 2:
                creationClient();
                break;
                        
            }
        
        }
        catch(BorneException e){
            
            creerDonnee();
        }
  
    }
    
    /**
     * Cette methode crée un serveur
     * 
     */
    
    public void creationServeur() throws IOException{
        
        System.out.println("Creation Serveur :");
        
        
        String surnom = demandeChaine("Surnom :");
       
        String prenom = demandeChaine("Prenom :");
        
        int porteMonnaie = demandeEntier("Porte monnaie :");
        
        String cri = demandeChaine("Cri :");
        
        System.out.println("Sexe : | Homme - 1 | Femme - 2 |");
        
        int signe = 0;
        int sexe = 0;
        
        boolean conforme1 = false;
        boolean conforme2 = false;
        boolean conforme3 = false;
        
        while(!conforme1){
        conforme1 = true;
        try{
        
        switch(scanEntierBorne(1,2)) { 
                
                    case 1:
                        
                        sexe = 1;
                        
                        while(!conforme2){
                        conforme2 = true;          
                        try{
                        System.out.println("Taille des biceps : | Gros biceps - 1 | Moyens biceps - 2 | Petits biceps - 3 |");
                        signe = scanEntierBorne(1,3);
                        }
                        catch(BorneException a){                            
                            conforme2 = false;
                        }
                        
                        
                        
                        }
                        break;
                    case 2:
                        
                        sexe = 2;
                        
                        while(!conforme3){
                        conforme3 = true;
                        
                        try{
                        System.out.println("Coefficient de seduction : | Haute seduction - 1 | Moyenne seduction - 2 | Basse seduction - 3 |");
                        signe = scanEntierBorne(1, 3);
                        }
                        catch(BorneException b){                            
                            conforme3 = false;
                        
                        }
                        
                        
                        }
                        break;
        }
        
        }
        catch(BorneException e){
            
            conforme1 = false;
            
        }
        
        }
        
        SigneServeur signeServeur = conversionListeStrVersSigneServeur(sexe, signe);
        
        Serveur serveur = new Serveur(surnom, prenom, porteMonnaie, cri, signeServeur);
        
        charge.bar.embauche(serveur);
        
        System.out.println("Vous avez créé le serveur " + serveur.toString());
        
    }
    
    /**
     * Cette methode crée un client
     * 
     */
    
    public void creationClient() throws IOException{
        
        System.out.println("Creation Client :");
        
        String surnom = demandeChaine("Surnom :");
   
        String prenom = demandeChaine("Prenom :");
        
        int porteMonnaie = demandeEntier("Porte monnaie :");
      
        String cri = demandeChaine("Cri :");
        
        Boisson boissonFavBis = new Boisson();
        Boisson boissonFav = new Boisson();
        boolean bfConforme = false;
        while(!bfConforme){
            bfConforme = true;
        System.out.println("Boisson Favorie :");
        for (int i = 0; i < charge.boissons.size(); i++){
            
            System.out.print(charge.boissons.get(i).getNom() + " - " +  i +" | ");
            
        }
        try{        
        boissonFav = charge.boissons.get(scanEntierBorne(0, charge.boissons.size() - 1));
        }
        catch(BorneException c){
            bfConforme = false;
        }
        
        }
        
        boolean bfbConforme = false;
        
        while(!bfbConforme){
        bfbConforme = true;
        try{
        System.out.println("Boisson de secours :");
        for (int i = 0; i < charge.boissons.size(); i++){
            
            System.out.print(charge.boissons.get(i).getNom() + " - " +  i +" | ");
            
        }        
        boissonFavBis = charge.boissons.get(scanEntierBorne(0, charge.boissons.size()-1));
        }
        catch(BorneException d){
            
            bfbConforme = false;
            
        }
        }
        
        
        int niveauAlcool = demandeEntier("Niveau alcool :");
        
                
        int signe = 0;
        int sexe = 0;
        
        boolean conforme1 = false;
        boolean conforme2 = false;
        boolean conforme3 = false;
        
        while(!conforme1){
        
            System.out.println("Sexe : | Homme - 1 | Femme - 2 |");
            
            conforme1 = true;
            
        try{
        
            
        switch(scanEntierBorne(1,2)) { 
                
                    case 1:
                        sexe = 1;
                        while(!conforme2){
                            conforme2 = true;
                        try{
                        System.out.println("Couleur du t-shirt : | Blanc - 1 | Bleu - 2 | Noir - 3 | Rouge - 4 | Vert - 5 |");
                        signe = scanEntierBorne(1,5);
                        }
                        catch(BorneException a){
                            
                            conforme2 = false;
                        }
                        
                        }
                        break;
                    case 2:
                        sexe = 2;
                        while(!conforme3){
                            conforme3 = true;
                        try{
                        System.out.println("Bijoux : | Bague - 1 | Boucle d'oreille - 2 | Bracellet - 3 | Collier - 4 | Montre - 5 |");
                        signe = scanEntierBorne(1,5);
                        }
                        catch(BorneException b){
                            
                            conforme3 = false;
                        }
                        
                        }
                        break;
        }
        
        }
        
        catch(BorneException e){
            
            conforme1 = false;
        }
        
        }
        SigneClient signeClient = conversionListeStrVersSigneClient(sexe, signe);
        
        Client client = new Client(surnom, prenom, porteMonnaie, cri, boissonFav, boissonFavBis, niveauAlcool, signeClient);
        
        charge.bar.entreeClient(client);
        
        System.out.println("Vous avez créé le client " + client.toString());
        
    }
    
    /**
     * Menu pour effectuer une action
     * 
     */
    
    
    public void action() throws IOException, StopCommandeException{
        
        System.out.println("Quel type d'action voulez vous effectuer ?");
        System.out.println("| Gestion Bar - 1 | Action Bar - 2 | Action Tournoi -3 |");
        
        try{
            
        
        switch(scanEntierBorne(1,3)) { 
                
            case 1:
                actionBar();
                break;
            case 2:
                actionHumain();
                break;
            case 3:
                actionTournoi();
                break;
                        
            }
        
        }
        catch(BorneException e){
            
            action();
            
        }
        
    }
    
    /**
     * Menu pour effectuer une action de gestion du bar
     * 
     */
    
    public void actionBar() throws IOException{
        
        System.out.println("Quel type d'action voulez vous effectuer ?");
        System.out.println("| Commander des boissons au fournisseur - 1 | Rappeller à l'ordre un client trop alcoolisé - 2 |");
        
        try{
            
        
        switch(scanEntierBorne(1,2)) { 
                
            case 1:
                commandeFournisseur();
                break;
            case 2:
                rappelOrdre();
                break;
                                    
            }
        }
        catch(BorneException e){
            
            actionBar();
            
        }
               
        
    }
    
    /**
     * Cette methode permet de passer une commande au fournisseur
     * 
     */
    
    public void commandeFournisseur() throws IOException{
        
        System.out.println("Commande :");
        
        Stock commande = new Stock();
        
        for (int i = 0; i < charge.boissons.size(); i++){
            
            StockBoisson stockBoisson = new StockBoisson(charge.boissons.get(i), demandeEntier("nombre de " + charge.boissons.get(i).getNom() + ":"));
            
            commande.getStock().add(stockBoisson);
            
        }
        
        charge.bar.getBarman().envoieCommandeFournisseur(commande, charge.fournisseur);
        
    }
    
    /**
     * Cette methode permet de rappeller a l'ordre un client
     * 
     */
    
    public void rappelOrdre() throws IOException{
        
        System.out.println("Quel type de rappel à l'ordre voulez vous effectuer ?");
        System.out.println("| Automatique - 1 | Manuel - 2 |");
        
        try{
            
        switch(scanEntierBorne(1,2)) { 
                
            case 1:
                automatique();
                break;
            case 2:
                manuel();
                break;
                                    
            }
        
        }
        catch(BorneException e){
            
            rappelOrdre();
            
        }
        
    }
    
    /**
     * Rappel à l'ordre automatique 
     * 
     */
    
    public void automatique(){
        
        if (!charge.bar.getPatronne().checkEtatClient()){
            
            System.out.println("Aucun client n'a bessoin d'être rappellé à l'ordre.");
            
        }
        
    }
    
    /**
     * Rappel à l'ordre manuel
     * 
     */
    
    public void manuel() throws IOException{
        
        System.out.println("Quel client voulez vous rappeller à l'ordre ?");
        
        for (int i = 0; i < charge.bar.getClients().size(); i++){
            
            System.out.print(charge.bar.getClients().get(i).getPrenom() + " - " + i + " | ");
            
        }
        try{
        charge.bar.getPatronne().rappelOrdre(charge.bar.getClients().get(scanEntierBorne(0, charge.bar.getClients().size() - 1)));
        }
        catch(BorneException e){
            
            manuel();
            
        }
        
    }
    
    /**
     * Menu pour effectuer une action avec un individu present dans le bar
     * 
     */
    
    public void actionHumain() throws IOException, StopCommandeException{
        
        Humain humain = choixHumain();
        
        System.out.println("Quel type d'action voulez vous effectuer ?");
        System.out.println("| Se présenter - 1 | Commander auprès du barman - 2 | Commander auprès d'un serveur - 3 | Commander une tournée générale - 4 |");
        
        try{
        switch(scanEntierBorne(1,4)) { 
                
            case 1:
                humain.presentation();
                break;
            case 2:
                commanderBarman(humain);
                break;
            case 3:
                commanderServeur(humain);
                break;
            case 4:
                humain.tourneeGenerale(charge.bar.getBarman());
                break;
            
                                    
            }
        
        }
        catch(BorneException e){
            
            actionHumain();
        }
    }
    
    /**
     * Methode permetant dans passer une commande au barman
     * @param humain individu passant la commande
     */
    
    public void commanderBarman(Humain humain) throws IOException, StopCommandeException{
        
        System.out.println("Quel genre de commande voulez vous passer ?");
        System.out.println("| Commander un verre pour soi - 1 | Commander pour soi et d'autres personnes - 2 | Commander pour d'autres personnes - 3 |");
        
        try{
        switch(scanEntierBorne(1,3)) { 
            
            case 1:
                commanderSeul(humain, charge.bar.getBarman());
                break;
            case 2:
                commanderAvec(humain, charge.bar.getBarman());
                break;
            case 3:
                commanderAutres(humain, charge.bar.getBarman());
                break;
                                    
            }
        }
        catch(BorneException e){
            
            commanderBarman(humain);
            
        }
    }
    
    /**
     * Methode permetant dans passer une commande à un serveur
     * 
     * @param humain individu passant la commande
     */
    
    public void commanderServeur(Humain humain) throws IOException, StopCommandeException{
        
        Serveur serveur = new Serveur();
        
        serveur = choixServeur();
        
        System.out.println("Quel genre de commande voulez vous passer ?");
        System.out.println("| Commander un verre pour soi - 1 | Commander pour soi et d'autres personnes - 2 | Commander pour d'autres personnes - 3 |");
        
        try{
        switch(scanEntierBorne(1,3)) { 
            
            case 1:
                commanderSeul(humain, serveur);
                break;
            case 2:
                commanderAvec(humain, serveur);
                break;
            case 3:
                commanderAutres(humain, serveur);
                break;
                                    
            }
        }
        
        catch(BorneException e){
            
            commanderServeur(humain);
        }
    }
    
    /**
     * methode permetant à un individu de passer une commande pour lui seul
     * 
     * @param humain individu passant la commande
     * @param barman barman auquel il s'adresse
     */
    
    public void commanderSeul(Humain humain, Barman barman) throws StopCommandeException{
        
        ArrayList<Humain> seul = new ArrayList<>();
        
        seul.add(humain);
        
        humain.consommer(seul, barman);
        
    }
    
    /**
     * methode permetant à un individu de passer une commande pour lui seul
     * 
     * @param humain individu passant la commande
     * @param serveur serveur auquel il s'adresse
     */
    
    public void commanderSeul(Humain humain, Serveur serveur) throws StopCommandeException{
        
        ArrayList<Humain> seul = new ArrayList<>();
        
        seul.add(humain);
        
        humain.consommer(seul, serveur);
        
    }
    
    /**
     * Methode permetant à un individu de passer une commande pour lui et d'autres personnes
     * 
     * @param humain individu passant la commande
     * @param barman barman auquel il s'adresse
     */
    
    public void commanderAvec(Humain humain, Barman barman) throws IOException, StopCommandeException{
        
        ArrayList<Humain> avec = new ArrayList<>();
        
        avec.add(humain);
        
        ajoutAutres(avec);
        
        humain.consommer(avec, barman);
        
    }
    
    /**
     * Methode permetant à un individu de passer une commande pour lui et d'autres personnes 
     * 
     * @param humain individu passant la commande
     * @param serveur serveur auquel il s'adresse
     */
    
    public void commanderAvec(Humain humain, Serveur serveur) throws IOException, StopCommandeException{
        
        ArrayList<Humain> avec = new ArrayList<>();
        
        avec.add(humain);
        
        ajoutAutres(avec);
        
        humain.consommer(avec, serveur);
        
    }
    
    /**
     * Methode permetant à un individu de passer une commande pour d'autres personnes 
     * 
     * @param humain individu passant la commande
     * @param barman barman auquel il s'adresse
     */
    
    public void commanderAutres(Humain humain, Barman barman) throws IOException, StopCommandeException{
        
        ArrayList<Humain> avec = new ArrayList<>();
        
        ajoutAutres(avec);
        
        humain.consommer(avec, barman);
        
    }
    
    /**
     * Methode permetant à un individu de passer une commande pour d'autres personnes 
     * 
     * @param humain individu passant la commande
     * @param serveur serveur auquel il s'adresse
     */
    
    public void commanderAutres(Humain humain, Serveur serveur) throws IOException, StopCommandeException{
        
        ArrayList<Humain> avec = new ArrayList<>();
        
        ajoutAutres(avec);
        
        humain.consommer(avec, serveur);
        
    }
    
    /**
     * Methode permettant d'ajouter des individu dans une commande
     * 
     * @param liste liste d'individus
     */
    
    public void ajoutAutres(ArrayList<Humain> liste) throws IOException{
        
        String reponse = "O";
        
        while(!(reponse.equals("N"))){
            
            liste.add(choixHumainConsomateur());
            
            reponse = demandeON("Voulez vous offrir un verre à quelqu'un d'autre ? (O = oui / N = non)");
            
        }
        
    }
    
    /**
     * Methode permettant de choisir le serveur auquel on souhaite passer la commande
     * 
     * @return retourne le serveur choisi
     */
    
    public Serveur choixServeur() throws IOException{
        
        System.out.println("Auprès de quel serveur souhaitez vous commander ?");
        
        Serveur serveur = new Serveur();
        
        for (int i = 0; i < charge.bar.getServeurs().size(); i++){
            
            System.out.print(charge.bar.getServeurs().get(i).getPrenom() + " - " + i + " | ");
            
        }
        try{
        serveur = charge.bar.getServeurs().get(scanEntierBorne(0, charge.bar.getServeurs().size() - 1));
        }
        catch(BorneException e){
            
            choixServeur();
            
        }
        
        return serveur;
        
    }
    
    /**
     * Methode permetant de choisir l'individu avec lequel on veut effectuer une action
     * 
     * @return individu choisi
     */
    
    public Humain choixHumain() throws IOException{
        
        Humain humain = new Humain();
        
        System .out.println("Avec qui voulez vous effectuer une action ?");
        
        int num = 0;
        
        int lastClient = 0;
        
        int lastServeur = 0;
        
        System.out.println("Clients :");
                
        for (int i = 0; i < charge.bar.getClients().size(); i++){
            
            System.out.print(charge.bar.getClients().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        lastClient = num - 1;
        
        System.out.println("");
        
        System.out.println("Serveurs :");
        
        for (int i = 0; i < charge.bar.getServeurs().size(); i++){
            
            System.out.print(charge.bar.getServeurs().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        lastServeur = num - 1;
        
        System.out.println("");
        
        System.out.println("Barman :");
        
        System.out.print(charge.bar.getBarman().getPrenom() + " - " + num + " | ");
        
        System.out.println("");
        
        System.out.println("Patronne :");
        
        int numP = num + 1;
        
        System.out.print(charge.bar.getPatronne().getPrenom() + " - " + numP + " | ");
        
        System.out.println("");
        
        try{
        int choix = scanEntierBorne(0, num + 1);
        
        if (choix <= lastClient){
            
            humain = charge.bar.getClients().get(choix);
            
        }
        
        if (choix <= lastServeur && choix > lastClient){
            
            humain = charge.bar.getServeurs().get(choix - lastClient - 1);
            
        }
        
        if (choix == num){
            
            humain = charge.bar.getBarman();
            
        }
        
        if (choix == num + 1){
            
            humain = charge.bar.getPatronne();
            
        }
        
        }
        
        catch(BorneException e){
            
            choixHumain();
            
        }
                
        return humain;        
        
    }
    
    /**
     * Methode permetant de choisir un individu auquel on souhaite offrir un verre
     * 
     * @return  individu choisi
     */
    
     public Humain choixHumainConsomateur() throws IOException{
        
        Humain humain = new Humain();
        
        System .out.println("A qui voulez vous offir un verre ?");
        
        int num = 0;
        
        int lastClient = 0;
        
        int lastServeur = 0;
        
        System.out.println("Clients :");
                
        for (int i = 0; i < charge.bar.getClients().size(); i++){
            
            System.out.print(charge.bar.getClients().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        lastClient = num - 1;
        
        System.out.println("");
        
        System.out.println("Serveurs :");
        
        for (int i = 0; i < charge.bar.getServeurs().size(); i++){
            
            System.out.print(charge.bar.getServeurs().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        lastServeur = num - 1;
        
        System.out.println("");
        
        System.out.println("Barman :");
        
        System.out.print(charge.bar.getBarman().getPrenom() + " - " + num + " | ");
        
        System.out.println("");
        
        System.out.println("Patronne :");
        
        int numP = num + 1;
        
        System.out.print(charge.bar.getPatronne().getPrenom() + " - " + num + " | ");
        
        System.out.println("");
        
        try{
        
        int choix = scanEntierBorne(0, num + 1);
        
        if (choix <= lastClient){
            
            humain = charge.bar.getClients().get(choix);
            
        }
        
        if (choix <= lastServeur && choix > lastClient){
            
            humain = charge.bar.getServeurs().get(choix - lastClient - 1);
            
        }
        
        if (choix == num){
            
            humain = charge.bar.getBarman();
            
        }
        
        if (choix == num + 1){
            
            humain = charge.bar.getPatronne();
            
        }
        }
        catch(BorneException e){
            
            choixHumainConsomateur();
            
        }
        return humain;        
        
    }
     
     /**
      * Methode permetant de choisir un individu pour jouer
      * 
      * @return retourne l'individu choisit
      */
     
     public Humain choixJoueur() throws IOException{
        
        Humain humain = new Humain();
        
        int num = 0;
        
        int lastClient = 0;
        
        System.out.println("Clients :");
                
        for (int i = 0; i < charge.bar.getClients().size(); i++){
            
            System.out.print(charge.bar.getClients().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        lastClient = num - 1;
        
        System.out.println("");
        
        System.out.println("Serveurs :");
        
        for (int i = 0; i < charge.bar.getServeurs().size(); i++){
            
            System.out.print(charge.bar.getServeurs().get(i).getPrenom() + " - " + num + " | ");
            
            num++;
            
        }
        
        System.out.println("");
        
        try{
        
        int choix = scanEntierBorne(0, num - 1);
        
        if (choix <= lastClient){
            
            humain = charge.bar.getClients().get(choix);
            
        }
        
        else{
            
            humain = charge.bar.getServeurs().get(choix - lastClient - 1);
            
        }
        
        }
        catch(BorneException e){
            
            choixJoueur();
            
        }
                
        return humain;        
        
    }
     
    /**
     * Menu des actions de tournoi
     * 
     */
    
    public void actionTournoi() throws IOException, StopCommandeException{
        
        System.out.println("Quel type d'action voulez vous effectuer ?");
        System.out.println("| Gerer un tournoi - 1 | Creer un tournoi - 2 |");
        
        
        try{
        switch(scanEntierBorne(1,2)) { 
                
                    case 1:
                        choixTournoi();
                        break;
                    case 2:
                        creerTournoi();
                        break;
                                         
            }
        
        }
        
        catch(BorneException e){
            
            actionTournoi();
            
        }
        
    }
    
    /**
     * Methode permettant de choisir un tournoi avec lequel on veut effectuer une action
     * 
     */
    
    public void choixTournoi() throws IOException, StopCommandeException{
        
        if (charge.bar.getBarman().getTournois().size() == 0){
            
            System.out.println("Il n'existe pour l'instant aucun tournoi !");
            
        }
        
        else{
            
            System.out.println("Quel tournoi souhaitez vous gerer ?");
            
            for (int i = 0; i < charge.bar.getBarman().getTournois().size(); i++){
                
                System.out.println( charge.bar.getBarman().getTournois().get(i).getNom() + " - " + i + " | ");    
                
            }
            
            try{
            gererTournoi(charge.bar.getBarman().getTournois().get(scanEntierBorne(0, charge.bar.getBarman().getTournois().size() - 1))); 
            }
            catch(BorneException e){
                
                choixTournoi();
                
            }
            
        }
        
        
    }
    
    /**
     * Menu des actions possibles pour un tournoi existant
     * 
     * @param tournoi 
     */
    
    public void gererTournoi(Tournoi tournoi) throws IOException, StopCommandeException{
        
        
        System.out.println("Quel type d'action voulez vous effectuer pour le tournoi '" + tournoi.getNom() + "' ?");
        System.out.println("| Inscrire une Equipe - 1 | Clôturer les inscriptions - 2 | Lancer le tournoi - 3 | Gerer les matchs - 4 | Afficher synthese - 5 |");
        
        try{
        switch(scanEntierBorne(1,5)) { 
                
                    case 1:
                        inscrireEquipe(tournoi);
                        break;
                    case 2:
                        cloreInscriptionsTournoi(tournoi);
                        break;
                    case 3:
                        lancerTournoi(tournoi);
                        break;
                    case 4:
                        gererMatchs(tournoi);
                        break;                        
                    case 5:
                        tournoi.afficheSynthese();
                        break;
                                         
            }
        
        }
        catch(BorneException e){
            
            gererTournoi(tournoi);
            
        }
        
        if (demandeON("Voulez vous effectuer une nouvelle action pour ce tournoi ? ( 'O' = oui / 'N' = non)").equals("O")){
            
            
            gererTournoi(tournoi);
            
            
        }
        
    }
    
    public void gererMatchs(Tournoi tournoi) throws IOException, StopCommandeException{
        
        System.out.println("Quel type d'action voulez vous effectuer pour les matchs du tournoi '" + tournoi.getNom() + "' ?");
        System.out.println("| Lancer un match - 1 | Afficher les résultats et matchs déjà passés - 2 |");
        
        try{
        switch(scanEntierBorne(1,2)) { 
                
                    case 1:
                        lancerMatch(tournoi);
                        break;
                    case 2:
                        afficherMatchs(tournoi);
                        break;
                                                             
            }
        
        }
        catch(BorneException e){
            
            gererTournoi(tournoi);
            
        }
        
        
    }
    
    public void lancerMatch(Tournoi tournoi) throws IOException, StopCommandeException{
        
        if (tournoi.getCalendrier().getCalendrier().size() != 0 ){
        
            System.out.println("Quel type d'action voulez vous effectuer pour les matchs du tournoi '" + tournoi.getNom() + "' ?");
        
            for (int i = 0; i < tournoi.getCalendrier().getCalendrier().size(); i++){
            
                System.out.println("| " + tournoi.getCalendrier().getCalendrier().get(i)[0] + " VS " + tournoi.getCalendrier().getCalendrier().get(i)[1] + " - " + i + " |");
            
            }
        
            try{
          
        
                tournoi.deroulementMatch(tournoi.getCalendrier().getCalendrier().get(scanEntierBorne(0, tournoi.getCalendrier().getCalendrier().size())));
                
         
            }
            catch(BorneException e){
            
                gererTournoi(tournoi);
            
            }
        
        }
        
        else{
            
            System.out.println("Ce tournoi est terminé !");
            
        }
    }
    
    public void afficherMatchs(Tournoi tournoi){
        
        System.out.println("Matchs déjà joués :");
        
        for (int i = 0; i < tournoi.getMatchPasses().getCalendrier().size(); i++){
            
            System.out.println("| " + tournoi.getMatchPasses().getCalendrier().get(i)[0] + " VS " + tournoi.getMatchPasses().getCalendrier().get(i)[1] + " |");
            
        }
        
        System.out.println("Résultats :");
        
        tournoi.getTableau().afficheTabClassement();
        
    }
    
    /**
     * Methode permettant d'inscrire une equipe a un tournoi
     * 
     * @param tournoi 
     */
    public void inscrireEquipe(Tournoi tournoi) throws IOException{
        
        Equipe equipe = new Equipe();
        
        equipe = creationEquipe();
        
        equipe.toString();
        
        equipe.inscription(tournoi, charge.bar.getBarman());
        
    }
    
    
    /**
     * Methode permetant de créer une equipe
     * 
     * @return 
     */
    
    public Equipe creationEquipe() throws IOException{
        
        Joueur joueur1 = new Joueur();
        
        joueur1 = creationJoueur1();
        
        Joueur joueur2 = new Joueur();
        
        joueur2 = creationJoueur2();
        
        String nom = creationNomEquipe();
        
        return new Equipe(joueur1, joueur2, nom);
        
        
    }
    
    public NiveauFlechette choixNiveau() throws IOException{
        
        NiveauFlechette niveau = NiveauFlechette.expert;
        
        try{
        
        System.out.println("Choix du niveau :");
        System.out.println("| Debutant - 1 | Confirme - 2 | Expert - 3 | Manuel (Controlé par l'utilisateur) - 4 |");
        
        int choix = scanEntierBorne(1,4);
        
        switch(choix){
            
            case 1:
                niveau = NiveauFlechette.debutant;
                break;
            case 2:
                niveau = NiveauFlechette.confirme;
                break;
            case 3:
                niveau = NiveauFlechette.expert;
                break;
            case 4:
                niveau = NiveauFlechette.manuel;
                break;               
            
        
    }
        }
        catch(BorneException e){
            
            niveau = choixNiveau();
            
        }
        
        return niveau;
    }
    
    /**
     * Methode permetant de creer un joueur
     * 
     * @return retoure le joueur créé
     */
    
    public Joueur creationJoueur1() throws IOException{
        
        Humain humain = new Humain();
        
        System.out.println("Qui choisissez vous comme joueur numero 1 pour cette equipe ?");
        
        humain = choixJoueur();
        
        NiveauFlechette niveau = choixNiveau();
        
        return new Joueur(humain, niveau);        
                
    }
    
    /**
     * Methode permetant de creer un joueur
     * 
     * @return retoure le joueur créé
     */
    
    public Joueur creationJoueur2() throws IOException{
       
        Humain humain = new Humain();
        
        System.out.println("Qui choisissez vous comme joueur numero 2 pour cette equipe ?");
        
        humain = choixJoueur();
        
        NiveauFlechette niveau = choixNiveau();
        
        return new Joueur(humain, niveau);        
               
        
    }
    
    /**
     * Methode permettant de choisir un nom d'équipe
     * 
     * @return retourne le nom choisi
     */
    
    public String creationNomEquipe() throws IOException{
        
        return demandeChaine10("Quel est le nom de cette equipe ? (maximum 9 caracteres)");
        
    }
    
    /**
     * Methode permetant de cloturer les inscription d'un tournoi
     * 
     * @param tournoi 
     */
    
    public void cloreInscriptionsTournoi(Tournoi tournoi){
        
        
        charge.bar.getBarman().fermetureInscription(tournoi);
        
    }
    
    
    public void lancerTournoi(Tournoi tournoi) throws IOException, StopCommandeException{
    
    System.out.println("Choix du mode pour le tournoi '" + tournoi.getNom() + "' :");
        System.out.println("| Automatique - 1 | Manuel - 2 |");
        
        try{
        switch(scanEntierBorne(1,2)) { 
                
                    case 1:
                        lancerTournoiAuto(tournoi);
                        break;
                    case 2:
                        lancerTournoiManuel(tournoi);
                        break;
                    
                                         
            }
        
        }
        catch(BorneException e){
            
            lancerTournoi(tournoi);
            
        }
        
    }
    
    
    /**
     * Methode permettant de lancer les matchs d'un tournoi
     * 
     * @param tournoi 
     */
    
    public void lancerTournoiAuto(Tournoi tournoi) throws StopCommandeException, BorneException{
        
        charge.bar.getBarman().deroulementTournoiAuto(tournoi);
        
    }
    
    
    public void lancerTournoiManuel(Tournoi tournoi){
        
        charge.bar.getBarman().deroulementTournoiManuel(tournoi);
        
    }
    
    /**
     * Methode permetant de créer un tournoi
     * 
     */
    
    public void creerTournoi() throws IOException{
        
        String nom = demandeChaine("Quel nom voulez vous choisir ?");
        
        int prix = demandeEntier("Quel prix voulez vous choisir pour l'inscription ?");                
                
        charge.bar.getPatronne().creationTournoi(nom, prix);
        
    }
    
    /**
     * Methode permetant de choisir le signe distinctif d'un server
     * 
     * @param a variable correspondant au sexe
     * @param b variable correspondant au signe
     * @return retourne le signe distinctif
     */
    
    public SigneServeur conversionListeStrVersSigneServeur(int a, int b){
        
        SigneServeur signe = SigneServeur.grosBiceps;;
        
        switch(a) { 
                
            case 1:
                switch(b) { 
                
                    case 1:
                        signe = SigneServeur.grosBiceps;
                        break;
                    case 2:
                        signe = SigneServeur.normalBiceps;
                        break;
                    case 3:
                        signe = SigneServeur.petitBiceps;
                        break;
                        
            }
                break;
            case 2:
                switch(b) { 
                
                    case 1:
                        signe = SigneServeur.hauteSeduction;
                        break;
                    case 2:
                        signe = SigneServeur.moyenneSeduction;
                        break;
                    case 3:
                        signe = SigneServeur.basseSeduction;
                        break;
                        
            }
                break;
                        
            }
        
        return signe;
        
    }
    
    /**
     * Methode permetant de choisir le signe distinctif d'un client
     * 
     * @param a variable correspondant au sexe
     * @param b variable correspondant au signe
     * @return retourne le signe distinctif
     */
    
    public SigneClient conversionListeStrVersSigneClient(int a, int b){
        
        SigneClient signe = SigneClient.bague;
        
        switch(a) { 
                
            case 1:
                switch(b) { 
                
                    case 1:
                        signe = SigneClient.tShirtBlanc;
                        break;
                    case 2:
                        signe = SigneClient.tShirtBleu;
                        break;
                    case 3:
                        signe = SigneClient.tShirtNoir;
                        break;
                    case 4:
                        signe = SigneClient.tShirtRouge;
                        break;
                    case 5:
                        signe = SigneClient.tShirtVert;
                        break;
                        
            }
                break;
            case 2:
                switch(b) { 
                
                    case 1:
                        signe = SigneClient.bague;
                        break;
                    case 2:
                        signe = SigneClient.boucleOreille;
                        break;
                    case 3:
                        signe = SigneClient.bracellet;
                        break;
                    case 4:
                        signe = SigneClient.collier;
                        break;
                    case 5:
                        signe = SigneClient.montre;
                        break;
                        
            }
                break;
                        
            }
        
        return signe;
        
    }
    
    /**
     * Selection d'une boisson
     * 
     * @param entree nom de la boisson
     * @return 
     */
    
    public Boisson conversionListeStrVersBoisson(String entree){
        
        Boisson boisson = new Boisson();
        
        for(int i = 0; i < charge.boissons.size(); i++){
            
            if (charge.boissons.get(i).getNom().equals(entree)){
                
                boisson = charge.boissons.get(i);
                
            }
            
        }        
        
        return boisson;
    }
   
    
    /**
     * Methode permettant de traiter les entiers entrés par l'utilisateur
     * 
     * @param entree entier entré par l'utilisateur
     * @return entier traité
     */
    
    
    public int conversionStrVersInt(String entree){
        
        int puissance = entree.length();
        int nombre = 0;
        double result = 0;

        for(int i = 0; i < puissance; i++){
            
            char chiffre = entree.charAt(i);

            switch(chiffre) {
                case '0':
                    nombre = 0;
                    break;
                case '1':
                    nombre = 1;
                    break;
                case '2':
                    nombre = 2;
                    break;
                case '3':
                    nombre = 3;
                    break;
                case '4':
                    nombre = 4;
                    break;
                case '5':
                    nombre = 5;
                    break;
                case '6':
                    nombre = 6;
                    break;
                case '7':
                    nombre = 7;
                    break;
                case '8':
                    nombre = 8;
                    break;
                case '9':
                    nombre = 9;
                    break;
                    
                    

        }
            
            result = result + nombre*Math.pow((double) 10, (double) (puissance - i - 1));
            
        }
      
    if ((result == 0) && !(entree.equals("0"))){
        
        result = -1;
        
    }
        
    return (int) result;

}
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////Getion des Exceptions//////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public int scanEntierBorne(int min, int max) throws BorneException, IOException{
        
        int entier = 0;
        
        String chaine = keyboard.nextLine();
        
        
        try{
            quitTest(chaine);
        }
        catch(QuitException q){
            
        }
        
        entier = conversionStrVersInt(chaine);
        
        if (entier < min || entier > max){
            
            throw new BorneException();
            
        }
        
        return entier;
    }
    
    public int scanEntier() throws EntierException, IOException{
        
        int entier = 0;
        
        String chaine = keyboard.nextLine();
        
        try{
            quitTest(chaine);
        }
        catch(QuitException q){
            
        }
        boolean entierTest = true;
        
        for (int i = 0; i < chaine.length(); i++){
            
            if (chaine.charAt(i) != '0' && chaine.charAt(i) != '1' && chaine.charAt(i) != '2' && chaine.charAt(i) != '3' && chaine.charAt(i) != '4' && chaine.charAt(i) != '5' && chaine.charAt(i) != '6' && chaine.charAt(i) != '7' && chaine.charAt(i) != '8' && chaine.charAt(i) != '9'){
                
                entierTest = false;
            }
        }
        
        if (!entierTest){
            
            throw new EntierException();
            
        }
        
        entier = conversionStrVersInt(chaine);
        
        return entier;
    }
    
    public int demandeEntier(String question) throws IOException{
        
        boolean done = false;
        
        int entier = 0;
                
        while(!done){
        
            done = true;
            
            System.out.println(question);
                            
            try{
            
                entier = scanEntier();
            
            }
            catch(EntierException e){
            
                done = false;
            
            }
        
        }
        
        return entier;
    }
    
    public String scanChaine10() throws Chaine10Exception, IOException{
        
        String chaine = keyboard.nextLine();
        
        
        try{
            quitTest(chaine);
        }
        catch(QuitException q){
            
        }
        
        if(chaine.length() > 10){
            
            throw new Chaine10Exception();
            
        }
        
        return chaine;
    }
    
    public String demandeChaine10(String question) throws IOException{
        
        String chaine10 = "";
        
        boolean done = false;
        
        while(!done){
            
            done = true;
        
            try{
                
                System.out.println(question);
            
                chaine10 = scanChaine10();
            
            }
            catch(Chaine10Exception e){
            
                done = false;
            
            }
        
        }
        
        return chaine10;
    }
    
    public void quitTest(String chaine) throws QuitException, IOException{
        
        if (chaine.equals("quit")){
            
            throw new QuitException(this);
            
        }
        
    }
    
    public String scanON() throws ONException, IOException{
        
        String chaine = keyboard.nextLine();
        
        try{
            
            quitTest(chaine);
            
        }
        catch(QuitException q){
            
        }
        boolean conforme = false;
        
        if (chaine.equals("O") || chaine.equals("N")){
            
            conforme = true;
        }
        
        if(!conforme){
            
            throw new ONException();
            
        }
        
        return chaine;
    }
    
    public String demandeON(String question) throws IOException{
        
        String chaineON = "";
        
        boolean done = false;
        
        while(!done){
            
            done = true;
        
            try{           
                
                System.out.println(question);
            
                chaineON = scanON();
            
            }
            catch(ONException e){
            
                done = false;
            
            }
        
        }
        
        return chaineON;
    }
    
    public String scanChaine() throws IOException{
        
        String chaine = keyboard.nextLine();
        
        try{
            
            quitTest(chaine);
            
        }
        catch(QuitException q){
            
        }
        
        return chaine;
    }
    
    public String scanNomFichier() throws IOException, NomFichierException{
        
        String chaine = keyboard.nextLine();
        
        try{
            
            quitTest(chaine);
            
        }
        catch(QuitException q){
            
        }
        
        boolean conforme = true;
        
        if (chaine.length() > 200){
            
            conforme = false;
        }
        
        for (int i = 0; i < chaine.length(); i++){
            
            if (chaine.charAt(i) == '<' || chaine.charAt(i) == '>' || chaine.charAt(i) == ':' || chaine.charAt(i) == '/' || chaine.charAt(i) == '\\' || chaine.charAt(i) == '"' || chaine.charAt(i) == '|' || chaine.charAt(i) == '?' || chaine.charAt(i) == '!' || chaine.charAt(i) == '*'){
                
                conforme = false;                
                
            }
            
        }
        
        if (!conforme){
            
            throw new NomFichierException();
        }
        
        return chaine;
    }
    
    public String demandeNomFichier(String question) throws IOException{
                        
        String chaineNomFichier = "";
        
        boolean done = false;
        
        while(!done){
            
            done = true;
        
            try{                
            
                System.out.println(question);
                
                chaineNomFichier = scanNomFichier();
            
            }
            catch(NomFichierException e){
            
                done = false;
            
            }
        
        }
        
        return chaineNomFichier;
    }
    
    public String demandeChaine(String demande) throws IOException{
        
        System.out.println(demande);
        
        String reponse = scanChaine();
        
        return reponse;
    }
    
        
}
