package fr.univ.tln.tdomenge293.model.jpa_impl;

import fr.univ.tln.tdomenge293.interfaces.model.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "CLIENTS")
public class ClientJpaImpl implements Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID number;
    @NotNull String firstName;
    @NotNull String lastName;
    @NotNull @Email
    @Column(unique = true)
    String email;
    private static final long serialVersionUID=1;

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
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ClientJpaImpl clientJpa = (ClientJpaImpl) o;
        if (getNumber() == null || clientJpa.getNumber() == null) {
            return Objects.equals(getEmail(), clientJpa.getEmail()) && Objects.equals(getFirstName(), clientJpa.getFirstName()) && Objects.equals(getLastName(), clientJpa.getLastName());
        } else return Objects.equals(getNumber(), clientJpa.getNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
