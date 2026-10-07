package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioCategoriaDTO {

    private Long categoriaId;

    private String categoria;

    private Long quantidadeProdutos;
}