package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.CategoriaFormDTO;
import br.com.bikepadrao.entity.Categoria;
import br.com.bikepadrao.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaAdminService {

    private final CategoriaRepository categoriaRepository;


    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {

        return categoriaRepository
                .findAllByOrderByNomeAsc();
    }


    @Transactional(readOnly = true)
    public CategoriaFormDTO buscarForm(Long id) {

        Categoria categoria =
                categoriaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Categoria não encontrada."
                                )
                        );


        CategoriaFormDTO dto =
                new CategoriaFormDTO();

        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setSlug(categoria.getSlug());
        dto.setAtivo(categoria.getAtivo());

        return dto;
    }


    @Transactional
    public Categoria criar(
            CategoriaFormDTO dto
    ) {

        validarUnicidade(dto, null);

        Categoria categoria =
                new Categoria();

        preencher(categoria, dto);

        return categoriaRepository.save(
                categoria
        );
    }


    @Transactional
    public void atualizar(
            Long id,
            CategoriaFormDTO dto
    ) {

        Categoria categoria =
                categoriaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Categoria não encontrada."
                                )
                        );

        validarUnicidade(dto, id);

        preencher(categoria, dto);
    }


    @Transactional
    public void alternarStatus(Long id) {

        Categoria categoria =
                categoriaRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Categoria não encontrada."
                                )
                        );

        categoria.setAtivo(
                !Boolean.TRUE.equals(
                        categoria.getAtivo()
                )
        );
    }


    private void preencher(
            Categoria categoria,
            CategoriaFormDTO dto
    ) {

        categoria.setNome(
                dto.getNome().trim()
        );

        categoria.setSlug(
                dto.getSlug()
                        .trim()
                        .toLowerCase()
        );

        categoria.setAtivo(
                Boolean.TRUE.equals(
                        dto.getAtivo()
                )
        );
    }


    private void validarUnicidade(
            CategoriaFormDTO dto,
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
                    categoriaRepository
                            .existsByNome(nome);

            slugExiste =
                    categoriaRepository
                            .existsBySlug(slug);

        } else {

            nomeExiste =
                    categoriaRepository
                            .existsByNomeAndIdNot(
                                    nome,
                                    id
                            );

            slugExiste =
                    categoriaRepository
                            .existsBySlugAndIdNot(
                                    slug,
                                    id
                            );
        }


        if (nomeExiste) {

            throw new IllegalArgumentException(
                    "Já existe uma categoria com esse nome."
            );
        }


        if (slugExiste) {

            throw new IllegalArgumentException(
                    "Já existe uma categoria com esse slug."
            );
        }
    }
}