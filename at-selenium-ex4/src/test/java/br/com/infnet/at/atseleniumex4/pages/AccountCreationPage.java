package br.com.infnet.at.atseleniumex4.pages;

import br.com.infnet.at.atseleniumex4.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class AccountCreationPage extends BasePage {

    private final By radioTituloMr = By.id("id_gender1");
    private final By campoSenha = By.id("password");

    private final By campoPrimeiroNome = By.id("first_name");
    private final By campoSobrenome = By.id("last_name");
    private final By campoEndereco = By.id("address1");
    private final By comboPais = By.id("country");
    private final By campoEstado = By.id("state");
    private final By campoCidade = By.id("city");
    private final By campoCep = By.id("zipcode");
    private final By campoCelular = By.id("mobile_number");

    private final By botaoCriarConta = By.cssSelector("button[data-qa='create-account']");
    private final By textoContaCriada = By.cssSelector("h2[data-qa='account-created']");
    private final By botaoContinuar = By.cssSelector("a[data-qa='continue-button']");

    public AccountCreationPage(WebDriver driver) {
        super(driver);
    }

    public LoggedInHomePage preencherFormularioObrigatorioEConfirmar(String senha) {
        clicar(radioTituloMr);
        digitar(campoSenha, senha);

        digitar(campoPrimeiroNome, "Teste");
        digitar(campoSobrenome, "Automacao");
        digitar(campoEndereco, "Rua Selenium 123");

        Select selectPais = new Select(encontrar(comboPais));
        selectPais.selectByVisibleText("Canada");

        digitar(campoEstado, "Estado");
        digitar(campoCidade, "Cidade");
        digitar(campoCep, "12345");
        digitar(campoCelular, "11999990000");

        clicar(botaoCriarConta);

        String texto = obterTexto(textoContaCriada);
        if (!texto.toUpperCase().contains("ACCOUNT CREATED")) {
            throw new IllegalStateException("Conta não foi criada corretamente.");
        }

        clicar(botaoContinuar);
        return new LoggedInHomePage(driver);
    }
}
