package fr.univ.tln.tdomenge293.interfaces;

import java.io.Serializable;

public interface IProduit extends Serializable {

    IProduit creerProduit(String name, int prixUnit);

    Long getCode();
    String getName();
    int getPrixUnit();

    IProduit setCode(Long code);
    IProduit setName(String name);
    IProduit setPrixUnit(int prixUnit);
}
