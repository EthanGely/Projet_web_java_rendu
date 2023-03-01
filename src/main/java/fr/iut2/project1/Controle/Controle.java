package fr.iut2.project1.Controle;

import fr.iut2.project1.Etudiant.Etudiant;
import fr.iut2.project1.Note.Note;
import fr.iut2.project1.Prof.Prof;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "controle")
public class Controle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idC;

    @OneToMany(mappedBy = "controle", cascade = CascadeType.PERSIST)
    private List<Note> notes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idP")
    private Prof prof;

    @ManyToMany
    @JoinTable(
            name = "etudiant_controle",
            joinColumns = @JoinColumn(name = "idC"),
            inverseJoinColumns = @JoinColumn(name = "idE")
    )
    private List<Etudiant> etudiants;

    @Column(name = "intitule")
    private String intitule;


    public Controle() {}

    public Controle(Prof prof, String intitule) {
        this.prof = prof;
        this.intitule = intitule;
        prof.addControle(this);
    }

    public List<Note> getListeNotes() {
        return notes;
    }

    public void setListeNotes(List<Note> listeNotes) {
        this.notes = listeNotes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public Long getId() {
        return idC;
    }

    public void setId(Long id) {
        this.idC = id;
    }

    public float getMoyenne() {
        if (!notes.isEmpty()) {
            float sommeNotes = 0.f;
            for (Note note : notes) {
                sommeNotes += note.getNote();
            }
            return sommeNotes / notes.size();
        } else {
            return -1;
        }
    }

    public Prof getProf() {
        return this.prof;
    }

    public void setProf(Prof prof) {
        this.prof = prof;
    }

    public Date getDate() {
        if (!notes.isEmpty()) {
            return notes.get(0).getDate();
        } else {
            return null;
        }
    }

    public String getFormatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM", Locale.FRANCE);
        String date = sdf.format(getDate());
        if (!notes.isEmpty() && notes.get(0).getDateModif() != null) {
            date += " - Modifié le " + sdf.format(notes.get(0).getDateModif());
        }
        return date;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
}
