package fr.univtln.tdomenge293.model.collections;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univtln.tdomenge293.interfaces.IClient;
import fr.univtln.tdomenge293.interfaces.ICommande;
import fr.univtln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univtln.tdomenge293.interfaces.IProduit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "numero")
public class Commande implements ICommande, Comparable<Commande>{
    static Long cpt = 0L;

    Long numero;
    Date date;

    @JsonIdentityReference(alwaysAsId = true)
    IClient client;

    ArrayList<ILigneDeCommande> panier;

    //#region CONSTRUCTOR & FACTORY
    private Commande(Long numero, Date date, List<ILigneDeCommande> panier) {
        this.numero = numero;
        this.date = date;
        this.panier = new ArrayList<>(panier);
    }

    private Commande(Date date, List<ILigneDeCommande> panier) {
        this(cpt, date, panier);
        cpt++;
    }

    private Commande(List<ILigneDeCommande> panier) {
        this(Date.valueOf(LocalDate.now()), panier);
    }

    private Commande(Date date) {
        this(date, new ArrayList<>());
    }

    private Commande() {
        this(new ArrayList<>());
    }

    public static Commande of(Long numero, Date date, List<ILigneDeCommande> panier) {
        return new Commande(numero, date, panier);
    }
    public static Commande of(Date date, List<ILigneDeCommande> panier) { return new Commande(date, panier); }
    public static Commande of(List<ILigneDeCommande> panier) { return new Commande(panier); }
    public static Commande of(Date date) { return new Commande(date); }
    public static Commande of() { return new Commande(); }
    //#endregion

    @Override
    public ICommande creerCommande() { return Commande.of(); }
    @Override
    public ICommande creerCommande(Date date) { return Commande.of(date); }
    @Override
    public ICommande creerCommande(List<ILigneDeCommande> panier) { return Commande.of(panier); }

    //#region GETTER & SETTER
    @Override
    public Long getNumero() {
        return numero;
    }
    @Override
    public Date getDate() { return date; }
    @Override
    public IClient getClient() { return client; }
    @Override
    public List<ILigneDeCommande> getPanier() { return panier; }

    @Override
    public ICommande setNumero(Long numero) {
        this.numero = numero;
        return this;
    }
    @Override
    public ICommande setDate(Date date) { this.date = date; return this; }
    @Override
    public ICommande setClient(IClient client) { this.client = client; return this; }
    @Override
    public ICommande setPanier(List<ILigneDeCommande> panier) { this.panier = new ArrayList<>(panier); return this; }
    //#endregion

    //#region PANIER
    @Override
    public void ajouterProduit(IProduit produit) {
        //ajouter 1 produit si il est déjà dans le panier
        List<ILigneDeCommande> ligneDeCommandes = panier.stream().filter(x -> x.getProduit().equals(produit)).toList();
        if (!ligneDeCommandes.isEmpty()) {
            ligneDeCommandes.forEach(ILigneDeCommande::addOneQuantite);
            return;
        }
        
        //sinon on l'ajoute au panier
        panier.add( LigneDeCommande.of(1, this, produit) );
    }

    @Override
    public void ajouterProduit(ILigneDeCommande ligneDeCommande) { panier.add(ligneDeCommande); }

    @Override
    public void supprimerProduit(IProduit produit) {
        List<ILigneDeCommande> ligneDeCommandes = panier.stream().filter(x -> x.getProduit().equals(produit)).toList();
        ligneDeCommandes.forEach(x -> panier.remove(x));
    }

    @Override
    public void supprimerProduit(ILigneDeCommande ligneDeCommande) { panier.remove(ligneDeCommande); }
    
    @Override
    public void viderPanier() { panier.clear(); }
    //#endregion

    @Override
    public ICommande commander() { 
        return this;
    }

    //#region EQUALS & HASHCODE
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Commande c) return Objects.equals(getNumero(), c.getNumero());

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumero());
    }
    //#endregion

    @Override
    public int compareTo(Commande o) {
        return Long.compare(getNumero(), o.getNumero());
    }

    @Override
    public String toString() {
        return "Commande [numero=" + numero + ", date=" + date + (client != null ? ", client=" + client.getEmail() : "") + ", panier=" + panier + "]";
    }
}
