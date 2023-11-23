package fr.univtln.tdomenge293.interfaces;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("java:S125") //enlève le warning du code commenté, wip
public interface IClient extends Serializable {
    
    //méthode pour la création depuis api
    IClient creerClient(String nom, String prenom, String email);
    IClient creerClient(String nom, String prenom, String email, List<ICommande> commandes);
    // WIP: peut être utiliser cette méthode aussi IClient supprimerClient(String email);

    String getNom();
    String getPrenom();
    String getEmail();
    List<ICommande> getCommandes();

    IClient setNom(String nom);
    IClient setPrenom(String prenom);
    IClient setEmail(String email);
    IClient setCommandes(List<ICommande> commandes);

    void ajouterCommande(ICommande commande);
    ICommande passerCommande(List<ILigneDeCommande> commandes);
    
    ICommande annulerCommande(ICommande commande);
}
