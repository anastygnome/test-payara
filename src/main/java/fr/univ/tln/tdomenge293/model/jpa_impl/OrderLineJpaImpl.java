package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Item;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class OrderLineJpaImpl implements OrderLine {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_number", nullable = false)
    OrderJpaImpl order;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_number", nullable = false)
    ItemJpaImpl item;
    int quantity;

    public void setOrder(Order order) {
        if (order instanceof OrderJpaImpl) {
            this.order = (OrderJpaImpl) order;
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
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderLineJpaImpl orderLine = (OrderLineJpaImpl) o;
        return getOrder() != null && Objects.equals(getOrder(), orderLine.getOrder());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
