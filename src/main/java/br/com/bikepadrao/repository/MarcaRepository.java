package br.com.bikepadrao.repository;

import br.com.bikepadrao.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository
        extends JpaRepository<Marca, Long> {

    List<Marca> findByAtivoTrueOrderByNomeAsc();

    Optional<Marca> findBySlugAndAtivoTrue(
            String slug
    );

    boolean existsBySlug(String slug);

    List<Marca> findAllByOrderByNomeAsc();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(
            String nome,
            Long id
    );

    boolean existsBySlugAndIdNot(
            String slug,
            Long id
    );
}