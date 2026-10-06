package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProdutoEspecificacaoDTO {

    private Long id;

    private String nome;

    private String valor;

    private Integer ordem;
}