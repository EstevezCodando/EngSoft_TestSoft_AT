package br.com.infnet.at.atseleniumex4.pages;

import br.com.infnet.at.atseleniumex4.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupLoginPage extends BasePage {

    private final By campoNomeCadastro = By.cssSelector("input[data-qa='signup-name']");
    private final By campoEmailCadastro = By.cssSelector("input[data-qa='signup-email']");
    private final By botaoSignup = By.cssSelector("button[data-qa='signup-button']");

    private final By campoEmailLogin = By.cssSelector("input[data-qa='login-email']");
    private final By campoSenhaLogin = By.cssSelector("input[data-qa='login-password']");
    private final By botaoLogin = By.cssSelector("button[data-qa='login-button']");
    private final By mensagemErroLogin = By.xpath("//p[contains(text(),'Your email or password is incorrect')]");

    public SignupLoginPage(WebDriver driver) {
        super(driver);
    }

    public AccountCreationPage iniciarCadastro(String nome, String email) {
        digitar(campoNomeCadastro, nome);
        digitar(campoEmailCadastro, email);
        clicar(botaoSignup);
        return new AccountCreationPage(driver);
    }

    public LoggedInHomePage fazerLoginValido(String email, String senha) {
        digitar(campoEmailLogin, email);
        digitar(campoSenhaLogin, senha);
        clicar(botaoLogin);
        return new LoggedInHomePage(driver);
    }

    public void tentarLoginInvalido(String email, String senha) {
        digitar(campoEmailLogin, email);
        digitar(campoSenhaLogin, senha);
        clicar(botaoLogin);
    }

    public String obterMensagemErroLogin() {
        return obterTexto(mensagemErroLogin);
    }
}
