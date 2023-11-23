package fr.univ.tln.tdomenge293.model.jpa;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univ.tln.tdomenge293.interfaces.IProduit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
@NoArgsConstructor
@Table(name = "produit")
@Entity
public class Produit implements IProduit, Comparable<IProduit>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long code;

    @Column(name = "nom")
    String name;

    @Column(name = "prix")
    int prixUnit;
    
    //#region CONSTRUCTOR & FACTORY
    private Produit(String name, int prixUnit) {
        this.name = name;
        this.prixUnit = prixUnit;
    }

    public static Produit of(String name, int prixUnit) { return new Produit(name, prixUnit); }

    @Override
    public IProduit creerProduit(String name, int prixUnit) { return of(name, prixUnit); }
    //#endregion

    //#region GETTER
    @Override
    public Long getCode() { return code; }
    @Override
    public String getName() { return name; }
    @Override
    public int getPrixUnit() { return prixUnit; }
    //#endregion
    
    //#region SETTER
    @Override
    public IProduit setCode(Long code) { this.code = code; return this; }
    @Override
    public IProduit setName(String name) { this.name = name; return this; }
    @Override
    public IProduit setPrixUnit(int prixUnit) { this.prixUnit = prixUnit; return this; }
    //#endregion

    //#region EQUALS & HASHCODE
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Commande commande = (Commande) o;
        return getCode() != null && Objects.equals(getCode(), commande.getNumero());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    //#endregion


    @Override
    public int compareTo(IProduit iProduit) {
        return Long.compare(this.getCode(),iProduit.getCode());
    }

    @Override
    public String toString() {
        return "Produit [code=" + code + ", name=" + name + ", prixUnit=" + prixUnit + "]";
    }
}
