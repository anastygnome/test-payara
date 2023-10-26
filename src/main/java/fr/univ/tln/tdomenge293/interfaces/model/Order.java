package fr.univ.tln.tdomenge293.interfaces.model;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public interface Order {
    Client getClient();

    void setClient(Client client);

    UUID getNumber();

    void setNumber(UUID number);


    Set<? extends OrderLine> getOrderLines();

    default BigDecimal getTotalPrice() {
        return getOrderLines().stream()
                .map(OrderLine::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
