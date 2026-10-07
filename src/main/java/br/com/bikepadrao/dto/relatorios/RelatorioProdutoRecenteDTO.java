package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class RelatorioProdutoRecenteDTO {

    private Long id;

    private String nome;

    private String sku;

    private String categoria;

    private String marca;

    private Boolean ativo;

    private OffsetDateTime dataCriacao;
}