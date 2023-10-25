package fr.univ.tln.tdomenge293.interfaces;

import java.math.BigDecimal;
import java.util.Collection;
public interface Order {
    Client getClient();

    void setClient(Client client);

    String getNumber();

    void setNumber(String number);

    Collection<OrderLine> getItems();

    void setItems(Collection<OrderLine> items);

    void addItem(OrderLine item);

    void removeItem(OrderLine item);

    default BigDecimal getTotalPrice() {
        return getItems().stream()
                .map(OrderLine::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}

