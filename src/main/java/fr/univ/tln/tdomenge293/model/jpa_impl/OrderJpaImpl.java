package fr.univ.tln.tdomenge293.model.jpa_impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.interfaces.model.Order;
import fr.univ.tln.tdomenge293.interfaces.model.OrderLine;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ORDERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "number")
public class OrderJpaImpl implements Order, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID number;
    @FutureOrPresent
    @CreationTimestamp(source = SourceType.DB)
    @Column( nullable = false, updatable = false, insertable = false)
    LocalDate date;
    @Digits(integer=6, fraction=3)
    BigDecimal price = BigDecimal.ZERO;
    @Length(min = 3,max = 3)
    String currency ="EUR";
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "client_number", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private ClientJpaImpl client;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<OrderLineJpaImpl> orderLines = new LinkedHashSet<>();
   @Serial
    private static final long serialVersionUID=1;

    private OrderJpaImpl( BigDecimal price, ClientJpaImpl client) {
        this.price = price;
        this.client = client;
        client.getOrders().add(this);
        this.orderLines = new HashSet<>();
    }

    public static OrderJpaImpl of( BigDecimal price, ClientJpaImpl client) {
        return new OrderJpaImpl(price, client);
    }

    @Override
    public Client getClient() {
        return this.client;
    }

    @Override
    public void setClient(Client client) {
        if (client instanceof ClientJpaImpl clientJl) {
            this.client = clientJl;
        }
    }
public void addLine(OrderLineJpaImpl line) {
        line.setOrder(this);
        getOrderLines().add(line);
        setPrice(getPrice().add(line.getPrice()));
}

    @Override
    public BigDecimal getTotalPrice() {
        return getPrice();
    }

    public void setOrderLines(Set<OrderLine> items) {
        orderLines = new HashSet<>();
        items.stream().filter(OrderLineJpaImpl.class::isInstance).forEach(ord -> orderLines.add((OrderLineJpaImpl) ord));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderJpaImpl orderJpa = (OrderJpaImpl) o;
        return getNumber() != null && Objects.equals(getNumber(), orderJpa.getNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}