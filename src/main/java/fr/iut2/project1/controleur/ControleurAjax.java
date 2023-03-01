package fr.iut2.project1.controleur;

import com.google.gson.Gson;
import fr.iut2.project1.Etudiant.Etudiant;
import fr.iut2.project1.Etudiant.EtudiantDAO;
import fr.iut2.project1.Note.NoteDAO;
import fr.iut2.project1.Prof.Prof;
import fr.iut2.project1.Prof.ProfDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@SuppressWarnings("serial")
public class ControleurAjax extends HttpServlet {


    // POST
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // On vérifie dans un premier temps si la demande est une requête ajax (XMLHttpRequest)
        if (isXMLHttpRequest(request)) {

            // On récupère le path
            String action = request.getPathInfo();

            // Exécution action
            if (action.equals("/addAbs")) {
                doAddAbs(request, response);
            } else if (action.equals("/setConnected")){
                doSetConnected(request, response);
            } else if (action.equals("/deleteGrade")){
                doDeleteGrade(request, response);
            } else if (action.equals("/deleteUser")){
                doDeleteUser(request, response);
            }
        } else {
            // Bad request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Récupère un "idUser" et un "tupeUser".
     * Supprime l'utilisateur (1 = etu, 2 = prof) correspondant.
     * Retourne true si tout est bon, false sinon.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDeleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUser = Integer.parseInt(request.getParameter("idUser"));
        int typeUser = Integer.parseInt(request.getParameter("typeUser"));
        boolean isDeleted = false;
        if (typeUser == 1) {
            isDeleted = EtudiantDAO.remove((long) idUser);
        } else if (typeUser == 2) {
            isDeleted = ProfDAO.remove((long) idUser);
        }


        String json = new Gson().toJson(isDeleted);
        // Retourne le résultat sous forme JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    /**
     * Récupère un "idGrade" et supprime la note correspondante.
     * Retourne la moyenne actuelle de l'étudiant (ou -1 si pas de moyenne)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDeleteGrade(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idGrade = Integer.parseInt(request.getParameter("idGrade"));


        String json = new Gson().toJson(NoteDAO.remove(NoteDAO.getNoteById(idGrade)));
        // Retourne le résultat sous forme JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    /**
     * Récupère un "id" d'étudiant, et ajoute une absence.
     * Retourne un nombre d'absences.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doAddAbs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = verifParameterLong(request, "id");


        String json = new Gson().toJson(EtudiantDAO.addAbsences(id, 1).getNbAbsences());

        // Retourne le résultat sous forme JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    /**
     * Récupère un "id", et passe le champ "hasConnected" d'un utilisateur à true.
     * Revoie la valeur de ce champ.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doSetConnected(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = verifParameterLong(request, "id");

        Prof prof = ProfDAO.retrieveById(id);
        Etudiant etu = EtudiantDAO.retrieveById(id);
        String json = null;

        if (etu != null && prof == null) {
            EtudiantDAO.update(etu.setConnected());
            json = new Gson().toJson(etu.getHasConnected());
        } else if (prof != null && etu == null) {
            ProfDAO.update(prof.setConnected());
            json = new Gson().toJson(prof.isHasConnected());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    /**
     * Fonction dupliquée du ctrl
     * @param request
     * @param nomParam
     * @return
     */
    public Long verifParameterLong(HttpServletRequest request, String nomParam) {
        if (request.getParameter(nomParam) != null) {
            long number;
            try {
                number = Integer.parseInt(request.getParameter(nomParam));
            } catch (NumberFormatException e) {
                number = -1;
            }
            return number;
        } else {
            return (long) -1;
        }
    }


    /**
     * Vérifie si la requete est bien du bon type
     * @param request
     * @return
     */
    private boolean isXMLHttpRequest(HttpServletRequest request) {
        return request.getHeader("x-requested-with").equals("XMLHttpRequest");
    }

}
