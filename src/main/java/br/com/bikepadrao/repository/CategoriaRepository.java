package br.com.bikepadrao.repository;

import br.com.bikepadrao.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByAtivoTrueOrderByNomeAsc();

    Optional<Categoria> findBySlugAndAtivoTrue(String slug);

    boolean existsBySlug(String slug);

    List<Categoria> findAllByOrderByNomeAsc();

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