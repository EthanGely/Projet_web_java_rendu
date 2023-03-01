package fr.iut2.project1.Note;



import fr.iut2.project1.Controle.Controle;
import fr.iut2.project1.Etudiant.Etudiant;
import fr.iut2.project1.utility.GestionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class NoteDAO {

    public static Note create(Float noteEtu, int coef, Etudiant etudiant, Controle ctrl) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // create
        em.getTransaction().begin();

        // create new groupe
        Note note = new Note(noteEtu, coef, etudiant, ctrl);
        em.persist(note);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return note;
    }

    public static float remove(Note note) {

        float moyenne = note.getEtudiant().deleteGrade(note);

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // Le groupe passé en paramètre doit être associé à l'EM
        if (!em.contains(note)) {
            note = em.merge(note);
        }

        // Supprime l'entité courante mais aussi les entités (étudiants) liées
        // grâce à l'annotation cascade = {CascadeType.REMOVE} dans la classe Groupe
        em.remove(note);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();
        return moyenne;
    }

    public static int removeAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // RemoveAll
        int deletedCount = em.createQuery("DELETE FROM Note").executeUpdate();

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return deletedCount;
    }


    public static List<Note> getAll() {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT n FROM Note n");

        @SuppressWarnings("unchecked")
        List<Note> listNote = q.getResultList();

        return listNote;
    }

    public static Note update(Note note) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        //
        em.getTransaction().begin();

        // Attacher une entité persistante (etudiant) à l’EntityManager courant  pour réaliser la modification
        em.merge(note);

        // Commit
        em.getTransaction().commit();

        // Close the entity manager
        em.close();

        return note;
    }


    public static Note getNoteById(int id) {

        // Creation de l'entity manager
        EntityManager em = GestionFactory.factory.createEntityManager();

        // Recherche
        Query q = em.createQuery("SELECT n FROM Note n WHERE n.idN = :id")
                .setParameter("id", id);

        @SuppressWarnings("unchecked")
        Note note = (Note) q.getSingleResult();

        return note;
    }
}

