package fr.univ.tln.tdomenge293.model.coll_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Item;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import lombok.Data;

@Data
public class OrderLineCollImpl implements OrderLine {
    Item item;
    OrderCollImpl order;
    int quantity;

    @Override
    public void setOrder(Order order) {
        if (order instanceof OrderCollImpl) {
            this.order = (OrderCollImpl) order;
        } else throw new IllegalArgumentException("Bad type");
    }
}
