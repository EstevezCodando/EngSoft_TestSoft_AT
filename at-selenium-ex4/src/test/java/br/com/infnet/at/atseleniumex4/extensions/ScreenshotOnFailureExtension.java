package br.com.infnet.at.atseleniumex4.extensions;

import br.com.infnet.at.atseleniumex4.base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotOnFailureExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        WebDriver driver = BaseTest.getDriver();
        if (driver == null) return;

        if (!(driver instanceof TakesScreenshot takesScreenshot)) return;

        byte[] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);

        try {
            Path pasta = Paths.get("target", "screenshots");
            Files.createDirectories(pasta);

            String nome = context.getDisplayName().replaceAll("[^a-zA-Z0-9-_]", "_")
                    + "_" + System.currentTimeMillis() + ".png";

            Files.write(pasta.resolve(nome), screenshotBytes);

            System.out.println("Screenshot salvo em: " + pasta.resolve(nome).toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao salvar screenshot: " + e.getMessage());
        }
    }
}
