package br.com.bikepadrao.repository.relatorios;

import br.com.bikepadrao.dto.relatorios.*;
import br.com.bikepadrao.entity.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RelatorioRepository
        extends Repository<Produto, Long> {


    @Query("""
            SELECT COUNT(p)
            FROM Produto p
            """)
    long contarProdutos();


    @Query("""
            SELECT COUNT(p)
            FROM Produto p
            WHERE p.ativo = true
            """)
    long contarProdutosAtivos();


    @Query("""
            SELECT COUNT(p)
            FROM Produto p
            WHERE p.ativo = false
            """)
    long contarProdutosInativos();


    @Query("""
            SELECT COUNT(p)
            FROM Produto p
            WHERE p.estoque BETWEEN 1 AND 5
            """)
    long contarEstoqueBaixo();


    @Query("""
            SELECT COUNT(p)
            FROM Produto p
            WHERE p.estoque <= 0
            """)
    long contarSemEstoque();


    @Query("""
            SELECT COUNT(c)
            FROM Categoria c
            """)
    long contarCategorias();


    @Query("""
            SELECT COUNT(m)
            FROM Marca m
            """)
    long contarMarcas();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioEstoqueDTO(
            p.id,
            p.nome,
            p.sku,
            c.nome,
            m.nome,
            p.estoque,
            p.preco,
            p.ativo
        )
        FROM Produto p
        JOIN p.categoria c
        LEFT JOIN p.marca m
        ORDER BY p.estoque ASC, p.nome ASC
        """)
    List<RelatorioEstoqueDTO> buscarRelatorioEstoque();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioCategoriaDTO(
            c.id,
            c.nome,
            COUNT(p)
        )
        FROM Categoria c
        LEFT JOIN Produto p
            ON p.categoria = c
        GROUP BY c.id, c.nome
        ORDER BY COUNT(p) DESC, c.nome ASC
        """)
    List<RelatorioCategoriaDTO> buscarProdutosPorCategoria();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioMarcaDTO(
            m.id,
            m.nome,
            COUNT(p)
        )
        FROM Marca m
        LEFT JOIN Produto p
            ON p.marca = m
        GROUP BY m.id, m.nome
        ORDER BY COUNT(p) DESC, m.nome ASC
        """)
    List<RelatorioMarcaDTO> buscarProdutosPorMarca();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioProdutoIncompletoDTO(
            p.id,
            p.nome,
            p.sku,
            c.nome,
            m.nome,
            p.ativo
        )
        FROM Produto p
        JOIN p.categoria c
        LEFT JOIN p.marca m
        WHERE p.imagens IS EMPTY
        ORDER BY p.nome ASC
        """)
    List<RelatorioProdutoIncompletoDTO> buscarProdutosSemImagem();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioProdutoIncompletoDTO(
            p.id,
            p.nome,
            p.sku,
            c.nome,
            m.nome,
            p.ativo
        )
        FROM Produto p
        join p.categoria c
        LEFT JOIN p.marca m
        WHERE p.especificacoes IS EMPTY
        ORDER BY p.nome ASC
        """)
    List<RelatorioProdutoIncompletoDTO> buscarProdutosSemEspecificacao();

    @Query("""
        SELECT new br.com.bikepadrao.dto.relatorios.RelatorioProdutoRecenteDTO(
            p.id,
            p.nome,
            p.sku,
            c.nome,
            m.nome,
            p.ativo,
            p.dataCriacao
        )
        FROM Produto p
        JOIN p.categoria c
        LEFT JOIN p.marca m
        ORDER BY p.dataCriacao DESC
        """)
    List<RelatorioProdutoRecenteDTO> buscarProdutosRecentes();
}