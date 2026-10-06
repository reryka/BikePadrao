package br.com.bikepadrao.repository;

import br.com.bikepadrao.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
        SELECT p
        FROM Produto p
        JOIN FETCH p.categoria c
        LEFT JOIN FETCH p.marca m
        WHERE p.ativo = true
          AND c.ativo = true
          AND (
                :busca = ''
                OR LOWER(p.nome) LIKE CONCAT('%', :busca, '%')
                OR LOWER(p.sku) LIKE CONCAT('%', :busca, '%')
              )
          AND (
                :categoria = ''
                OR c.slug = :categoria
              )
          AND (
                :marca = ''
                OR (
                    m.slug = :marca
                    AND m.ativo = true
                )
              )
        ORDER BY

            CASE
                WHEN :ordem = 'nome'
                THEN LOWER(p.nome)
            END ASC,

            CASE
                WHEN :ordem = 'menor-preco'
                THEN COALESCE(
                    p.precoPromocional,
                    p.preco
                )
            END ASC,

            CASE
                WHEN :ordem = 'maior-preco'
                THEN COALESCE(
                    p.precoPromocional,
                    p.preco
                )
            END DESC,

            CASE
                WHEN :ordem = 'recentes'
                THEN p.dataCriacao
            END DESC,

            p.nome ASC
        """)
    List<Produto> buscarCatalogo(
            @Param("busca") String busca,
            @Param("categoria") String categoria,
            @Param("marca") String marca,
            @Param("ordem") String ordem
    );


    @Query("""
        SELECT p
        FROM Produto p
        JOIN FETCH p.categoria c
        LEFT JOIN FETCH p.marca m
        WHERE p.slug = :slug
          AND p.ativo = true
          AND c.ativo = true
        """)
    Optional<Produto> buscarDetalhePorSlug(
            @Param("slug") String slug
    );


    @Query("""
            SELECT p
            FROM Produto p
            JOIN FETCH p.categoria c
            LEFT JOIN FETCH p.marca m
            WHERE p.destaque = true
              AND p.ativo = true
              AND c.ativo = true
            ORDER BY p.nome
            """)
    List<Produto> buscarDestaques();

    @Query("""
        SELECT p
        FROM Produto p
        JOIN FETCH p.categoria
        LEFT JOIN FETCH p.marca
        WHERE p.id = :id
        """)
    Optional<Produto> buscarAdminPorId(
            @Param("id") Long id
    );

    @Query(
            value = """
            SELECT p
            FROM Produto p
            WHERE (
                :busca = ''
                OR LOWER(p.nome) LIKE CONCAT('%', :busca, '%')
                OR LOWER(p.sku) LIKE CONCAT('%', :busca, '%')
            )
            AND (
                :categoriaId IS NULL
                OR p.categoria.id = :categoriaId
            )
            AND (
                :marcaId IS NULL
                OR p.marca.id = :marcaId
            )
            AND (
                :ativo IS NULL
                OR p.ativo = :ativo
            )
            AND (
                :destaque IS NULL
                OR p.destaque = :destaque
            )
            ORDER BY p.nome
            """,

            countQuery = """
            SELECT COUNT(p)
            FROM Produto p
            WHERE (
                :busca = ''
                OR LOWER(p.nome) LIKE CONCAT('%', :busca, '%')
                OR LOWER(p.sku) LIKE CONCAT('%', :busca, '%')
            )
            AND (
                :categoriaId IS NULL
                OR p.categoria.id = :categoriaId
            )
            AND (
                :marcaId IS NULL
                OR p.marca.id = :marcaId
            )
            AND (
                :ativo IS NULL
                OR p.ativo = :ativo
            )
            AND (
                :destaque IS NULL
                OR p.destaque = :destaque
            )
            """
    )
    Page<Produto> buscarAdmin(
            @Param("busca") String busca,
            @Param("categoriaId") Long categoriaId,
            @Param("marcaId") Long marcaId,
            @Param("ativo") Boolean ativo,
            @Param("destaque") Boolean destaque,
            Pageable pageable
    );

    boolean existsBySku(String sku);

    List<Produto> findAllByOrderByNomeAsc();

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsBySkuAndIdNot(String sku, Long id);
}