package br.com.infnet.at.atviacepex3;

import br.com.infnet.at.atviacepex3.client.ViaCepClient;
import br.com.infnet.at.atviacepex3.model.ViaCepEndereco;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

class ViaCepApiTests {

    private ViaCepClient client;

    @BeforeEach
    void setUp() {
        this.client = new ViaCepClient();
    }

    @Test
    @DisplayName("CEP válido e existente deve retornar dados completos")
    void testeCepValidoExistente() {
        Optional<ViaCepEndereco> resultado = client.buscarPorCep("01001000");

        Assertions.assertTrue(resultado.isPresent());
        ViaCepEndereco endereco = resultado.get();

        Assertions.assertEquals("SP", endereco.getUf());
        Assertions.assertEquals("São Paulo", endereco.getLocalidade());
        Assertions.assertNotNull(endereco.getLogradouro());
    }

    @Test
    @DisplayName("CEP válido mas inexistente deve retornar Optional.empty")
    void testeCepValidoInexistente() {
        Optional<ViaCepEndereco> resultado = client.buscarPorCep("00000000");
        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("CEP inválido deve lançar IllegalArgumentException")
    void testeCepInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> client.buscarPorCep("123"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> client.buscarPorCep("123456789"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> client.buscarPorCep("ABC12345"));
    }

    @Test
    @DisplayName("Consulta por endereço válido deve retornar lista não vazia")
    void testeEnderecoValido() {
        List<ViaCepEndereco> lista = client.buscarPorEndereco(
                "SP",
                "Sao Paulo",
                "Avenida Paulista");

        Assertions.assertFalse(lista.isEmpty());
        Assertions.assertTrue(lista.stream().allMatch(e -> "SP".equals(e.getUf())));
    }

    @Test
    @DisplayName("Logradouro inexistente deve retornar lista vazia")
    void testeLogradouroInexistente() {
        List<ViaCepEndereco> lista = client.buscarPorEndereco(
                "SP",
                "Sao Paulo",
                "RuaQueNaoExisteXYZ123");

        Assertions.assertTrue(lista.isEmpty());
    }

    @Test
    @DisplayName("UF inválida deve lançar IllegalArgumentException")
    void testeUfInvalida() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> client.buscarPorEndereco("S", "Sao Paulo", "Avenida Paulista"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> client.buscarPorEndereco("SPX", "Sao Paulo", "Avenida Paulista"));
    }

    @Test
    @DisplayName("Cidade ou logradouro vazios devem lançar IllegalArgumentException")
    void testeCamposVazios() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> client.buscarPorEndereco("SP", "", "Avenida Paulista"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> client.buscarPorEndereco("SP", "Sao Paulo", ""));
    }

    @Test
    @DisplayName("CEP vazio deve lançar IllegalArgumentException")
    void testeCepVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> client.buscarPorCep(""));
    }

    @Test
    @DisplayName("Cidade com acento deve ser aceita na consulta por endereço")
    void testeCidadeComAcento() {
        List<ViaCepEndereco> lista = client.buscarPorEndereco(
                "SP",
                "São Paulo",
                "Avenida Paulista");

        Assertions.assertFalse(lista.isEmpty());
    }

}
