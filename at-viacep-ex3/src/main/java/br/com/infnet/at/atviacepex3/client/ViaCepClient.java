package br.com.infnet.at.atviacepex3.client;

import br.com.infnet.at.atviacepex3.model.ViaCepEndereco;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class ViaCepClient {

    private static final String BASE_URL = "https://viacep.com.br/ws";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ViaCepClient() {
        this(HttpClient.newHttpClient(), new ObjectMapper());
    }

    public ViaCepClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Optional<ViaCepEndereco> buscarPorCep(String cep) {
        validarCep(cep);

        String url = BASE_URL + "/" + cep + "/json/";
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> resposta = httpClient.send(requisicao, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() != 200) {
                throw new RuntimeException("Erro ao chamar ViaCEP. HTTP: " + resposta.statusCode());
            }

            ViaCepEndereco endereco = objectMapper.readValue(resposta.body(), ViaCepEndereco.class);

            if (endereco.isInexistente()) {
                return Optional.empty();
            }

            return Optional.of(endereco);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Falha ao consumir a API ViaCEP", e);
        }
    }

    public List<ViaCepEndereco> buscarPorEndereco(String uf, String cidade, String logradouro) {
        validarUf(uf);
        validarCidade(cidade);
        validarLogradouro(logradouro);

        String cidadeEncoded = urlEncodePathSegment(cidade);
        String logradouroEncoded = urlEncodePathSegment(logradouro);

        String url = BASE_URL + "/" + uf + "/" + cidadeEncoded + "/" + logradouroEncoded + "/json/";
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> resposta = httpClient.send(requisicao, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() != 200) {
                throw new RuntimeException("Erro ao chamar ViaCEP. HTTP: " + resposta.statusCode());
            }

            return objectMapper.readValue(resposta.body(), new TypeReference<List<ViaCepEndereco>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Falha ao consumir a API ViaCEP", e);
        }
    }

    private void validarCep(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido. Deve conter exatamente 8 dígitos numéricos.");
        }
    }

    private void validarUf(String uf) {
        if (uf == null || !uf.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("UF inválida. Deve conter exatamente 2 letras maiúsculas.");
        }
    }

    private void validarCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade não pode ser vazia.");
        }
    }

    private void validarLogradouro(String logradouro) {
        if (logradouro == null || logradouro.isBlank()) {
            throw new IllegalArgumentException("Logradouro não pode ser vazio.");
        }
    }

    /**
     * Encode seguro para segmentos de path (não para query string).
     * Substitui o '+' por '%20' para representar espaços corretamente no caminho.
     */
    private String urlEncodePathSegment(String valor) {
        String encoded = URLEncoder.encode(valor, StandardCharsets.UTF_8);
        return encoded.replace("+", "%20");
    }
}
