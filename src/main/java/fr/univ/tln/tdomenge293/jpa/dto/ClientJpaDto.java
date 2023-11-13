package fr.univ.tln.tdomenge293.jpa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl;
import fr.univ.tln.tdomenge293.model.jpa_impl.OrderLineJpaImpl;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link fr.univ.tln.tdomenge293.model.jpa_impl.ClientJpaImpl}
 */

public record ClientJpaDto(@NotNull UUID number, @NotNull String firstName, @NotNull String lastName, @NotNull String email, Set<UUID> orders) implements Serializable {
public static ClientJpaDto fromClient(ClientJpaImpl c , Set<UUID> orders){
    return new ClientJpaDto(c.getNumber(),c.getFirstName(),c.getLastName(),c.getEmail(),orders == null ? new HashSet<>() : orders);
}
}