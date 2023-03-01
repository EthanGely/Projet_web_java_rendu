package fr.iut2.project1.Prof;

import fr.iut2.project1.Controle.Controle;
import fr.iut2.project1.Groupe.Groupe;
import fr.iut2.project1.Groupe.GroupeDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "prof")
public class Prof implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idP;
    @Column(name = "prenom" ,nullable = false)
    private String prenom;
    @Column(name = "nom" ,nullable = false)
    private String nom;
    @Column(name = "mail" ,nullable = false)
    private String mail;
    @Column(name = "password" ,nullable = false)
    private String password;


    @ManyToOne
    @JoinColumn(name = "idG")
    private Groupe groupe;


    @JoinColumn(name = "controles")
    @OneToMany(mappedBy = "prof", cascade = CascadeType.PERSIST)
    private List<Controle> controles = new ArrayList<>();


    @Column(name = "hasConnected" ,nullable = false)
    private boolean hasConnected = false;


    @Column(name = "isAdmin" ,nullable = false)
    private boolean isAdmin = false;

    public Prof(){}

    public Prof(String prenom, String nom, String mail, String password, boolean isAdmin, Groupe groupe) {
        this.prenom = prenom;
        this.nom = nom;
        this.mail = mail;
        this.password = password;
        this.isAdmin = isAdmin;
        if (!isAdmin && groupe != null) {
            this.groupe = groupe;
        } else {
            this.groupe = null;
        }
    }


    public Long getId() {
        return idP;
    }

    public void setId(Long id) {
        this.idP = id;
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

    public Groupe getGroupe() {

        return groupe;
    }

    public void setGroupe(Groupe gr, boolean isTemp) {
        if (isAdmin) {
            this.groupe = null;
        } else {
            this.groupe = gr;
        }
        setProftoGroupes(isTemp);
    }

    private void setProftoGroupes(boolean isTemp) {
        boolean hasProf = false;
        for (Prof prof : this.groupe.getProfs()) {
            if (prof.getId().equals(this.getId())) {
                hasProf = true;
                break;
            }
        }
        if (!hasProf) {
            this.groupe.setProf(this);
        }
    }

    public List<Controle> getControles() {
        return controles;
    }

    public void setControles(List<Controle> controles) {
        this.controles = controles;
    }

    public boolean isHasConnected() {
        return hasConnected;
    }

    public void setHasConnected(boolean hasConnected) {
        this.hasConnected = hasConnected;
    }

    public Prof setConnected() {
        this.hasConnected = true;
        return this;
    }

    public Prof setDisconnected() {
        this.hasConnected = false;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }



    public void addControle(Controle ctrl) {
        this.controles.add(ctrl);
    }

    public void addAllControles(List<Controle> ctrls) {
        this.controles.addAll(ctrls);
    }
}
