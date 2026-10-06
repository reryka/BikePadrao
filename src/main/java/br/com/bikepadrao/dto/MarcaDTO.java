package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarcaDTO {

    private Long id;
    private String nome;
    private String slug;
}