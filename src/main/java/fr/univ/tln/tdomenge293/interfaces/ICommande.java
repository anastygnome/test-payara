package fr.univ.tln.tdomenge293.interfaces;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public interface ICommande extends Serializable {
    Long getNumero();
    Date getDate();
    IClient getClient();
    List<ILigneDeCommande> getPanier();

    ICommande setNumero(Long numero);
    ICommande setDate(Date date);
    ICommande setClient(IClient client);
    ICommande setPanier(List<ILigneDeCommande> panier);

    void ajouterProduit(IProduit produit);
    void ajouterProduit(ILigneDeCommande ligneDeCommande); //permet de définir la quantité d'un produit directement
    void supprimerProduit(IProduit produit);
    void supprimerProduit(ILigneDeCommande ligneDeCommande);
    void viderPanier();
    ICommande commander();
}
