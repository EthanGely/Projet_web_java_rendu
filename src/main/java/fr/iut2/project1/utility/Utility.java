package fr.iut2.project1.utility;

import fr.iut2.project1.Etudiant.Etudiant;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class Utility extends HttpServlet {

    /**
     * Récupère, vérifie et retourne un Long depuis la requête.
     * Retourne -1 si erreur.
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
     * Récupère, vérifie et retourne un int depuis la requête.
     * Retourne -1 si erreur.
     * @param request
     * @param nomParam
     * @return
     */
    public int verifParameterInt(HttpServletRequest request, String nomParam) {
        if (request.getParameter(nomParam) != null) {
            int number = 1;
            try {
                number = Integer.parseInt(request.getParameter(nomParam));
            } catch (NumberFormatException e) {
                number = -1;
            }
            return number;
        } else {
            return -1;
        }
    }

    /**
     * Récupère, vérifie et retourne un String depuis la requête.
     * Retourne -1 si erreur.
     * @param request
     * @param nomParam
     * @return
     */
    public String verifParameterString(HttpServletRequest request, String nomParam) {
        if (request.getParameter(nomParam) != null) {
            return String.valueOf(request.getParameter(nomParam));
        } else {
            return "";
        }
    }

    /**
     * Récupère, vérifie et retourne un float depuis la requête.
     * Retourne -1 si erreur.
     * @param request
     * @param nomParam
     * @return
     */
    public float verifParameterFloat(HttpServletRequest request, String nomParam) {
        if (request.getParameter(nomParam) != null) {
            float number = 1;
            try {
                number = Float.parseFloat(request.getParameter(nomParam));
            } catch (NumberFormatException e) {
                number = 0;
            }
            return number;
        } else {
            return -1;
        }
    }

    /**
     * Ajoute les actions qui nécessitent d'être connecté en tant que Prof
     * @return HastSet : String
     */
    public Set<String> addProfActions() {
        Set<String> actionsProfs = new HashSet<>();
        //Certaines pages / actions sont réservées aux profs. On les ajoute à la liste
        actionsProfs.add("/listeEtudiants");
        actionsProfs.add("/editionetudiant");
        actionsProfs.add("/modifetudiant");
        actionsProfs.add("/SupprimerEtu");
        actionsProfs.add("/CreerEtu");
        actionsProfs.add("/CreationEtudiant");
        actionsProfs.add("/listeControles");
        actionsProfs.add("/DetailControle");
        actionsProfs.add("/addNotes");
        actionsProfs.add("/validerNotes");
        actionsProfs.add("/creerProf");
        actionsProfs.add("/creationProf");
        return actionsProfs;
    }

    /**
     * Tri de la liste des étudiants par nom de groupe, puis par leur prénom respectif
     * @param listeEtu
     */
    public static void trierListeEtu(List<Etudiant> listeEtu) {
        Collections.sort(listeEtu, Comparator.comparing((Etudiant s) -> s.getGroupe().getNom()).thenComparing(Etudiant::getPrenom));
    }

}
