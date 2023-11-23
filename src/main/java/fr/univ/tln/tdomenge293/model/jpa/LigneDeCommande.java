package fr.univ.tln.tdomenge293.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.univ.tln.tdomenge293.interfaces.ICommande;
import fr.univ.tln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univ.tln.tdomenge293.interfaces.IProduit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "ligne_de_commande", uniqueConstraints = @UniqueConstraint(columnNames = {"produit_code", "commande_num"})
)
@Entity
public class LigneDeCommande implements ILigneDeCommande, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    Long id;

    @Min(value = 0, message = "entity.constraint.quantity")
    int quantite;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Commande.class)
    @JoinColumn(name = "commande_num")
    ICommande commande;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Produit.class)
    @JoinColumn(name = "produit_code")
    IProduit produit;

    private LigneDeCommande(int quantite, ICommande commande, IProduit produit) {
        this.quantite = quantite;
        this.commande = commande;
        this.produit = produit;
    }

    public static ILigneDeCommande of(int quantite, ICommande commande, IProduit produit) { return new LigneDeCommande(quantite, commande, produit); }

    @Override
    public ILigneDeCommande creerLigneDeCommande(int quantite, ICommande commande, IProduit produit) {
        return of(quantite, commande, produit);
    }

    //#region GETTER & SETTER
    @Override
    public int getQuantite() { return quantite; }
    @Override
    public ICommande getCommande() { return commande; }
    @Override
    public IProduit getProduit() { return produit; }

    @Override
    public ILigneDeCommande setQuantite(int quantite) { this.quantite = quantite; return this; }
    @Override
    public ILigneDeCommande setCommande(ICommande commande) { this.commande = commande; return this; }
    @Override
    public ILigneDeCommande setProduit(IProduit produit) { this.produit = produit; return this; }
    //#endregion

    @Override
    public void addOneQuantite() { this.quantite++; }

    @Override
    public void removeOneQuantite() {
        if (quantite > 0) this.quantite--;
    }

    //#region EQUALS & HASHCODE
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Commande command = (Commande) o;
        return getId() != null && Objects.equals(getId(), command.getNumero());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    //#endregion


    @Override
    public String toString() {
        return "LigneDeCommande [quantite=" + quantite + ", commande num=" + commande.getNumero() + ", produit code=" + produit.getCode() + "]";
    }
}
