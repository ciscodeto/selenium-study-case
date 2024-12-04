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

    @Test
    @DisplayName("Should Add New Doctor")
    void shouldAddDoctor() {
        driver.get(adminUrl);

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
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Nenhum alerta foi exibido.");
        }

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
