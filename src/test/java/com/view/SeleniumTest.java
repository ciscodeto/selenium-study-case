package com.view;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        driver.get("https://sitetc1kaykywaleskabreno.vercel.app/admin");
    }
    @AfterEach
    void tearDown(){
        //driver.quit();
    }


    @Nested
    @DisplayName("Admin Page Test")
    class AdminPageTest {
        @Test
        @DisplayName("Adicionar Novo Médico")
        void shouldAddDoctor() throws InterruptedException {

            WebElement usernameField = driver.findElements(By.xpath("//input[@placeholder='Usuário']")).get(1);
            WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(1);
            WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

            usernameField.sendKeys("novo.medico123");
            passwordField.sendKeys("SenhaMedico123!");
            addButton.click();

            // Aguarde ou verifique se o novo médico foi adicionado à lista
            Thread.sleep(1000); // Apenas para visualização rápida
        }
    }
}
