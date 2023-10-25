package fr.univ.tln.tdomenge293.interfaces;

import java.math.BigDecimal;

public interface OrderLine {
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
