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

import static org.junit.jupiter.api.Assertions.*;

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
        @DisplayName("Should Add New Doctor")
        void shouldAddDoctor() {

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
        void shouldLoginWithValidCredentials() {
            driver.get(loginUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("diana.green123");
            passwordField.sendKeys("DianaPass123!");
            loginButton.click();

            wait.until(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/medico"));

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                    currentUrl, "A URL atual não é a esperada!");
        }

        @Test
        @DisplayName("Login para Página de Médico Usando um Paciente com Usuário e Senha")
        void shouldLoginWithInvaliDoctorCredentials() {
            driver.get(loginUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("bob.brown654");
            passwordField.sendKeys("BobPass654!");
            loginButton.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente")));

            String currentUrl = driver.getCurrentUrl();
            assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                    currentUrl, "A URL atual não é a esperada!");
        }



        @Test
        @DisplayName("Realizar Login de Médico Usando um Usuário e Senha Qualquer")
        void shouldLoginWithRandomCredentials() {
            driver.get(loginUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            try {
                WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
                WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
                WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

                usernameField.sendKeys("usuario123");
                passwordField.sendKeys("123456");
                loginButton.click();

                wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/medico")));

                String currentUrl = driver.getCurrentUrl();
                assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                        currentUrl, "A URL atual não é a esperada!");
            } catch (Exception e) {
                Assertions.fail("Erro no Teste 3: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Realizar Login Correto com Usuário e Senha de Paciente")
        void shouldLoginWithValidCredentialsPacient() {
            driver.get(loginUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("bob.brown654");
            passwordField.sendKeys("BobPass654!");
            loginButton.click();

            wait.until(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente"));

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/paciente",
                    currentUrl, "A URL atual não é a esperada!");
        }
        @Test
        @DisplayName("Login para Página de Paciente Usando um Médico com Usuário e Senha")
        void shouldLoginWithInvalidPatientCredentials() {
            driver.get(loginUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

            usernameField.sendKeys("novo.medico123");
            passwordField.sendKeys("SenhaMedico123!");
            loginButton.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente")));

            String currentUrl = driver.getCurrentUrl();
            assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/paciente",
                    currentUrl, "A URL atual não é a esperada!");
        }
    }
}
