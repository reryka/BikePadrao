package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;
    private String nome;
    private String slug;
}