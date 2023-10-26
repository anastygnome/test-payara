package fr.univ.tln.tdomenge293.model.coll_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import lombok.Data;

import java.util.*;

@Data
public class OrderCollImpl implements Order {
    Client client;
    UUID number;
    Set<OrderLineCollImpl> orderLines = new HashSet<>();
    }
