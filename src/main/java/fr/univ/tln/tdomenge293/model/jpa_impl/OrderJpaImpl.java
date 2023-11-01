package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ORDERS")

public class OrderJpaImpl implements Order, Serializable {
    @Id
    UUID number;
    @CreationTimestamp(source = SourceType.DB)
    @Column( nullable = false, updatable = false, insertable = false)
    LocalDate date;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false, orphanRemoval = true)
    @JoinColumn(name = "client_number", nullable = false)
    private ClientJpaImpl client;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Set<OrderLineJpaImpl> orderLines = new LinkedHashSet<>();
    private static final long serialVersionUID=1;

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