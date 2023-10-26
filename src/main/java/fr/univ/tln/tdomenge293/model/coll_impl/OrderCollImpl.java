package fr.univ.tln.tdomenge293.model.coll_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderCollImpl implements Order {
    Client client;
    UUID number;
    Set<OrderLineCollImpl> orderLines = new HashSet<>();
}
