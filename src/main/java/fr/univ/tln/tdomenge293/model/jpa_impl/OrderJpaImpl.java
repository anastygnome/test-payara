package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class OrderJpaImpl implements Order {
    @Id
    UUID number;


    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false, orphanRemoval = true)
    @JoinColumn(name = "client_jpa_impl_number", nullable = false)
    private ClientJpaImpl client;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Set<OrderLineJpaImpl> orderLines = new LinkedHashSet<>();

    @Override
    public Client getClient() {
        return this.client;
    }

    @Override
    public void setClient(Client client) {
        if (client instanceof ClientJpaImpl) {
            this.client = (ClientJpaImpl) client;
        }
    }

    public void setOrderLines(Set<OrderLine> items) {
        orderLines = new HashSet<>();
        items.stream().filter(OrderLineJpaImpl.class::isInstance).forEach(ord -> orderLines.add((OrderLineJpaImpl) ord));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderJpaImpl orderJpa = (OrderJpaImpl) o;
        return getNumber() != null && Objects.equals(getNumber(), orderJpa.getNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}