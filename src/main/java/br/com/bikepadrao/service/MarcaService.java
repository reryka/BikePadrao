package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.MarcaDTO;
import br.com.bikepadrao.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public List<MarcaDTO> listarAtivas() {

        return marcaRepository
                .findByAtivoTrueOrderByNomeAsc()
                .stream()
                .map(marca ->
                        new MarcaDTO(
                                marca.getId(),
                                marca.getNome(),
                                marca.getSlug()
                        )
                )
                .toList();
    }
}