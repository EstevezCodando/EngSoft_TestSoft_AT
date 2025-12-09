package br.com.infnet.at.atseleniumex4.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement encontrar(By locator) {
        return driver.findElement(locator);
    }

    protected void clicar(By locator) {
        encontrar(locator).click();
    }

    protected void digitar(By locator, String texto) {
        WebElement elemento = encontrar(locator);
        elemento.clear();
        elemento.sendKeys(texto);
    }

    protected String obterTexto(By locator) {
        return encontrar(locator).getText();
    }
}
