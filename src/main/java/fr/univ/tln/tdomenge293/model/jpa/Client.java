package fr.univ.tln.tdomenge293.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univ.tln.tdomenge293.interfaces.IClient;
import fr.univ.tln.tdomenge293.interfaces.ICommande;
import fr.univ.tln.tdomenge293.interfaces.ILigneDeCommande;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
@NoArgsConstructor
@Table(name = "client")
@Entity
@Named("JPA")
public class Client implements IClient{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    Long numero;
    String nom;
    String prenom;

    @Email(message = "entity.constraint.email")
    @NotNull
    String email;

    @OneToMany(cascade ={CascadeType.ALL}, mappedBy = "client", fetch = FetchType.EAGER)
    List<Commande> commandes;

    private Client(String nom, String prenom, String email, List<ICommande> commandes) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.commandes = commandes.stream().map(c -> (Commande)c).collect(Collectors.toCollection(ArrayList::new));
    }

    public static IClient of(String nom, String prenom, String email) { return new Client(nom, prenom, email, new ArrayList<>()); }
    public static IClient of(String nom, String prenom, String email, List<ICommande> commandes) { return new Client(nom, prenom, email, commandes); }


    @Override
    public IClient creerClient(String nom, String prenom, String email) { return of(nom, prenom, email); }
    @Override
    public IClient creerClient(String nom, String prenom, String email, List<ICommande> commandes) { return of(nom, prenom, email, commandes); }

    //#region GETTER & SETTER
    @Override
    public String getNom() { return nom; }
    @Override
    public String getPrenom() { return prenom; }
    @Override
    public String getEmail() { return email; }
    @Override
    public List<ICommande> getCommandes() { return commandes.stream().map(c -> (ICommande)c).collect(Collectors.toCollection(ArrayList::new)); }

    @Override
    public IClient setNom(String nom) { this.nom = nom; return this; }
    @Override
    public IClient setPrenom(String prenom) { this.prenom = prenom; return this; }
    @Override
    public IClient setEmail(String email) { this.email = email; return this; }
    @Override
    public IClient setCommandes(List<ICommande> commandes) { this.commandes = commandes.stream().map(c -> (Commande)c).collect(Collectors.toCollection(ArrayList::new)); return this; }
    //#endregion

    @Override
    public ICommande passerCommande(List<ILigneDeCommande> panier) {
        Commande c = Commande.of();
        panier.forEach(c::ajouterProduit);
        commandes.add(c);
        return c;
    }
    
    @Override
    public void ajouterCommande(ICommande commande) { commandes.add((Commande) commande); }

    @Override
    public ICommande annulerCommande(ICommande commande) {
        commandes.remove(commande);
        return commande;
    }

    //#region EQUALS & HASHCODE
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Client c) return email.equals( c.getEmail() );

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }
    //#endregion

    @Override
    public String toString() {
        return "Client [nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", commandes=" + commandes + "]";
    }

    public void setNumero(Long id) {
        this.numero = id;
    }
}
