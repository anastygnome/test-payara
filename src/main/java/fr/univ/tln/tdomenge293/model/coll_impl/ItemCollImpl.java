package fr.univ.tln.tdomenge293.model.coll_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Item;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data(staticConstructor = "of")
public class ItemCollImpl implements Item {
    String name;
    UUID number;
    BigDecimal price;
}
