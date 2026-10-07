package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioProdutoIncompletoDTO {

    private Long id;

    private String nome;

    private String sku;

    private String categoria;

    private String marca;

    private Boolean ativo;
}