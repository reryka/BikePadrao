package br.com.bikepadrao.repository;

import br.com.bikepadrao.entity.ProdutoImagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoImagemRepository
        extends JpaRepository<ProdutoImagem, Long> {


    @Query("""
            SELECT i
            FROM ProdutoImagem i
            WHERE i.produto.id IN :produtoIds
              AND i.principal = true
            ORDER BY i.ordem
            """)
    List<ProdutoImagem> buscarPrincipaisPorProdutoIds(
            @Param("produtoIds")
            List<Long> produtoIds
    );

    List<ProdutoImagem> findByProdutoIdOrderByOrdemAsc(
            Long produtoId
    );
}