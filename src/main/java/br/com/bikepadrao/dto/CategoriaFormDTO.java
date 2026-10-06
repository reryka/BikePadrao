package br.com.bikepadrao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoriaFormDTO {

    private Long id;

    @NotBlank(message = "Informe o nome.")
    private String nome;

    @NotBlank(message = "Informe o slug.")
    private String slug;

    private Boolean ativo = true;
}