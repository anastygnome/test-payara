package fr.univ.tln.tdomenge293.interfaces.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public interface Item extends Serializable {
     long serialVersionUID = 1L;
    UUID getNumber();

    void setNumber(UUID number);

    String getName();

    void setName(String name);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);
}
