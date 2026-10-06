package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProdutoImagemDTO {

    private Long id;

    private String url;

    private Integer ordem;

    private Boolean principal;
}