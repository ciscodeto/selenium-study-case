package com.view;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Nested
    @DisplayName("Admin Page Test")
    class AdminPageTest {
        private final String adminUrl = "https://sitetc1kaykywaleskabreno.vercel.app/admin";

        @BeforeEach
        void adminSetUp() {
            driver.get(adminUrl);
        }

        @Test
        @DisplayName("Adicionar Novo Médico")
        void shouldAddDoctor() throws InterruptedException {

            WebElement usernameField = driver.findElements(By.xpath("//input[@placeholder='Usuário']")).get(1);
            WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(1);
            WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

            usernameField.sendKeys("novo.medico123");
            passwordField.sendKeys("SenhaMedico123!");
            addButton.click();

            Thread.sleep(1000);
        }
    }

    @Nested
    @DisplayName("Admin Page Test")
    class LoginPageTest {
        private final String loginUrl = "https://sitetc1kaykywaleskabreno.vercel.app/login";

        @BeforeEach
        void loginSetUp() {
            driver.get(loginUrl);
        }

        @Test
        @DisplayName("Realizar Login Correto com Usuário e Senha")
        void shouldLoginWithValidCredentials() throws InterruptedException {
            driver.get(loginUrl);
            Thread.sleep(2000);

            WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='User']"));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("diana.green123");
            passwordField.sendKeys("DianaPass123!");
            loginButton.click();

            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                    currentUrl, "A URL atual não é a esperada!");
        }
    }
}
