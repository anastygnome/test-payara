package fr.univ.tln.tdomenge293.interfaces;

import java.math.BigDecimal;

public interface Item {
    String getNumber();

    void setNumber(String number);

    String getName();

    void setName(String name);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);
}
