package fr.univtln.tdomenge293.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import fr.univtln.tdomenge293.interfaces.IClient;
import fr.univtln.tdomenge293.interfaces.ICommande;
import fr.univtln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univtln.tdomenge293.interfaces.IProduit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "numero")
@Table(name = "commande")
@Entity
public class Commande implements ICommande, Comparable<Commande>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long numero;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonDeserialize(using = DateDeserializer.class)
    @Column(name = "date_commande")
    Date date;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "commande", fetch = FetchType.EAGER)
    List<LigneDeCommande> panier;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne (targetEntity = Client.class, cascade = CascadeType.ALL)
    IClient client;

    //#region CONSTRUCTOR & FACTORY
    private Commande(Date date, List<ILigneDeCommande> panier) {
        this.date = date;
        this.panier = panier.stream().map(c -> (LigneDeCommande)c).collect(Collectors.toCollection(ArrayList::new));
    }

    private Commande(List<ILigneDeCommande> panier) {
        this(Date.valueOf(LocalDate.now()), panier);
    }

    private Commande(Date date) {
        this(date, new ArrayList<>());
    }

    public Commande() {
        this(new ArrayList<>());
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
    public List<ILigneDeCommande> getPanier() { return panier.stream().map(c -> (ILigneDeCommande)c).toList(); }

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
    public ICommande setPanier(List<ILigneDeCommande> panier) { this.panier = panier.stream().map(c -> (LigneDeCommande)c).toList(); return this; }
    //#endregion

    //#region PANIER
    @Override
    public void ajouterProduit(IProduit produit) {
        //ajouter 1 produit si il est déjà dans le panier
        List<ILigneDeCommande> ligneDeCommandes = panier.stream().filter(x -> x.getProduit().equals(produit)).collect(Collectors.toCollection(ArrayList::new));
        if (!ligneDeCommandes.isEmpty()) {
            ligneDeCommandes.forEach(ILigneDeCommande::addOneQuantite);
            return;
        }
        
        //sinon on l'ajoute au panier
        panier.add( (LigneDeCommande) LigneDeCommande.of(1, this, produit) );
    }

    @Override
    public void ajouterProduit(ILigneDeCommande ligneDeCommande) { panier.add( (LigneDeCommande) ligneDeCommande); }

    @Override
    public void supprimerProduit(IProduit produit) {
        List<ILigneDeCommande> ligneDeCommandes = panier.stream().filter(x -> x.getProduit().equals(produit)).collect(Collectors.toCollection(ArrayList::new));
        ligneDeCommandes.forEach(x -> panier.remove(x));
    }

    @Override
    public void supprimerProduit(ILigneDeCommande ligneDeCommande) { panier.remove(ligneDeCommande); }
    
    @Override
    public void viderPanier() { panier.clear(); }
    //#endregion

    @Override
    public ICommande commander() { 
        panier = Collections.unmodifiableList(panier); //rend le panier immutable
        return this;
    }

    //#region EQUALS & HASHCODE
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Commande commande = (Commande) o;
        return getNumero() != null && Objects.equals(getNumero(), commande.getNumero());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
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
