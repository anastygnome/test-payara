package fr.univtln.tdomenge293.model.collections;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.univtln.tdomenge293.interfaces.ICommande;
import fr.univtln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univtln.tdomenge293.interfaces.IProduit;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LigneDeCommande implements ILigneDeCommande{

    @Min(value = 1, message = "entity.constraint.quantity")
    int quantite;

    @JsonIdentityReference(alwaysAsId = true)
    ICommande commande;

    @JsonIdentityReference(alwaysAsId = true)
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + quantite;
        result = prime * result + ((commande == null) ? 0 : commande.hashCode());
        result = prime * result + ((produit == null) ? 0 : produit.hashCode());
        return result;
    }
    }