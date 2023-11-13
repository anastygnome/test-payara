package fr.univ.tln.tdomenge293.model.jpa_impl;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.univ.tln.tdomenge293.interfaces.model.Client;
import fr.univ.tln.tdomenge293.utils.ExtendedEmailValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

    @Getter
    @Setter
    @ToString
    @RequiredArgsConstructor
    @Entity
    @Table(name = "CLIENTS")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "number")
    public class ClientJpaImpl implements Client {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Access(AccessType.PROPERTY)
        private UUID number;
        @NotNull String firstName;
        @NotNull String lastName;
        @NotNull
        @ExtendedEmailValidator
        @Column(unique = true)
       private String email;
        @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
        @ToString.Exclude
        @JsonIgnore
        private Set<OrderJpaImpl> orders = new HashSet<>();
        @Serial
        private static final long serialVersionUID = 1;

        private ClientJpaImpl(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public static ClientJpaImpl of(String firstName, String lastName, String email) {
            return new ClientJpaImpl(firstName, lastName, email);
        }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = (o instanceof HibernateProxy hibernateProxy) ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy hibernateProxy) ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ClientJpaImpl clientJpa = (ClientJpaImpl) o;
        return getNumber() != null && clientJpa.getNumber() != null ?  Objects.equals(getNumber(), clientJpa.getNumber()) : Objects.equals(getEmail(),clientJpa.getEmail());
    }

    @Override
    public final int hashCode() {
        return (this instanceof HibernateProxy hibernateProxy) ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
