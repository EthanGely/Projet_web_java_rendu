package fr.iut2.project1.controleur;

import fr.iut2.project1.Controle.Controle;
import fr.iut2.project1.Controle.ControleDAO;
import fr.iut2.project1.Etudiant.Etudiant;
import fr.iut2.project1.Etudiant.EtudiantDAO;
import fr.iut2.project1.utility.GestionFactory;
import fr.iut2.project1.Groupe.Groupe;
import fr.iut2.project1.Groupe.GroupeDAO;
import fr.iut2.project1.Note.Note;
import fr.iut2.project1.Note.NoteDAO;
import fr.iut2.project1.Prof.Prof;
import fr.iut2.project1.Prof.ProfDAO;
import fr.iut2.project1.utility.Utility;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Controleur extends HttpServlet {

    // URL

    private Map<String, String> urls = new HashMap<>();

    private String lastSearch = "";

    private Prof profLogin = null;
    private Etudiant etuLogin = null;

    //L'ensemble des actions que seuls les profs peuvent utiliser
    private Set<String> actionsProfs = new HashSet<>();


    private List<Etudiant> listeEtu = new ArrayList<>();

    private static final String FILTRE_KEY = "FILTRE_KEY";
    private static final String GROUPE_KEY = "GROUPE_KEY";

    private boolean fMoyenne = false, fAbs = false, fGroupe = false;
    
    private Utility utility = new Utility();


    public void init() throws ServletException {
        //Certaines pages / actions sont réservées aux profs. On les ajoute à la liste
         actionsProfs = utility.addProfActions();

        //Initialisation des URLs
         urls = addUrls();


        // Création de la factory permettant la création d'EntityManager
        // (gestion des transactions)
        GestionFactory.open();

        ///// INITIALISATION DE LA BD
        // Normalement l'initialisation se fait directement dans la base de données
        if ((GroupeDAO.getAll().size() == 0) && (EtudiantDAO.getAll().size() == 0) && (ProfDAO.getAll().size() == 0)) {

            // Creation des groupes
            Groupe BIGDATA = GroupeDAO.create("BIG-DATA");
            Groupe SIMO = GroupeDAO.create("SIMO");
            Groupe AW = GroupeDAO.create("AW");
            Groupe ASSR = GroupeDAO.create("ASSR");


            ProfDAO.create("Admin", "UnNomRandom", "admin@iut.fr", "admin123", true, null);
            ProfDAO.create("Francis", "Brunet-Manquat", "francis.brunet-manquat@univ-grenoble-alpes.fr", "fb123", false, SIMO.getId());

            // Creation des étudiants
            Random rand = new Random();

            EtudiantDAO.addAbsences(EtudiantDAO.create("Mikâil", "Killic", "mikail.killic@etu-univ-grenoble-alpes.fr", "mk123", SIMO), rand.nextInt(5) + 1);
            EtudiantDAO.create("Ethan", "Gely", "ethan.gely@etu-univ-grenoble.fr", "eg123", SIMO);
            EtudiantDAO.create("Françoise", "Coat", "francoise.coat@iut.fr", "fc123", BIGDATA);
            EtudiantDAO.create("Laurent", "Bonnaud", "laurent.bonnaud@iut.fr", "lb123", ASSR);
            EtudiantDAO.create("Sébastien", "Bourdon", "sebastien.bourdon@iut.fr", "sb123", AW);
            EtudiantDAO.create("Mathieu", "Gatumel", "mathieu.gatumel@iut.fr", "mg123", SIMO);
        }
    }

    /**
     * Ajoute les urls dans le Map
     * @return
     */
    private Map<String, String> addUrls() {

        Map<String, String> urls = new HashMap<>();

        //Initialisation des URLs
        urls.put("urlListeEtudiants", getInitParameter("urlListeEtudiants"));
        urls.put("urlFicheEtudiant", getInitParameter("urlFicheEtudiant"));
        urls.put("urlEditionEtudiant", getInitParameter("urlEditionEtudiant"));
        urls.put("urlCreerEtu", getInitParameter("urlCreerEtu"));
        urls.put("urlAddNotes", getInitParameter("urlAddNotes"));
        urls.put("urlLogin", getInitParameter("urlLogin"));
        urls.put("urlProfil", getInitParameter("urlProfil"));
        urls.put("urlRemerciements", getInitParameter("urlRemerciements"));
        urls.put("urlListeControles", getInitParameter("urlListeControles"));
        urls.put("urlDetailControle", getInitParameter("urlDetailControle"));
        urls.put("urlCreerProf", getInitParameter("urlCreerProf"));

        return urls;
    }


    /**
     *
     * @param request
     */
    private void addAllGroupes(HttpServletRequest request) {
        Set<Groupe> sortedGroups = new TreeSet<>(Comparator.comparing(Groupe::getNom));
        sortedGroups.addAll(GroupeDAO.getAll());
        request.setAttribute("allGroupes", sortedGroups);
    }

    @Override
    public void destroy() {
        super.destroy();

        // Fermeture de la factory
        GestionFactory.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // et non, c'est un get !
        doGet(request, response);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //Si un utilisateur est connecté, à chaque changement de page, l'étudiant ou le prof connecté est actualisé avec les données de la BDD
        if (etuLogin != null && profLogin == null) {
            etuLogin = EtudiantDAO.getEtudiantByID(etuLogin.getId());
        } else if (etuLogin == null && profLogin != null) {
            profLogin = ProfDAO.getUserByID(profLogin.getId());
        }

        //L'action demandée par une JSP
        String action = request.getPathInfo();
        //Log
        getServletContext().log("Action demandée : " + action);

        //Si pas d'action (ou demande de déconnexion), on déconnecte l'utilisateur
        if (action == null || action.equals("/Disconnect")) {
            //Déconnecte l'utilisateur actuel
            doDisconnect(request, response);

            // Toutes les pages (sauf Login et la déconnexion) nécessitent d'être connecté.
        } else if (etuLogin != null || profLogin != null) {
            //Certaines pages sont réservée uniquement aux profs
            if (actionsProfs.contains(action) && profLogin != null && etuLogin == null) {
                if (action.equals("/listeEtudiants")) {
                    //Affichage de la liste des étudiants (filtré si pas admin)
                    doListe(request, response);

                } else if (action.equals("/editionetudiant")) {
                    //Affichage de la page de modification d'un étudiant
                    doEditionEtudiant(request, response);

                } else if (action.equals("/modifetudiant")) {
                    //Appliquer (ou non) les modifications apportées à un étudiant
                    doModifEtudiant(request, response);

                } else if (action.equals("/SupprimerEtu")) {
                    //Supprime un étudiant
                    doDeleteEtu(request, response);

                }  else if (action.equals("/CreerEtu")) {
                    //Affiche la page de création d'un étudiant
                    doCreerEtu(request, response);

                } else if (action.equals("/CreationEtudiant")) {
                    //Crée l'étudiant
                    doCreationEtudiant(request, response);

                } else if (action.equals("/listeControles")) {
                    //Affiche la liste des controles du prof connecté (ou de tous les profs si c'est un admin qui est connecté)
                    doListeControles(request, response);

                } else if (action.equals("/DetailControle")) {
                    //Affiche les détails d'un controle
                    doDetailControle(request, response);

                } else if (action.equals("/addNotes")) {
                    //Affiche la page pour ajouter une liste de notes
                    doListeNotes(request, response);

                } else if (action.equals("/validerNotes")) {
                    //Applique la liste de notes à chaque élève
                    doAppliquerNotes(request, response);

                } else if (action.equals("/creerProf")) {
                    //Applique la liste de notes à chaque élève
                    doCreerProf(request, response);

                } else if (action.equals("/creationProf")) {
                    //Applique la liste de notes à chaque élève
                    doCreationProf(request, response);

                } else {
                    doDisconnect(request, response);
                }
            } else {
                //Les autres actions sont disponibles pour les profs ET les étudiants
                if (action.equals("/FicheEtudiant")) {
                    //Fiche de détails d'un étudiant
                    doFicheEtudiant(request, response);

                } else if (action.equals("/RechercheEtudiant")) {
                    //Effectue une recherche sur la liste des étudiants, puis renvoie sur la même page
                    doRechercheEtudiant(request, response);

                } else if (action.equals("/profilUser")) {
                    //Affiche le profil de l'utilisateur courant
                    doProfil(request, response);

                } else if (action.equals("/modifProfil")) {
                    //Modifie le profil de l'utilisateur courant
                    doModifProfil(request, response);

                } else if (action.equals("/Remerciements")) {
                    //Affiche la page de remerciements
                    doRemerciements(request, response);

                } else {
                    //Déconnecte l'utilisateur actuel
                    doDisconnect(request, response);
                }
            }

        } else {
            //Les actions qui ne nécéssitent pas d'être connecté
            if (action.equals("/verifLogin")) {
                //vérification du login
                doVerifLogin(request, response);

            } else {
                //Déconnexion
                doDisconnect(request, response);
            }
        }
    }

    /**
     * Affiche les détails d'un controle grâce à son "id".
     * Retourne un seul "controle"
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDetailControle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controle controle = ControleDAO.getByID(utility.verifParameterLong(request, "id"));
        request.setAttribute("controle", controle);
        loadJSP(urls.get("urlDetailControle"), request, response);
    }

    /**
     * Affiche la liste des controles liés au prof connecté.
     * Retourne la List des "controles"
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doListeControles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Controle> controles = ControleDAO.getByProf(profLogin);
        request.setAttribute("controles", controles);
        loadJSP(urls.get("urlListeControles"), request, response);
    }

    /**
     * Affiche la page des remerciements.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doRemerciements(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadJSP(urls.get("urlRemerciements"), request, response);
    }

    /**
     * Vérifie et applique si besoin les modifications d'un profil.
     * Utilise un "idUser", un "typeUser", un "prenom", un "nom", un "mail", un "oldPass".
     * Utilise aussi en option un "newPass" et un "passVerif".
     * Retourne un code d'erreur, sur la page de modification du profil.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doModifProfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Long idUser = utility.verifParameterLong(request, "idUser");
        int typeUser = utility.verifParameterInt(request, "typeUser");

        String prenom = utility.verifParameterString(request, "prenom");
        String nom = utility.verifParameterString(request, "nom");
        String mail = utility.verifParameterString(request, "mail");
        String oldPass = utility.verifParameterString(request, "oldPass");
        String newPass = utility.verifParameterString(request, "newPass");
        String passVerif = utility.verifParameterString(request, "passVerif");

        int codeErreur = 0;
        //1 = old mdp différent
        //2 = new mdp différent
        //3 = cant find user
        //4 = empty field


        if (!prenom.equals("") && !nom.equals("") && !mail.equals("") && !oldPass.equals("")) {
            if (typeUser == 1) {
                Etudiant etu = EtudiantDAO.getEtudiantByID(idUser);
                if (etu.getId().equals(etuLogin.getId())) {
                    if (oldPass.equals(etu.getPassword())) {
                        etu.setPrenom(prenom);
                        etu.setNom(nom);
                        etu.setMail(mail);
                        if (!newPass.equals("") && !passVerif.equals("")) {
                            if (newPass.equals(passVerif)) {
                                if (!newPass.equals(oldPass)) {
                                    etu.setPassword(newPass);
                                }
                            } else {
                                codeErreur = 2;
                            }
                        }
                        EtudiantDAO.update(etu);
                        etuLogin = etu;
                    } else {
                        codeErreur = 1;
                    }
                } else {
                    codeErreur = 3;
                }
            } else {
                Prof prof = ProfDAO.getUserByID(idUser);
                if (prof.getId().equals(profLogin.getId())) {
                    if (oldPass.equals(prof.getPassword())) {
                        prof.setPrenom(prenom);
                        prof.setNom(nom);
                        prof.setMail(mail);
                        if (!newPass.equals("") && !passVerif.equals("")) {
                            if (newPass.equals(passVerif)) {
                                if (!oldPass.equals(newPass)) {
                                    prof.setPassword(newPass);
                                }
                            } else {
                                codeErreur = 2;
                            }
                        }
                        ProfDAO.update(prof);
                        profLogin = prof;
                    } else {
                        codeErreur = 1;
                    }
                } else {
                    codeErreur = 3;
                }
            }
        } else {
            codeErreur = 4;
        }
        request.setAttribute("codeErreur", codeErreur);
        loadJSP(urls.get("urlProfil"), request, response);
    }

    /**
     * Affiche la page de modification du profil.
     * Retourne un "codeErreur" égal à -1 (car pas d'erreur).
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doProfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("codeErreur", -1);
        loadJSP(urls.get("urlProfil"), request, response);
    }

    /**
     * Vérifie un login, puis renvoie sur la bonne page.
     * Utilise un "mail" et un "pass".
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doVerifLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mail = utility.verifParameterString(request, "mail");
        String pass = utility.verifParameterString(request, "pass");

        Prof prof = ProfDAO.getUserByMail(mail);
        Etudiant etu = EtudiantDAO.getUserByMail(mail);
        if (prof != null) {
            if (prof.getPassword().equals(pass)) {
                profLogin = prof;
                doListe(request, response);
            } else {
                request.setAttribute("erreur", 2);
                doDisconnect(request, response);
            }
        } else if (etu != null) {
            if (etu.getPassword().equals(pass)) {
                etuLogin = etu;
                request.setAttribute("etudiant", etu);
                loadJSP(urls.get("urlFicheEtudiant"), request, response);
            } else {
                request.setAttribute("erreur", 2);
                doDisconnect(request, response);
            }
        } else {
            request.setAttribute("erreur", 1);
            doDisconnect(request, response);
        }
    }

    /**
     * Fonction
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        boolean isConnected = false;
        LocalDateTime validity = null;
        if (session.getAttribute("isConnected") != null) {
            isConnected = (boolean) session.getAttribute("isConnected");
        }

        if (session.getAttribute("validity") != null) {
            validity = (LocalDateTime) session.getAttribute("validity");
        }

        if (isConnected && validity != null) {
            LocalDateTime now = LocalDateTime.now();
            Duration timeSinceLastLogin = Duration.between(validity, now);
            if (timeSinceLastLogin.toMinutes() < 60) {
                // automatically log in the user
                session.setAttribute("validity", LocalDateTime.now());
                if (etuLogin != null && etuLogin.getMail() != null) {
                    request.setAttribute("mail", etuLogin.getMail());
                    request.setAttribute("pass", etuLogin.getPassword());
                    doVerifLogin(request, response);
                } else if (profLogin != null && profLogin.getMail() != null) {
                    request.setAttribute("mail", profLogin.getMail());
                    request.setAttribute("pass", profLogin.getPassword());
                    doVerifLogin(request, response);
                } else {
                    // set the user's login status to false
                    session.setAttribute("isConnected", false);
                    request.setAttribute("erreur", 0);
                    request.setAttribute("mdp", "");
                    doDisconnect(request, response);
                }
            } else {
                // set the user's login status to false
                session.setAttribute("isConnected", false);
                request.setAttribute("erreur", 0);
                request.setAttribute("mdp", "");
                doDisconnect(request, response);
            }
        } else {
            // set the user's login status to false
            session.setAttribute("isConnected", false);
            request.setAttribute("erreur", 0);
            request.setAttribute("mdp", "");
            doDisconnect(request, response);
        }
    }



    /**
     * Récupère le "coef", le "libelle" et la "note-[idEtu]" (ou une "isAbsent")
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doAppliquerNotes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int coef = utility.verifParameterInt(request, "coef");
        if (coef == -1) {
            coef = 1;
        }

        String libelle = utility.verifParameterString(request, "libelle");
        if (libelle.equals("")) {
            libelle = "Note devoir surveillé";
        }

        //Controle ctrl = ControleDAO.create(profLogin, libelle);
        Controle ctrl = new Controle(profLogin, libelle);

        if (ctrl != null) {
            List<Note> listeNotes = new ArrayList<>();
            for (Etudiant etu : listeEtu) {
                float note = utility.verifParameterFloat(request, "note-" + etu.getId());

                boolean isAbsent = utility.verifParameterString(request, "isAbsent-" + etu.getId()).equals("on");
                if (!isAbsent) {
                    if (note < 0) {
                        note = 0;
                    }
                    Note oNote = new Note(note, coef, etu, ctrl);
                    ctrl.getListeNotes().add(oNote);
                    //listeNotes.add(etu.setNote(note, coef, ctrl));
                    //EtudiantDAO.update(etu);
                }
            }

            ControleDAO.create(ctrl);

            doListe(request, response);
        } else {
            doDisconnect(request, response);
        }


    }

    /**
     * Récupère le nom de la page "who" pour rediriger vers la recherche étudiants de cette page.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doRechercheEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("who") != null) {
            String who = String.valueOf(request.getParameter("who"));
            if (who.equals("listeEtudiants.jsp")) {
                doListe(request, response);
            } else if (who.equals("addNotes.jsp")) {
                doListeNotes(request, response);
            } else {
                doListe(request, response);
            }
        }
    }

    /**
     * Récupère un "prenom", un "nom", un "groupe" pour créer un prof.
     * Renvoie un "codeErreur" sur la page de création si erreur.
     * Sinon, renvoie un "etudiant" qui vient d'être créé sur la fiche de cet étudiant.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCreationProf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codeErreur = 0;
        String prenom = utility.verifParameterString(request, "prenom");
        String nom = utility.verifParameterString(request, "nom");
        if (prenom == null || prenom.equals("")) {
            codeErreur += 1;
        }

        if (nom == null || nom.equals("")) {
            codeErreur += 2;
        }

        String isAdminString = utility.verifParameterString(request, "isAdmin");
        boolean isAdmin;

        if (!isAdminString.equals("on")) {
            isAdmin = false;
        } else {
            isAdmin = true;
        }

        Long idGroupe = utility.verifParameterLong(request, "groupe");

        if (idGroupe == -1 || isAdmin) {
            idGroupe = null;
        }


        if (codeErreur > 0) {
            String codeErreurBinaire = Integer.toBinaryString(codeErreur);
            codeErreurBinaire = "00000000" + codeErreurBinaire;
            request.setAttribute("codeErreur", codeErreurBinaire);
            loadJSP(urls.get("urlCreerProf"), request, response);
        } else {
            Prof prof = ProfDAO.create(prenom, nom, prenom + "." + nom + "@iut.fr", prenom.charAt(0) + nom.charAt(0) + "123", isAdmin, idGroupe);

            doListe(request, response);
        }
    }


    /**
     * Affiche la page de création d'un prof.
     * Retourne la List des "groupes", et un "codeErreur" = 0;
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCreerProf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les étudiants
        addAllGroupes(request);
        request.setAttribute("codeErreur", 0);

        //
        loadJSP(urls.get("urlCreerProf"), request, response);
    }

    /**
     * Récupère un "prenom", un "nom" et un "groupe" pour créer un étudiant.
     * Renvoie un "codeErreur" sur la page de création si erreur.
     * Sinon, renvoie un "etudiant" qui vient d'être créé sur la fiche de cet étudiant.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCreationEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int codeErreur = 0;
        if (request.getParameter("prenom") == null || String.valueOf(request.getParameter("prenom")).equals("")) {
            codeErreur += 1;
        }

        if (request.getParameter("nom") == null || String.valueOf(request.getParameter("nom")).equals("")) {
            codeErreur += 2;
        }

        Groupe groupeFound = null;
        if (request.getParameter("groupe") != null && !String.valueOf(request.getParameter("groupe")).equals("")) {
            Set<Groupe> groupes = GroupeDAO.getAll();
            for (Groupe gr : groupes) {
                if (gr.getNom().equals(request.getParameter("groupe"))) {
                    groupeFound = gr;
                }
            }
        }
        if (groupeFound == null) {
            codeErreur += 4;
        }


        if (codeErreur > 0) {
            String codeErreurBinaire = Integer.toBinaryString(codeErreur);
            codeErreurBinaire = "00000000" + codeErreurBinaire;
            request.setAttribute("codeErreur", codeErreurBinaire);
            loadJSP(urls.get("urlCreerEtu"), request, response);
        } else {
            String prenom = utility.verifParameterString(request, "prenom");
            String nom = utility.verifParameterString(request, "nom");
            Etudiant etu = EtudiantDAO.create(prenom, nom, prenom + "." + nom + "@iut.fr", prenom.charAt(0) + nom.charAt(0) + "123", groupeFound);

            request.setAttribute("etudiant", etu);
            loadJSP(urls.get("urlFicheEtudiant"), request, response);
        }
    }

    /**
     * Affiche la page de création d'un étudiant.
     * Retourne la List des "groupes", et un "codeErreur" = 0;
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCreerEtu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les étudiants
        addAllGroupes(request);
        request.setAttribute("codeErreur", 0);

        //
        loadJSP(urls.get("urlCreerEtu"), request, response);
    }


    /**
     * Supprime l'étudiant avec un "id".
     * Affiche la liste des étudiants.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDeleteEtu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EtudiantDAO.remove((long) Integer.parseInt(request.getParameter("id")));
        doListe(request, response);
    }


    /**
     * Retourne la liste des étudiants trouvés grâce au "nom" précisé dans la query
     *
     * @param request
     * @return Liste d'étudiants
     */
    private List<Etudiant> getEtudiantsFromNom(HttpServletRequest request) {
        if (request.getParameter("nom") != null) {
            request.setAttribute("recherche", request.getParameter("nom"));
            return EtudiantDAO.getEtudiantsByName(request.getParameter("nom"));
        } else {
            request.setAttribute("recherche", "");
            return null;
        }

    }

    /**
     * Si la requête provient d'une page avec un filtre (argument "isFiltre" présent).
     * Les arguments de la requête seront utilisées pour définir quels filtres seront actifs.
     * Les derniers filtres utilisées sont sauvegardés dans des attributs de classe.
     * Si la requête provient d'une page sans filtre, on utilise les attributs de classe sauvegardés précédemment.
     * On ajoute alors le nombre binaire obtenu à la requête. (1er bit = Moyenne, 2eme bit = abs, 3eme bit = groupe).
     *
     * @param request- requête
     * @return request
     */
    private HttpServletRequest getFiltresBinaire(HttpServletRequest request) {
        int filtreAfficher = 0;
        if (request.getParameter("isFiltre") != null) {
            if (request.getParameter("fMoyenne") != null) {
                filtreAfficher += 1;
                fMoyenne = true;
            } else {
                fMoyenne = false;
            }

            if (request.getParameter("fAbs") != null) {
                filtreAfficher += 2;
                fAbs = true;
            } else {
                fAbs = false;
            }

            if (request.getParameter("fGroupeEtu") != null) {
                filtreAfficher += 4;
                fGroupe = true;
            } else {
                fGroupe = false;
            }
        } else {
            if (fMoyenne) {
                filtreAfficher += 1;
            }

            if (fAbs) {
                filtreAfficher += 2;
            }

            if (fGroupe) {
                filtreAfficher += 4;
            }
        }
        request.setAttribute("filtre", "00000000" + Integer.toBinaryString(filtreAfficher));
        return request;
    }

    /**
     * Retourne la liste des objets Groupe séléctionnés par l'utilisateur (dans la query)
     *
     * @param request
     * @return Liste de Groupe
     */
    private List<Groupe> getGroupesFromQuery(HttpServletRequest request) {
        List<Groupe> groupesFiltre = new ArrayList<>();
        int isGroupeFiltre = 0;
        if (profLogin.isAdmin()) {
            if (request.getParameter("allClasses") == null) {
                for (Groupe gr : GroupeDAO.getAll()) {
                    if (request.getParameter(gr.getNom()) != null) {
                        groupesFiltre.add(gr);
                        isGroupeFiltre = 1;
                    }
                }
            }
        } else {
            groupesFiltre.add(profLogin.getGroupe());
        }
        //Bolean qui permet de savoir si le bouton "tous les groupes" est désactivé ou non
        request.setAttribute("isGroupeFiltre", isGroupeFiltre);
        // Ajoute la liste des groupes séléctionnés (pour les boutons des groupes)
        request.setAttribute("groupesFiltre", groupesFiltre);
        return groupesFiltre;
    }

    /**
     * Retourne la liste des étudiants qui sont dans les groupes de la query
     *
     * @param request
     * @return Liste d'étudiant
     */
    private List<Etudiant> getEtudiantsByGroupesFromQuery(HttpServletRequest request) {
        //Récupération des groupes choisis par l'utilisateur
        List<Groupe> groupesFiltre = getGroupesFromQuery(request);

        // Récupère soit tous les étudiants, soit les étudiants appartenant à certains groupes
        List<Etudiant> etudiants = new ArrayList<>();
        //Si des groupes sont choisis
        if (!groupesFiltre.isEmpty()) {
            // On récupère les étudiants de chaque groupe
            for (Groupe gr : groupesFiltre) {
                etudiants.addAll(EtudiantDAO.getAllByGroupe(gr));
            }
        } else {
            //Sinon, on récupère tous les étudiants
            etudiants = EtudiantDAO.getAll();
        }
        return etudiants;
    }

    /**
     * Retourne la liste des étudiants en commun entre les deux listes
     *
     * @param listePrioritaire
     * @param listeSecondaire
     * @return liste d'étudiants
     */
    private List<Etudiant> getMatchingEtudiants(List<Etudiant> listePrioritaire, List<Etudiant> listeSecondaire) {
        //Si les deux listes sont peuplées
        if (listePrioritaire != null && !listePrioritaire.isEmpty() && listeSecondaire != null && !listeSecondaire.isEmpty()) {
            //On crée l'array contenant les correspondances
            List<Etudiant> etudiantsMatch = new ArrayList<>();
            for (Etudiant etuPrio : listePrioritaire) {
                for (Etudiant etuSec : listeSecondaire) {
                    if (etuPrio.getId().equals(etuSec.getId())) {
                        etudiantsMatch.add(etuPrio);
                    }
                }
            }
            return etudiantsMatch;
        } else if (listePrioritaire == null || listePrioritaire.isEmpty()) {
            return listeSecondaire;
        } else {
            return listePrioritaire;
        }
    }

    /**
     * Récupère la liste des étudiants (filtrée si besoin).
     * Renvoie une List "etudiants" sur la page de liste.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doListe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        addAllGroupes(request);

        //Récupération des valeurs des boutons por l'affichage des infos supplémentaires (moyenne, nbAbs, groupe)
        getFiltresBinaire(request);


        //Récupération et ajout des étudiants en commun entre les deux listes (triés par champs recherche / par groupe)
        listeEtu = getMatchingEtudiants(getEtudiantsFromNom(request), getEtudiantsByGroupesFromQuery(request));

        Utility.trierListeEtu(listeEtu);


        request.setAttribute("etudiants", listeEtu);

        //
        loadJSP(urls.get("urlListeEtudiants"), request, response);
    }


    /**
     * Même fonction que doListe, mais page différente -> regrouper les fonctions ?
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doListeNotes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addAllGroupes(request);

        //Récupération des valeurs des boutons por l'affichage des infos supplémentaires (moyenne, nbAbs, groupe)
        getFiltresBinaire(request);


        //Récupération et ajout des étudiants en commun entre les deux listes (triés par champs recherche / par groupe)
        listeEtu = getMatchingEtudiants(getEtudiantsFromNom(request), getEtudiantsByGroupesFromQuery(request));
        Utility.trierListeEtu(listeEtu);
        request.setAttribute("etudiants", listeEtu);

        //
        loadJSP(urls.get("urlAddNotes"), request, response);
    }


    /**
     * Récupère un "id" étudiant.
     * Revnvoie un "etudiant", et un "codeErreur" = 0, sur la page d'édition d'un étudiant.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doEditionEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer l'id de l'étudiant
        Long idEtudiant = utility.verifParameterLong(request, "id");

        // Récupérer l'étudiant
        Etudiant etudiant = EtudiantDAO.retrieveById(idEtudiant);

        // Ajouter l'étudiant à la requête pour affichage
        request.setAttribute("etudiant", etudiant);
        request.setAttribute("prof", profLogin);

        //
        request.setAttribute("codeErreur", 0);
        loadJSP(urls.get("urlEditionEtudiant"), request, response);
    }

    /**
     * Affiche la page d'un "etudiant" avec son "id".
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doFicheEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer l'id de l'étudiant
        Long idEtudiant = utility.verifParameterLong(request, "id");
        if (idEtudiant == -1 && etuLogin != null) {
            idEtudiant = etuLogin.getId();
        }

        // Récupérer l'étudiant
        Etudiant etudiant = EtudiantDAO.retrieveById(idEtudiant);

        // Ajouter l'étudiant à la requête pour affichage
        request.setAttribute("etudiant", etudiant);

        //
        loadJSP(urls.get("urlFicheEtudiant"), request, response);
    }


    /**
     * Récupère un "id", un "nbAbs", une liste de note-[idNote].
     * Modifie l'étudiant concerné, et retourne un "etudiant" et un "codeErreur".
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doModifEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer l'id de l'étudiant
        Long idEtudiant = utility.verifParameterLong(request, "id");
        // Récupérer l'étudiant
        Etudiant etudiant = EtudiantDAO.retrieveById(idEtudiant);


        int absencesEtudiant;
        try {
            absencesEtudiant = Integer.parseInt(request.getParameter("nbAbs"));
        } catch (NumberFormatException e) {
            absencesEtudiant = etudiant.getNbAbsences();
        }

        //Si code = 1 -> problème nb abs, si code = 2 -> erreur note, si code = 3 -> erreur aux deux
        //Si code = 0, tout est bon
        int codeErreur = 0;
        List<Note> notes = etudiant.getNotes();

        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            float noteF = utility.verifParameterFloat(request, "note-" + note.getId());
            if (noteF == -1) {
                noteF = note.getNote();
            }
            if (noteF >= 0.0f && noteF <= 20.0f) {
                note.setNote(noteF);
                note.setDateModif(new Date());
                NoteDAO.update(note);
                EtudiantDAO.update(etudiant);
            } else {
                codeErreur = 2;
            }
        }


        if (absencesEtudiant < etudiant.getNbAbsences()) {
            if (absencesEtudiant < etudiant.getNbAbsences()) {
                codeErreur += 1;
            }
        }

        if (codeErreur == 0) {
            // Modifier l'étudiant
            etudiant.setNbAbsences(absencesEtudiant);
            EtudiantDAO.update(etudiant);
            request.setAttribute("etudiant", etudiant);
            loadJSP(urls.get("urlFicheEtudiant"), request, response);
        } else {
            // Ajouter le code d'erreur à la requête pour affichage
            request.setAttribute("codeErreur", codeErreur);
            if (codeErreur == 1) {
                //Si il y a un problème avec le nb abs uniquement
                //on update la moyenne
                EtudiantDAO.update(etudiant);
                request.setAttribute("etudiant", etudiant);
                loadJSP(urls.get("urlEditionEtudiant"), request, response);
            } else if (codeErreur == 2) {
                //Si il y a un problème avec la moyenne uniquement
                //on update le nb abs
                etudiant.setNbAbsences(absencesEtudiant);
                EtudiantDAO.update(etudiant);
                request.setAttribute("etudiant", etudiant);
                loadJSP(urls.get("urlEditionEtudiant"), request, response);
            } else {
                //Il y a des problèmes partout
                //Pas de mise à jour
                request.setAttribute("etudiant", etudiant);
                loadJSP(urls.get("urlEditionEtudiant"), request, response);
            }
        }


    }

    /**
     * Déconnecte l'utilisateur actuel, et le renvoie au login.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDisconnect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        etuLogin = null;
        profLogin = null;
        if (request.getAttribute("erreur") == null) {
            request.setAttribute("erreur", 0);
        }
        request.setAttribute("profLogin", null);
        request.setAttribute("etuLogin", null);
        request.getSession().setAttribute("isConnected", false);
        request.getSession().invalidate();

        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(urls.get("urlLogin"));
        rd.forward(request, response);
    }


    /**
     * Charge une page JSP. Vérifie encore une fois si l'utilisateur connecté à les droits pour cette page.
     * @param url
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loadJSP(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		L'interface RequestDispatcher permet de transférer le contrôle à une autre servlet
//		Deux méthodes possibles :
//		- forward() : donne le contrôle à une autre servlet. Annule le flux de sortie de la servlet courante
//		- include() : inclus dynamiquement une autre servlet
//			+ le contrôle est donné à une autre servlet puis revient à la servlet courante (sorte d'appel de fonction).
//			+ Le flux de sortie n'est pas supprimé et les deux se cumulent

        if ((profLogin != null && profLogin.isHasConnected()) || (etuLogin != null && etuLogin.getHasConnected())) {
            request.setAttribute("hasConnected", true);
        } else {
            request.setAttribute("hasConnected", false);
        }
        request.setAttribute("profLogin", profLogin);
        request.setAttribute("etuLogin", etuLogin);

        //Si un étudiant essaye d'accéder à une page réservée aux prof, on le déconnecte.
        if (etuLogin != null) {
            if (actionsProfs.contains(request.getPathInfo())) {
                doDisconnect(request, response);
            }
        }


        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        rd.forward(request, response);
    }

}
