package br.com.bikepadrao.repository;

import br.com.bikepadrao.entity.ProdutoEspecificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoEspecificacaoRepository
        extends JpaRepository<ProdutoEspecificacao, Long> {

    List<ProdutoEspecificacao>
    findByProdutoIdOrderByOrdemAsc(Long produtoId);
}