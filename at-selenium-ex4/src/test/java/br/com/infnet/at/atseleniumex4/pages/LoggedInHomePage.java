package br.com.infnet.at.atseleniumex4.pages;

import br.com.infnet.at.atseleniumex4.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoggedInHomePage extends BasePage {

    private final By textoUsuarioLogado = By.xpath("//a[contains(text(),'Logged in as')]");
    private final By linkLogout = By.cssSelector("a[href='/logout']");

    public LoggedInHomePage(WebDriver driver) {
        super(driver);
    }

    public String obterTextoUsuarioLogado() {
        return obterTexto(textoUsuarioLogado);
    }

    public SignupLoginPage fazerLogout() {
        clicar(linkLogout);
        return new SignupLoginPage(driver);
    }
}
