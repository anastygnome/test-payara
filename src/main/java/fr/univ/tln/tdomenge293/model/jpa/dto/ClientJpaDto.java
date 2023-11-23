package fr.univ.tln.tdomenge293.model.jpa.dto;

import fr.univ.tln.tdomenge293.model.jpa.Client;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link fr.univ.tln.tdomenge293.model.jpa.Client}
 */

public record ClientJpaDto(@NotNull Long number, @NotNull String firstName, @NotNull String lastName,
                           @NotNull String email, Set<Long> orders) implements Serializable {
    public static ClientJpaDto fromClient(Client c, Set<Long> orders) {
        return new ClientJpaDto(c.getNumero(), c.getPrenom(), c.getNom(), c.getEmail(), orders == null ? Set.of() : orders);
    }
}