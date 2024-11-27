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
    @Test
    @DisplayName("Realizar Login Correto com Usuário e Senha")
    void shouldLoginWithValidCredentials() throws InterruptedException {
        driver.get("https://sitetc1kaykywaleskabreno.vercel.app/");
        Thread.sleep(2000);

        // Localiza os elementos do formulário
        WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='User']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        // Preenche os campos de login
        usernameField.sendKeys("diana.green123");
        passwordField.sendKeys("DianaPass123!");
        loginButton.click();

        // Aguarda brevemente para o redirecionamento ou carregamento da nova página
        Thread.sleep(2000);

        // Verifica se o usuário foi redirecionado para a página incorreta
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.equals("https://sitetc1kaykywaleskabreno.vercel.app/medico")) {
            // Exibe mensagem no console e falha o teste
            System.out.println("Teste bem-sucedido");
        } else {
            // Mensagem de sucesso no console
            System.out.println("Teste falhou");
        }
    }


}
