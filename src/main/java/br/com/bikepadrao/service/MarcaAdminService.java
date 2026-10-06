package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.MarcaFormDTO;
import br.com.bikepadrao.entity.Marca;
import br.com.bikepadrao.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaAdminService {

    private final MarcaRepository marcaRepository;


    @Transactional(readOnly = true)
    public List<Marca> listarTodas() {

        return marcaRepository
                .findAllByOrderByNomeAsc();
    }


    @Transactional(readOnly = true)
    public MarcaFormDTO buscarForm(Long id) {

        Marca marca =
                marcaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Marca não encontrada."
                                )
                        );


        MarcaFormDTO dto =
                new MarcaFormDTO();

        dto.setId(marca.getId());
        dto.setNome(marca.getNome());
        dto.setSlug(marca.getSlug());
        dto.setAtivo(marca.getAtivo());

        return dto;
    }


    @Transactional
    public Marca criar(
            MarcaFormDTO dto
    ) {

        validarUnicidade(dto, null);

        Marca marca = new Marca();

        preencher(marca, dto);

        return marcaRepository.save(marca);
    }


    @Transactional
    public void atualizar(
            Long id,
            MarcaFormDTO dto
    ) {

        Marca marca =
                marcaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Marca não encontrada."
                                )
                        );

        validarUnicidade(dto, id);

        preencher(marca, dto);
    }


    @Transactional
    public void alternarStatus(Long id) {

        Marca marca =
                marcaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Marca não encontrada."
                                )
                        );

        marca.setAtivo(
                !Boolean.TRUE.equals(
                        marca.getAtivo()
                )
        );
    }


    private void preencher(
            Marca marca,
            MarcaFormDTO dto
    ) {

        marca.setNome(
                dto.getNome().trim()
        );

        marca.setSlug(
                dto.getSlug()
                        .trim()
                        .toLowerCase()
        );

        marca.setAtivo(
                Boolean.TRUE.equals(
                        dto.getAtivo()
                )
        );
    }


    private void validarUnicidade(
            MarcaFormDTO dto,
            Long id
    ) {

        String nome =
                dto.getNome().trim();

        String slug =
                dto.getSlug()
                        .trim()
                        .toLowerCase();


        boolean nomeExiste;

        boolean slugExiste;


        if (id == null) {

            nomeExiste =
                    marcaRepository
                            .existsByNome(nome);

            slugExiste =
                    marcaRepository
                            .existsBySlug(slug);

        } else {

            nomeExiste =
                    marcaRepository
                            .existsByNomeAndIdNot(
                                    nome,
                                    id
                            );

            slugExiste =
                    marcaRepository
                            .existsBySlugAndIdNot(
                                    slug,
                                    id
                            );
        }


        if (nomeExiste) {

            throw new IllegalArgumentException(
                    "Já existe uma marca com esse nome."
            );
        }


        if (slugExiste) {

            throw new IllegalArgumentException(
                    "Já existe uma marca com esse slug."
            );
        }
    }
}