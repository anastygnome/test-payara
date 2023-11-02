package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Item;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "orders_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_number", "item_number"}))
public class OrderLineJpaImpl implements OrderLine, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_number", nullable = false)
    private OrderJpaImpl order;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "item_number", nullable = false)
    private ItemJpaImpl item;
    @Min(0)
    private int quantity;

    public void setOrder(Order order) {
        if (order instanceof OrderJpaImpl orderJ) {
            this.order = orderJ;
        } else throw new IllegalArgumentException("Bad type");
    }

    @Override
    public void setItem(Item item) {
        this.item = (ItemJpaImpl) item;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> oEffectiveClass = (o instanceof HibernateProxy hibernateProxy) ?
                hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();

        Class<?> thisEffectiveClass = (this instanceof HibernateProxy hibernateProxy) ?
                hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;

        OrderLineJpaImpl orderLine = (OrderLineJpaImpl) o;

        return getId() != null && orderLine.getId() != null ? Objects.equals(getId(), orderLine.getId()) : Objects.equals(getOrder(), orderLine.getOrder());
    }

    @Override
    public final int hashCode() {
        return (this instanceof HibernateProxy hibernateProxy) ?
                hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}