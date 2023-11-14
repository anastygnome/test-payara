package fr.univtln.bruno.samples.jakartaee.jpajaxrs;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@NamedQuery(name="Message.findAll",query = "select m from Message m")
@Table(name = "MESSAGE")
@NoArgsConstructor
@Getter
@ToString
@JsonPropertyOrder({ "id", "content" })
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)

public class Message {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    private Message(String content) {
        this.content = content;
    }

    @NotNull
    @Column(name = "CONTENT")
    @NotBlank
    private String content;
    public static Message createMessage(String content) {
        return new Message(content);
    }
}
