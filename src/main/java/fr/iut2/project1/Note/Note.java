package fr.iut2.project1.Note;

import fr.iut2.project1.Controle.Controle;
import fr.iut2.project1.Etudiant.Etudiant;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "note")
public class Note implements Comparable<Note> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idN;

    @Column(nullable = false)
    private float note;

    @Column(nullable = false)
    private int coef;

    @Column(nullable = false)
    private Date date;

    @Column()
    private Date dateModif;

    @ManyToOne
    @JoinColumn(name = "idE")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "idC")
    private Controle controle;

    public Note(float note, int coef, Etudiant etu, Controle ctrl) {
        this.note = note;
        this.coef = coef;
        this.etudiant = etu;
        this.date = new Date();
        this.controle = ctrl;
    }

    public Note() {
        this.date = new Date();
    }

    public Long getId() {
        return idN;
    }

    public void setId(Long id) {
        this.idN = id;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public int getCoef() {
        return coef;
    }

    public void setCoef(int coef) {
        this.coef = coef;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Controle getControle() {
        return controle;
    }

    public void setControle(Controle controle) {
        this.controle = controle;
    }

    @Override
    public int compareTo(Note o) {
        return this.idN.compareTo(o.idN);
    }

}
