package fr.univ.tln.tdomenge293.interfaces.model;

import java.io.Serializable;
import java.math.BigDecimal;

public interface OrderLine extends Serializable {
     long serialVersionUID = 1L;
    Order getOrder();

    void setOrder(Order order);

    Item getItem();

    void setItem(Item item);

    int getQuantity();

    void setQuantity(int quantity);

    default BigDecimal getPrice() {
        return getItem()
                .getPrice()
                .multiply(BigDecimal.valueOf(getQuantity()));
    }
}
