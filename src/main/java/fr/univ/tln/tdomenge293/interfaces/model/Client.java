package fr.univ.tln.tdomenge293.interfaces.model;

import java.util.UUID;

public interface Client {
    UUID getNumber();

    void setNumber(UUID number);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);
}