package fr.iut2.project1.Groupe;


import fr.iut2.project1.Etudiant.Etudiant;
import fr.iut2.project1.Prof.Prof;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity implementation class for Entity: Groupe
 */
@Entity
@Table(name = "groupe")
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idG;

    @Column(unique = true, nullable = false)
    private String nom;

    @OneToMany(mappedBy = "groupe", cascade = CascadeType.PERSIST)
    private List<Etudiant> etudiants = new ArrayList<>();

    @OneToMany(mappedBy = "groupe")
    private Set<Prof> profs = new HashSet<>();

    public Groupe() {}

    public Long getId() {
        return idG;
    }

    public void setId(Long id) {
        this.idG = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom.toUpperCase();
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public Set<Prof> getProfs() {
        return profs;
    }

    public void setProfs(Set<Prof> profs) {
        this.profs = profs;
    }

    public void setProf(Prof prof) {
        this.profs.add(prof);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groupe)) return false;
        return idG != null && idG.equals(((Groupe) o).idG);
    }
}
