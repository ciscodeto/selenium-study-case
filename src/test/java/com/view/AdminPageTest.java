package com.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminPageTest extends BaseTest {
    private final String adminUrl = "https://sitetc1kaykywaleskabreno.vercel.app/admin";

    private static String generateUsername() {
        return "Fulanim" + System.currentTimeMillis();
    }

    private static String generatePassword() {
        return "sEnh4!" + System.currentTimeMillis();
    }

    private void handleAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Nenhum alerta foi exibido.");
        }
    }

    @Test
    @DisplayName("Should Add New Doctor")
    void shouldAddDoctor() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='Usuário']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Senha']"));
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        String newDoctorUsername = generateUsername();
        String newDoctorPassword = generatePassword();

        usernameField.sendKeys(newDoctorUsername);
        passwordField.sendKeys(newDoctorPassword);
        addButton.click();

        handleAlert();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastDoctorElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//ul/li[last()]"))
        );

        String lastDoctorText = lastDoctorElement.getText();
        assertTrue(
                lastDoctorText.contains(newDoctorUsername) && lastDoctorText.contains(newDoctorPassword),
                "O último item da lista não corresponde ao médico recém-adicionado!"
        );
    }

    @Test
    @DisplayName("Should Not Add Repeated Doctor")
    void shouldNotAddRepeatedDoctor() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='Usuário']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Senha']"));
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        String existingDoctorUsername = "diana.green123";
        String existingDoctorPassword = "DianaPass123!";

        List<WebElement> initialDoctorList = driver.findElements(By.xpath("//ul/li"));
        long initialCount = initialDoctorList.stream()
                .filter(element -> element.getText().contains(existingDoctorUsername))
                .count();

        usernameField.sendKeys(existingDoctorUsername);
        passwordField.sendKeys(existingDoctorPassword);
        addButton.click();

        handleAlert();

        List<WebElement> updatedDoctorList = driver.findElements(By.xpath("//ul/li"));
        long updatedCount = updatedDoctorList.stream()
                .filter(element -> element.getText().contains(existingDoctorUsername))
                .count();

        assertEquals(initialCount, updatedCount, "O médico duplicado foi adicionado novamente!");
    }

    @Test
    @DisplayName("Should Not Add Doctor Without Password")
    void shouldNotAddDoctorWithoutPassword() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElement(By.xpath("//input[@placeholder='Usuário']"));
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        String newDoctorUsername = generateUsername();

        List<WebElement> initialDoctorList = driver.findElements(By.xpath("//ul/li"));

        for (WebElement doctor : initialDoctorList) {
            if (doctor.getText().contains("Senha: ")) {
                continue;
            }
            WebElement deleteButton = doctor.findElement(By.xpath(".//button[contains(@class, 'buttonlixeira')]"));
            deleteButton.click();
            handleAlert();
        }

        long initialCount = driver.findElements(By.xpath("//ul/li"))
                .stream()
                .filter(element -> element.getText().contains(newDoctorUsername))
                .count();

        usernameField.sendKeys(newDoctorUsername);
        addButton.click();

        handleAlert();

        List<WebElement> updatedDoctorList = driver.findElements(By.xpath("//ul/li"));
        long updatedCount = updatedDoctorList.stream()
                .filter(element -> element.getText().contains(newDoctorUsername))
                .count();

        assertEquals(initialCount, updatedCount, "O médico foi adicionado mesmo sem senha!");
    }

    @Test
    @DisplayName("Should Not Add Doctor Without Name")
    void shouldNotAddDoctorWithoutName() {
        driver.get(adminUrl);

        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Senha']"));
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        String newDoctorPassword = generatePassword();

        List<WebElement> initialDoctorList = driver.findElements(By.xpath("//ul/li"));
        int initialListSize = initialDoctorList.size();

        passwordField.sendKeys(newDoctorPassword);
        addButton.click();

        handleAlert();

        List<WebElement> updatedDoctorList = driver.findElements(By.xpath("//ul/li"));
        int updatedListSize = updatedDoctorList.size();

        assertEquals(initialListSize, updatedListSize, "O médico foi adicionado mesmo sem nome!");
    }

    @Test
    @DisplayName("Should Delete Doctor")
    void shouldDeleteDoctor() {
        driver.get(adminUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastDoctorElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//ul/li[last()]"))
        );

        String lastDoctorText = lastDoctorElement.getText();
        WebElement deleteButton = lastDoctorElement.findElement(By.xpath(".//button[contains(@class, 'buttonlixeira')]"));

        deleteButton.click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Nenhum alerta foi exibido.");
        }

        boolean doctorDeleted;
        List<WebElement> doctorElements = driver.findElements(By.xpath("//ul/li"));
        doctorDeleted = doctorElements.stream()
                .noneMatch(element -> element.getText().contains(lastDoctorText));

        assertTrue(doctorDeleted, "O médico ainda está presente na lista após a exclusão!");
    }
}
