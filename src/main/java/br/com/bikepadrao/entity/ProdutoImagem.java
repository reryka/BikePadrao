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
@Table(name = "produto_imagem")
public class ProdutoImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "ordem", nullable = false)
    private Integer ordem = 0;

    @Column(name = "principal", nullable = false)
    private Boolean principal = false;

    @Column(name = "data_criacao", nullable = false)
    private OffsetDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = OffsetDateTime.now();
    }
}