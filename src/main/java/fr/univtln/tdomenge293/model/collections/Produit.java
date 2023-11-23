package fr.univtln.tdomenge293.model.collections;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import fr.univtln.tdomenge293.interfaces.IProduit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class Produit implements IProduit, Comparable<IProduit>{
    static Long cpt = 0L;

    Long code;
    String name;
    int prixUnit;
    
    //#region CONSTRUCTOR & FACTORY
    private Produit(Long code, String name, int prixUnit) {
        this.code = code;
        this.name = name;
        this.prixUnit = prixUnit;
    }

    private Produit(String name, int prixUnit) {
        this(cpt, name, prixUnit);
        cpt++;
    }

    public static Produit of(Long code, String name, int prixUnit) { return new Produit(code, name, prixUnit); }
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
    public int hashCode() {
        final int prime = 31;
        long result = 1;
        result = prime * result + code;
        return (int) result;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Produit p) return code == p.getCode();
        return false;
    }
    //#endregion    

    @Override
    public int compareTo(IProduit o) {return Long.compare(code,o.getCode());}

    @Override
    public String toString() {
        return "Produit [code=" + code + ", name=" + name + ", prixUnit=" + prixUnit + "]";
    }
}
