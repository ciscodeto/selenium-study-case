package com.view;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;

public class SeleniumTest {
    private WebDriver driver;

    @BeforeEach
    void setUp(){
        System.setProperty("webdriver.chrome.driver",
                Paths.get("src", "test", "resources", "drivers", "chromedriver.exe").toString());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
    }
    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @DisplayName("Should open and close Chrome browser")
    void shouldOpenAndCloseChromeBrowser() throws InterruptedException {
        try {
            // Acessa a página do Google
            System.out.println("Abrindo o navegador...");
            driver.get("https://www.google.com");
            System.out.println("Título da página: " + driver.getTitle());

            // Aguarda 1 segundo para visualização
            Thread.sleep(1000);
        } finally {
            // Fecha o navegador
            driver.quit();
            System.out.println("Navegador fechado.");
        }
    }
}
