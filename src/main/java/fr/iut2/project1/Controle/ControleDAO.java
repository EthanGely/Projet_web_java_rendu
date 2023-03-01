package fr.iut2.project1.Controle;


import fr.iut2.project1.utility.GestionFactory;
import fr.iut2.project1.Prof.Prof;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ControleDAO {


    public static Controle retrieveById(int id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        Controle ctr = em.find(Controle.class, id);

        // Close the entity manager
        em.close();

        return ctr;
    }


    public static void create(Controle ctrl) {


            // Creation de l'entity manager
            EntityManager em = GestionFactory.factory.createEntityManager();

            em.getTransaction().begin();
            em.persist(ctrl);
            em.getTransaction().commit();
            em.close();
    }


    public static Controle add(Controle controle) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();


        em.persist(controle);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return controle;
    }

    public static Controle update(Controle ctr) {

        Controle controleBDD = getByID(ctr.getId());

        remove(controleBDD);

        controleBDD = ctr;

        return add(controleBDD);
    }


    public static void remove(Controle ctr) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // L'étudiant passé en paramètre doit être associé à l'EM
        if (!em.contains(ctr)) {
            ctr = em.merge(ctr);
        }

        // Remove
        em.remove(ctr);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();
    }

    public static void remove(int id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        //
        em.createQuery("DELETE FROM Controle AS c WHERE c.idC = :id")
                .setParameter("id", id)
                .executeUpdate();

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();
    }

    public static int removeAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // RemoveAll
        int deletedCount = em.createQuery("DELETE FROM Controle").executeUpdate();

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return deletedCount;
    }

    // Retourne l'ensemble des controles
    public static List<Controle> getAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche 
        Query q = em.createQuery("SELECT c FROM Controle c");

        @SuppressWarnings("unchecked")
        List<Controle> listControle = q.getResultList();

        return listControle;
    }

    public static List<Controle> getByProf(Prof prof) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        if (prof.isAdmin()) {
            return getAll();
        } else {
            Query q = em.createQuery("SELECT c FROM Controle c WHERE c.prof = :prof GROUP BY c.idC")
                    .setParameter("prof", prof);

            @SuppressWarnings("unchecked")
            List<Controle> listControle = q.getResultList();

            return listControle;
        }
    }

    public static Controle getByProfIntitule(Prof prof, String intitule) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche

        Query q = em.createQuery("SELECT c FROM Controle c WHERE c.prof = :prof AND c.intitule = :intitule")
                .setParameter("prof", prof).setParameter("intitule", intitule);

        try {
            @SuppressWarnings("unchecked")
            Controle controle = (Controle) q.getSingleResult();
            return controle;
        }catch (NoResultException e) {
            return null;
        }

    }

    public static Controle getByID(Long id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT c FROM Controle c WHERE c.idC = :id GROUP BY c.idC")
                .setParameter("id", id);

        try {
            @SuppressWarnings("unchecked")
            Controle controle = (Controle) q.getSingleResult();
            return controle;
        }catch (NoResultException e) {
            return null;
        }
    }
}
