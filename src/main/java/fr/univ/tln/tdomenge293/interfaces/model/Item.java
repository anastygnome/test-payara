package fr.univ.tln.tdomenge293.interfaces.model;

import java.math.BigDecimal;
import java.util.UUID;

public interface Item {
    UUID getNumber();

    void setNumber(UUID number);

    String getName();

    void setName(String name);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);
}
