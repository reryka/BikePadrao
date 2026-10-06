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
@Table(name = "produto_especificacao")
public class ProdutoEspecificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "produto_id",
            nullable = false
    )
    private Produto produto;

    @Column(
            name = "nome",
            nullable = false,
            length = 100
    )
    private String nome;

    @Column(
            name = "valor",
            nullable = false,
            length = 300
    )
    private String valor;

    @Column(
            name = "ordem",
            nullable = false
    )
    private Integer ordem = 0;

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