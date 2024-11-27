package com.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    @DisplayName("Adicionar Novo Médico")
    void shouldAddDoctor() throws InterruptedException {
        driver.get("https://sitetc1kaykywaleskabreno.vercel.app/admin");

        Thread.sleep(2000);

        WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='Usuário']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Senha']"));
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        usernameField.sendKeys("novo.medico123");
        passwordField.sendKeys("SenhaMedico123!");
        addButton.click();

        // Aguarde ou verifique se o novo médico foi adicionado à lista
        Thread.sleep(1000); // Apenas para visualização rápida
    }
}
