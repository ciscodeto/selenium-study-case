package com.view;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumTest {
    private WebDriver driver;

    @BeforeEach
    void setUp(){
        System.setProperty("webdriver.chrome.driver",
                Paths.get("src", "test", "resources", "drivers", "chromedriver.exe").toString());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
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

            WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='Usuário']"));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Senha']"));
            WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

            String newDoctorUsername = "novo.medico123";
            String newDoctorPassword = "SenhaMedico123!";
            usernameField.sendKeys(newDoctorUsername);
            passwordField.sendKeys(newDoctorPassword);
            addButton.click();

            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                Alert alert = wait.until(ExpectedConditions.alertIsPresent()); // Espera até o alerta estar presente
                alert.accept(); // Aceitar o alerta
            } catch (Exception e) {
                System.out.println("Nenhum alerta foi exibido.");
            }

            // Localizar o último item da lista com espera dinâmica
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement lastDoctorElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//ul/li[last()]"))
            );

            String lastDoctorText = lastDoctorElement.getText();
            assertTrue(
                    lastDoctorText.contains(newDoctorUsername) && lastDoctorText.contains(newDoctorPassword),
                    "O último item da lista não corresponde ao médico recém-adicionado!"
            );

            System.out.println("Teste bem-sucedido: Médico adicionado com sucesso.");
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
        @Test
        @DisplayName("Realizar Login incorreto de Medico Usando um Paciente com Usuário e Senha")
        void shouldloginwithinvaliddoctorcredentials() throws InterruptedException {
            driver.get(loginUrl);
            Thread.sleep(2000);

            WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='User']"));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("bob.brown654");
            passwordField.sendKeys("BobPass654!");
            loginButton.click();

            Thread.sleep(2000);

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                    currentUrl, "A URL atual não é a esperada!");
        }
        @Test
        @DisplayName("Realizar Login de Medico Usando um Usuário e Senha qualquer")
        void shouldloginwithrandomcredentials() throws InterruptedException {
            driver.get(loginUrl);
            Thread.sleep(2000);

            WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='User']"));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("usuario123");
            passwordField.sendKeys("123456");
            loginButton.click();

            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                    currentUrl, "A URL atual não é a esperada!");
        }
    }
}
