package com.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest extends BaseTest {
    private final String loginUrl = "https://sitetc1kaykywaleskabreno.vercel.app/login";

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
    @Test
    @DisplayName("Realizar Login de Paciente com Usuário Errado e Senha Certa")
    void PatientLoginWithWrongUserAndCorrectPassword() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        usernameField.sendKeys("Usuario errado");
        passwordField.sendKeys("BobPass654!");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/paciente",
                currentUrl, "A URL atual não é a esperada!");
    }
    @Test
    @DisplayName("Realizar Login de Paciente com Usuário Certo e Senha Errada")
    void PatientLoginWithCorrectUserAndWrongPassword() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        usernameField.sendKeys("bob.brown654");
        passwordField.sendKeys("SenhaErrada");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/paciente",
                currentUrl, "A URL atual não é a esperada!");
    }
    @Test
    @DisplayName("Realizar Login de Medico com Usuário Errado e Senha Certa")
    void DoctorLoginWithWrongUserAndCorrectPassword() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        usernameField.sendKeys("Usuario errado");
        passwordField.sendKeys("DianaPass123!");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/medico")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                currentUrl, "A URL atual não é a esperada!");
    }
    @Test
    @DisplayName("Realizar Login de Medico com Usuário Certo e Senha Errada")
    void DoctorLoginWithCorrectUserAndWrongPassword() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        usernameField.sendKeys("diana.green123");
        passwordField.sendKeys("SenhaErrada");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/medico")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                currentUrl, "A URL atual não é a esperada!");
    }
    @Test
    @DisplayName("Realizar Login de Medico com Usuário Certo e Sem Senha")
    void DoctorLoginWithCorrectUserAndNoPass() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='User']")));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        usernameField.sendKeys("diana.green123");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/medico")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/medico",
                currentUrl, "A URL atual não é a esperada!");
    }
    @Test
    @DisplayName("Realizar Login de Paciente com Senha Certa e Sem Usuario")
    void PatientLoginWithCorrectPasswordAndNoUser() {
        driver.get(loginUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        passwordField.sendKeys("BobPass654!");
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/paciente")));

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/paciente",
                currentUrl, "A URL atual não é a esperada!");
    }
}
