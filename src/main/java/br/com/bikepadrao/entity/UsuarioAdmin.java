package br.com.bikepadrao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario_admin")
public class UsuarioAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "username",
            nullable = false,
            length = 100,
            unique = true
    )
    private String username;

    @Column(
            name = "senha",
            nullable = false,
            length = 255
    )
    private String senha;

    @Column(
            name = "role",
            nullable = false,
            length = 30
    )
    private String role = "ADMIN";

    @Column(
            name = "ativo",
            nullable = false
    )
    private Boolean ativo = true;

    @Column(
            name = "data_criacao",
            nullable = false
    )
    private OffsetDateTime dataCriacao;

    @PrePersist
    public void prePersist() {

        if (dataCriacao == null) {
            dataCriacao = OffsetDateTime.now();
        }
    }
}