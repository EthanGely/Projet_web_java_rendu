package fr.iut2.project1.Etudiant;

import fr.iut2.project1.Controle.Controle;
import fr.iut2.project1.Groupe.Groupe;
import fr.iut2.project1.Note.Note;
import fr.iut2.project1.Note.NoteDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity implementation class for Entity: Groupe
 */
@Entity
@Table(name = "etudiant")
public class Etudiant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idE;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "nom", nullable = false)
    private String nom;


    @Column(name="mail", nullable = false)
    private String mail;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "hasConnected" ,nullable = false)
    private boolean hasConnected = false;

    @Column(name = "nbAbsences")
    private int nbAbsences = 0;


    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.PERSIST)
    private List<Note> notes;
    @ManyToOne
    @JoinColumn(name = "idG")
    private Groupe groupe;

    @ManyToMany(mappedBy = "etudiants", fetch = FetchType.EAGER)
    private List<Controle> controles;


    private static final long serialVersionUID = 1L;

    public Etudiant() {}

    public Etudiant(String prenom, String nom, String mail, String pass, Groupe groupe) {
        this.prenom = prenom;
        this.nom = nom;
        this.mail = mail;
        this.password = pass;
        this.groupe = groupe;
    }

    public Long getId() {
        return idE;
    }

    public void setId(Long id) {
        this.idE = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getHasConnected() {
        return hasConnected;
    }

    public void setHasConnected(boolean hasConnected) {
        this.hasConnected = hasConnected;
    }

    public int getNbAbsences() {
        return nbAbsences;
    }

    public void setNbAbsences(int nbAbsences) {
        this.nbAbsences = nbAbsences;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public List<Controle> getControles() {
        return controles;
    }

    public void setControles(List<Controle> controles) {
        this.controles = controles;
    }


    public float getMoyenne() {
        if (!notes.isEmpty()) {
            return calculerMoyenne();
        } else {
            return -1;
        }
    }

    public float deleteGrade(Note note) {
        notes.remove(note);
        return getMoyenne();
    }

    @Override
    public String toString() {
        return "[" + this.getId() + "] " + this.getPrenom() + " " + this.getNom();
    }



    public void setAllNotes(List<Note> note) {
        this.notes = note;

    }

    private void filtreNote() {
        Collections.sort(notes, (p1, p2) -> p1.compareTo(p2));
        Long id = (long) -1;
        List<Note> toDelete = new ArrayList<>();
        for (Note note : notes) {
            if (note.getId() == id) {
                toDelete.add(note);
            } else {
                id = note.getId();
            }
        }
        for (Note n2d : toDelete) {
            notes.remove(n2d);
        }
    }

    public Note setNote(float note, int coef, Controle ctrl) {
        Note newNote = NoteDAO.create( note, coef, this, ctrl);
        notes.add(newNote);
        EtudiantDAO.update(this);
        return newNote;
    }

    public Note getNoteById(int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                return note;
            }
        }
        return null;
    }

    public float calculerMoyenne() {
        filtreNote();
        float sommeNotesCoef = 0;
        float sommeCoef = 0;
        for (Note noteEtu : notes) {
            sommeNotesCoef += noteEtu.getNote() * noteEtu.getCoef();
            sommeCoef += noteEtu.getCoef();
        }
        return (sommeNotesCoef / sommeCoef);
    }

    public Etudiant setConnected() {
        this.hasConnected = true;
        return this;
    }

    public Etudiant setDisconnected() {
        this.hasConnected = false;
        return this;
    }

}
