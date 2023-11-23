package fr.univ.tln.tdomenge293.model.collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univ.tln.tdomenge293.interfaces.IClient;
import fr.univ.tln.tdomenge293.interfaces.ICommande;
import fr.univ.tln.tdomenge293.interfaces.ILigneDeCommande;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Alternative
public class Client implements IClient {

    String nom;
    String prenom;

    @Email(message = "entity.constraint.email")
    String email;

    List<ICommande> commandes;

    private Client(String nom, String prenom, String email, List<ICommande> commandes) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.commandes = commandes;
    }

    @Inject
    private Client(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.commandes = new ArrayList<>();
    }


    public static Client of(String nom, String prenom, String email) {
        return new Client(nom, prenom, email);
    }
    public static IClient of(String nom, String prenom, String email, List<ICommande> commandes) { return new Client(nom, prenom, email, commandes); }
    @JsonCreator
    public static IClient jof(String nom, String prenom, String email, List<Commande> commandes) {
    List<ICommande> cmd = new ArrayList<>(commandes.size());
    cmd.addAll(commandes);
    return new Client(nom, prenom, email, cmd); }
    @Override
    public IClient creerClient(String nom, String prenom, String email) { return Client.of(nom, prenom, email); }

    public IClient creerClient(String nom, String prenom, String email, List<ICommande> commandes) { return Client.of(nom, prenom, email, new ArrayList<>()); }

    //#region GETTER & SETTER
    @Override
    public String getNom() { return nom; }
    @Override
    public String getPrenom() { return prenom; }
    @Override
    public String getEmail() { return email; }
    @Override
    public List<ICommande> getCommandes() { return commandes; }

    @Override
    public IClient setNom(String nom) { this.nom = nom; return this; }
    @Override
    public IClient setPrenom(String prenom) { this.prenom = prenom; return this; }
    @Override
    public IClient setEmail(String email) { this.email = email; return this; }
    @Override
    public IClient setCommandes(List<ICommande> commandes) { this.commandes = commandes; return this; }
    //#endregion    

    @Override
    public ICommande passerCommande(List<ILigneDeCommande> panier) {
        ICommande c = Commande.of(panier);
        commandes.add(c);
        return c;
    }
    
    @Override
    public void ajouterCommande(ICommande commande) { commandes.add(commande); }

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
}
