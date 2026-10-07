package br.com.bikepadrao.service.relatorios;

import br.com.bikepadrao.dto.relatorios.*;
import br.com.bikepadrao.repository.relatorios.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;


    public RelatorioResumoDTO buscarResumo() {

        return new RelatorioResumoDTO(

                relatorioRepository
                        .contarProdutos(),

                relatorioRepository
                        .contarProdutosAtivos(),

                relatorioRepository
                        .contarProdutosInativos(),

                relatorioRepository
                        .contarEstoqueBaixo(),

                relatorioRepository
                        .contarSemEstoque(),

                relatorioRepository
                        .contarCategorias(),

                relatorioRepository
                        .contarMarcas()
        );
    }

    public RelatorioEstoqueResumoDTO buscarEstoque() {

        List<RelatorioEstoqueDTO> produtos = relatorioRepository.buscarRelatorioEstoque();


        BigDecimal valorTotal = produtos.stream()

                        .filter(produto ->
                                produto.getEstoque() != null
                                        && produto.getEstoque() > 0
                        )

                        .map(RelatorioEstoqueDTO::getValorEstoque)

                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return new RelatorioEstoqueResumoDTO(produtos, valorTotal);
    }

    public List<RelatorioCategoriaDTO> buscarProdutosPorCategoria() {
        return relatorioRepository.buscarProdutosPorCategoria();
    }

    public List<RelatorioMarcaDTO> buscarProdutosPorMarca(){
        return relatorioRepository.buscarProdutosPorMarca();
    }

    public List<RelatorioProdutoIncompletoDTO> buscarProdutosSemImagem() {
        return relatorioRepository.buscarProdutosSemImagem();
    }

    public List<RelatorioProdutoIncompletoDTO> buscarProdutosSemEspecificacao(){
        return relatorioRepository.buscarProdutosSemEspecificacao();
    }

    public List<RelatorioProdutoRecenteDTO> buscarProdutosRecentes(){
        return relatorioRepository.buscarProdutosRecentes();
    }
}