package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.ProdutoDetalheDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class WhatsAppService {

    @Value("${loja.whatsapp.numero}")
    private String numero;

    @Value("${loja.base-url}")
    private String baseUrl;


    public String criarLinkProduto(
            ProdutoDetalheDTO produto
    ) {

        BigDecimal preco =
                produto.getPrecoPromocional() != null
                        ? produto.getPrecoPromocional()
                        : produto.getPreco();


        String precoFormatado = NumberFormat
                .getCurrencyInstance(
                        Locale.forLanguageTag("pt-BR")
                )
                .format(preco);


        String linkProduto =
                baseUrl
                        + "/produto/"
                        + produto.getSlug();


        String mensagem = """
                Olá! Tenho interesse neste produto:

                %s

                Código: %s
                Preço: %s

                %s
                """.formatted(
                produto.getNome(),
                produto.getSku(),
                precoFormatado,
                linkProduto
        );


        String mensagemCodificada =
                URLEncoder
                        .encode(
                                mensagem,
                                StandardCharsets.UTF_8
                        )
                        .replace("+", "%20");


        String numeroLimpo =
                numero.replaceAll("\\D", "");


        return "https://wa.me/"
                + numeroLimpo
                + "?text="
                + mensagemCodificada;
    }
}