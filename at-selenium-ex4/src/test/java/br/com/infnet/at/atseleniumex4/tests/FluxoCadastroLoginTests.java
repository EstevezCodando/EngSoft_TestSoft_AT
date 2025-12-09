package br.com.infnet.at.atseleniumex4.tests;

import br.com.infnet.at.atseleniumex4.base.BaseTest;
import br.com.infnet.at.atseleniumex4.pages.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FluxoCadastroLoginTests extends BaseTest {

    @Test
    @DisplayName("Cadastro de novo usuário e login com credenciais válidas")
    void deveCadastrarUsuarioELogarComSucesso() {
        String nome = "UsuarioAT";
        String senha = "SenhaForte123";
        String email = "user_" + System.currentTimeMillis() + "@teste.com";

        HomePage home = new HomePage(driver);
        SignupLoginPage signup = home.clicarSignupLogin();

        AccountCreationPage account = signup.iniciarCadastro(nome, email);
        LoggedInHomePage logged = account.preencherFormularioObrigatorioEConfirmar(senha);

        Assertions.assertTrue(logged.obterTextoUsuarioLogado().contains(nome));
    }

    @Test
    @DisplayName("Login inválido deve exibir mensagem de erro")
    void deveExibirErroLoginInvalido() {
        HomePage home = new HomePage(driver);
        SignupLoginPage login = home.clicarSignupLogin();

        login.tentarLoginInvalido("naoexiste@teste.com", "senha123");

        Assertions.assertTrue(login.obterMensagemErroLogin()
                .contains("incorrect"), "Mensagem de erro deveria ser exibida.");
    }
}
