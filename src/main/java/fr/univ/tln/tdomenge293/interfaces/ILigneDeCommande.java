package fr.univ.tln.tdomenge293.interfaces;

import java.io.Serializable;

public interface ILigneDeCommande extends Serializable {

    ILigneDeCommande creerLigneDeCommande(int quantite, ICommande commande, IProduit produit);

    int getQuantite();
    default int getPrix() { return getQuantite() * getProduit().getPrixUnit(); }
    ICommande getCommande();
    IProduit getProduit();

    ILigneDeCommande setQuantite(int quantite);
    ILigneDeCommande setCommande(ICommande commande);
    ILigneDeCommande setProduit(IProduit produit);

    void addOneQuantite();
    void removeOneQuantite();
}
