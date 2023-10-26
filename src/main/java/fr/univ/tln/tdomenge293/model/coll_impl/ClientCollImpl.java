package fr.univ.tln.tdomenge293.model.coll_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import lombok.Data;

import java.util.UUID;

@Data(staticConstructor = "of")

public class ClientCollImpl implements Client {
    UUID number;
    String firstName;
    String lastName;
    String email;
}
