package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ITEMS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@NamedQuery(name = "ItemJpaImpl.findByNumber", query = "select i from ItemJpaImpl i where i.number = :number")

public class ItemJpaImpl implements Item, Serializable {
    String name;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID number;
    @DecimalMin(value = "0.0")
    @Digits(integer=6, fraction=3)
    BigDecimal price;
    @Serial
    private static final long serialVersionUID=1L;

    private ItemJpaImpl(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ItemJpaImpl of(String name, BigDecimal price) {
        return new ItemJpaImpl(name, price);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ItemJpaImpl itemJpa = (ItemJpaImpl) o;
        return getNumber() != null && Objects.equals(getNumber(), itemJpa.getNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
