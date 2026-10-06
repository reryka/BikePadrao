package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.ProdutoAdminDTO;
import br.com.bikepadrao.dto.ProdutoFormDTO;
import br.com.bikepadrao.entity.Categoria;
import br.com.bikepadrao.entity.Marca;
import br.com.bikepadrao.entity.Produto;
import br.com.bikepadrao.repository.CategoriaRepository;
import br.com.bikepadrao.repository.MarcaRepository;
import br.com.bikepadrao.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import br.com.bikepadrao.storage.ImagemStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoAdminService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ImagemStorageService imagemStorageService;

    @Transactional(readOnly = true)
    public List<ProdutoAdminDTO> listarTodos() {

        return produtoRepository
                .findAllByOrderByNomeAsc()
                .stream()
                .map(produto ->
                        new ProdutoAdminDTO(
                                produto.getId(),
                                produto.getNome(),
                                produto.getSku(),
                                produto.getPreco(),
                                produto.getEstoque(),
                                produto.getAtivo(),
                                produto.getDestaque()
                        )
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoAdminDTO> listar(String busca) {

        return listar(
                busca,
                null,
                null,
                null,
                null
        );
    }


    @Transactional(readOnly = true)
    public List<ProdutoAdminDTO> listar(
            String busca,
            Long categoriaId,
            Long marcaId,
            Boolean ativo,
            Boolean destaque
    ) {

        if (busca == null || busca.isBlank()) {
            busca = "";
        } else {
            busca = busca
                    .trim()
                    .toLowerCase();
        }

        return produtoRepository
                .buscarAdmin(
                        busca,
                        categoriaId,
                        marcaId,
                        ativo,
                        destaque
                )
                .stream()
                .map(produto ->
                        new ProdutoAdminDTO(
                                produto.getId(),
                                produto.getNome(),
                                produto.getSku(),
                                produto.getPreco(),
                                produto.getEstoque(),
                                produto.getAtivo(),
                                produto.getDestaque()
                        )
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoFormDTO buscarForm(Long id) {

        Produto produto = produtoRepository
                .buscarAdminPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );

        ProdutoFormDTO dto = new ProdutoFormDTO();

        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setSlug(produto.getSlug());
        dto.setSku(produto.getSku());

        dto.setDescricaoCurta(
                produto.getDescricaoCurta()
        );

        dto.setDescricao(
                produto.getDescricao()
        );

        dto.setPreco(produto.getPreco());

        dto.setPrecoPromocional(
                produto.getPrecoPromocional()
        );

        dto.setEstoque(produto.getEstoque());

        dto.setAtivo(produto.getAtivo());

        dto.setDestaque(
                produto.getDestaque()
        );

        dto.setCategoriaId(
                produto.getCategoria().getId()
        );

        if (produto.getMarca() != null) {
            dto.setMarcaId(
                    produto.getMarca().getId()
            );
        }

        return dto;
    }


    @Transactional
    public Produto criar(
            ProdutoFormDTO dto
    ) {

        validarUnicidade(dto, null);

        Produto produto = new Produto();

        preencherProduto(produto, dto);

        return produtoRepository.save(produto);
    }


    @Transactional
    public void atualizar(
            Long id,
            ProdutoFormDTO dto
    ) {

        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );

        validarUnicidade(dto, id);

        preencherProduto(produto, dto);
    }


    @Transactional
    public void alternarStatus(Long id) {

        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );

        produto.setAtivo(
                !Boolean.TRUE.equals(
                        produto.getAtivo()
                )
        );
    }

    @Transactional
    public void alterarStatusEmLote(
            List<Long> ids,
            boolean ativo
    ) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto selecionado."
            );
        }

        List<Produto> produtos =
                produtoRepository.findAllById(ids);

        if (produtos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto encontrado."
            );
        }

        produtos.forEach(
                produto -> produto.setAtivo(ativo)
        );

        produtoRepository.saveAll(produtos);
    }

    @Transactional
    public void alterarDestaqueEmLote(
            List<Long> ids,
            boolean destaque
    ) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto selecionado."
            );
        }

        List<Produto> produtos =
                produtoRepository.findAllById(ids);

        if (produtos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto encontrado."
            );
        }

        produtos.forEach(
                produto -> produto.setDestaque(destaque)
        );

        produtoRepository.saveAll(produtos);
    }

    /* * 1. Localiza os produtos
            2. Guarda as URLs das imagens
            3. Exclui Produto
            4. Cascade exclui ProdutoImagem
            5. Cascade exclui ProdutoEspecificacao
            6. Commit do banco
            7. Exclui os arquivos físicos*/
    @Transactional
    public void excluirEmLote(
            List<Long> ids
    ) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto selecionado."
            );
        }


        List<Produto> produtos =
                produtoRepository.findAllById(ids);


        if (produtos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum produto encontrado."
            );
        }


        long quantidadeIds =
                ids.stream()
                        .distinct()
                        .count();


        if (produtos.size() != quantidadeIds) {
            throw new IllegalArgumentException(
                    "Um ou mais produtos selecionados não foram encontrados."
            );
        }


        /*
         * Guarda as URLs antes da exclusão.
         * Depois que os produtos forem removidos,
         * os registros de produto_imagem também serão
         * removidos pelo cascade.
         */

        List<String> urlsImagens =
                produtos.stream()
                        .flatMap(
                                produto ->
                                        produto
                                                .getImagens()
                                                .stream()
                        )
                        .map(imagem -> imagem.getUrl())
                        .filter(url ->
                                url != null &&
                                        !url.isBlank()
                        )
                        .distinct()
                        .toList();


        /*
         * Exclui os produtos.
         *
         * Produto possui CascadeType.ALL e
         * orphanRemoval para imagens e especificações.
         */

        produtoRepository.deleteAll(produtos);

        produtoRepository.flush();


        /*
         * Somente depois que a transação do banco
         * for confirmada, remove os arquivos físicos.
         *
         * Assim evitamos apagar as imagens antes
         * de saber se a exclusão do banco funcionou.
         *
         */

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCommit() {

                                for (String url : urlsImagens) {

                                    try {

                                        imagemStorageService
                                                .excluir(url);

                                    } catch (RuntimeException e) {

                                        /*
                                         * O produto já foi excluído do banco.
                                         * Um erro no arquivo físico não deve
                                         * transformar a operação em erro
                                         * para o administrador.
                                         */

                                        log.error(
                                                "Não foi possível excluir "
                                                        + "o arquivo físico da imagem: {}",
                                                url,
                                                e
                                        );
                                    }
                                }
                            }
                        }
                );
    }

    private void preencherProduto(
            Produto produto,
            ProdutoFormDTO dto
    ) {

        Categoria categoria =
                categoriaRepository
                        .findById(dto.getCategoriaId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Categoria inválida."
                                )
                        );


        Marca marca = null;

        if (dto.getMarcaId() != null) {

            marca = marcaRepository
                    .findById(dto.getMarcaId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Marca inválida."
                            )
                    );
        }


        produto.setNome(
                dto.getNome().trim()
        );

        produto.setSlug(
                dto.getSlug()
                        .trim()
                        .toLowerCase()
        );

        produto.setSku(
                dto.getSku()
                        .trim()
                        .toUpperCase()
        );

        produto.setDescricaoCurta(
                dto.getDescricaoCurta()
        );

        produto.setDescricao(
                dto.getDescricao()
        );

        produto.setPreco(
                dto.getPreco()
        );

        produto.setPrecoPromocional(
                dto.getPrecoPromocional()
        );

        produto.setEstoque(
                dto.getEstoque()
        );

        produto.setAtivo(
                Boolean.TRUE.equals(dto.getAtivo())
        );

        produto.setDestaque(
                Boolean.TRUE.equals(dto.getDestaque())
        );

        produto.setCategoria(categoria);

        produto.setMarca(marca);
    }


    private void validarUnicidade(
            ProdutoFormDTO dto,
            Long produtoId
    ) {

        boolean slugExiste;

        boolean skuExiste;


        if (produtoId == null) {

            slugExiste =
                    produtoRepository
                            .existsBySlug(dto.getSlug());

            skuExiste =
                    produtoRepository
                            .existsBySku(dto.getSku());

        } else {

            slugExiste =
                    produtoRepository
                            .existsBySlugAndIdNot(
                                    dto.getSlug(),
                                    produtoId
                            );

            skuExiste =
                    produtoRepository
                            .existsBySkuAndIdNot(
                                    dto.getSku(),
                                    produtoId
                            );
        }


        if (slugExiste) {

            throw new IllegalArgumentException(
                    "Já existe um produto com esse slug."
            );
        }


        if (skuExiste) {

            throw new IllegalArgumentException(
                    "Já existe um produto com esse SKU."
            );
        }
    }
}