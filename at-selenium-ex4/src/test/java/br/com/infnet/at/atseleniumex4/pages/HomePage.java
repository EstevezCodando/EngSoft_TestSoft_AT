package br.com.infnet.at.atseleniumex4.pages;

import br.com.infnet.at.atseleniumex4.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By linkSignupLogin = By.cssSelector("a[href='/login']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public SignupLoginPage clicarSignupLogin() {
        clicar(linkSignupLogin);
        return new SignupLoginPage(driver);
    }
}
