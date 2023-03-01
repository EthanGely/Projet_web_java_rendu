package fr.iut2.project1.Prof;


import fr.iut2.project1.utility.GestionFactory;
import fr.iut2.project1.Groupe.Groupe;
import fr.iut2.project1.Groupe.GroupeDAO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ProfDAO {

    private static int NB_ABSENCES_MAX = 8;

    public static Prof retrieveById(Long id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        Prof etu = em.find(Prof.class, id);
        // etu est maintenant un objet de la classe Etudiant
        // ou NULL si l'étudiant n'existe pas

        // Close the entity manager
        em.close();

        return etu;
    }


    public static Prof create(String prenom, String nom, String mail, String pass, boolean isAdmin, Long groupID) {


        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // create new etudiant
        Prof prof = new Prof();
        prof.setPrenom(prenom);
        prof.setNom(nom);
        prof.setMail(mail);
        prof.setPassword(pass);
        prof.setAdmin(isAdmin);
        if (!isAdmin) {
            Groupe groupe = GroupeDAO.getByID(groupID);
            prof.setGroupe(groupe, false);
        }

        em.persist(prof);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return prof;
    }

    public static Prof update(Prof prof) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // Attacher une entité persistante (etudiant) à l’EntityManager courant  pour réaliser la modification
        em.merge(prof);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return prof;
    }


    public static void remove(Prof prof) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // L'étudiant passé en paramètre doit être associé à l'EM
        if (!em.contains(prof)) {
            prof = em.merge(prof);
        }

        // Remove
        em.remove(prof);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();
    }

    public static boolean remove(Long id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        Prof prof = getUserByID(id);
        if (prof != null) {
            remove(prof);
            return true;
        } else {
            return false;
        }
    }

    public static int removeAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // RemoveAll
        int deletedCount = em.createQuery("DELETE FROM Prof").executeUpdate();

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return deletedCount;
    }

    // Retourne l'ensemble des etudiants
    public static List<Prof> getAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT u FROM Prof u");

        @SuppressWarnings("unchecked")
        List<Prof> listProf = q.getResultList();

        return listProf;
    }


    public static Prof getUserByID(Long id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT p FROM Prof p WHERE p.idP = :id")
                .setParameter("id", id);

        Prof prof;

        try {
            prof = (Prof) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }



        return prof;
    }

    public static Prof getUserByMail(String mail) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT p FROM Prof p WHERE p.mail = :mail")
                .setParameter("mail", mail).setMaxResults(1);

        try {
            @SuppressWarnings("unchecked")
            Prof prof = (Prof) q.getSingleResult();
            return prof;
        } catch (NoResultException e) {
            return null;
        }
    }

}
