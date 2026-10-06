package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.CategoriaDTO;
import br.com.bikepadrao.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> listarAtivas() {

        return categoriaRepository
                .findByAtivoTrueOrderByNomeAsc()
                .stream()
                .map(categoria ->
                        new CategoriaDTO(
                                categoria.getId(),
                                categoria.getNome(),
                                categoria.getSlug()
                        )
                )
                .toList();
    }
}