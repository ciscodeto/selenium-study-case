package com.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class PacientPageTest extends BaseTest {

    private final String pacienteUrl = "https://sitetc1kaykywaleskabreno.vercel.app/paciente";

    @Test
    @DisplayName("Tentar salvar atendimento com paciente não existente verificando a lista na página Admin")
    void testSaveAppointmentWithNonExistentPatient() {

        driver.get("https://sitetc1kaykywaleskabreno.vercel.app/admin");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement patientListSection = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='root']/div/h2[4]")));
        String patientListText = patientListSection.getText();
        boolean patientExists = patientListText.contains("Paciente Inexistente");
        assertFalse(patientExists, "O paciente 'Paciente Inexistente' foi encontrado na lista, mas não deveria existir.");

        driver.get(pacienteUrl);

        WebElement patientNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[1]/label/input")));
        patientNameField.sendKeys("Paciente Inexistente");

        WebElement patientNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/input")));
        patientNumberField.sendKeys("13690000");

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/button")));
        confirmButton.click();

        WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[3]/label/input")));
        dateField.sendKeys("2024-12-04");

        WebElement timeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[4]/label/input")));
        timeField.sendKeys("10:30");

        WebElement specialtyField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[5]/label/input")));
        specialtyField.sendKeys("Otorrinolaringologista");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/button[1]")));
        saveButton.click();

        // Verifica se o arquivo de relatório foi criado
        try {
            String filePath = "caminho/do/diretorio/do/arquivo/relatorio_paciente.txt"; // Ajuste conforme necessário
            Path path = Paths.get(filePath);

            boolean isFileCreated = Files.exists(path);
            assertFalse(isFileCreated, "O arquivo de relatório foi criado, mas não deveria ter sido, pois o paciente não existe.");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Erro ao verificar a criação do arquivo de relatório.");
        }
    }
    @Test
    @DisplayName("Tentar salvar atendimento com data passada para o paciente 'bob.brown654'")
    void testSaveAppointmentWithPastDate() {
        driver.get(pacienteUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement patientNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[1]/label/input")));
        patientNameField.sendKeys("bob.brown654");

        WebElement patientNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/input")));
        patientNumberField.sendKeys("13690000");

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/button")));
        confirmButton.click();

        WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[3]/label/input")));
        dateField.sendKeys("2023-01-01");

        WebElement timeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[4]/label/input")));
        timeField.sendKeys("10:30");

        WebElement specialtyField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[5]/label/input")));
        specialtyField.sendKeys("Otorrinolaringologista");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/button[1]")));
        saveButton.click();

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept(); // Fecha o alerta
            fail("Alerta inesperado foi exibido: " + alertText);
        } catch (TimeoutException ignored) {
        }

        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/p")));
            String errorText = errorMessage.getText();
            assertEquals("A data da consulta deve ser maior que a data atual.", errorText,
                    "A mensagem de erro não é a esperada!");
        } catch (TimeoutException e) {
            fail("A mensagem de erro esperada não foi exibida.");
        }
    }

    @Test
    @DisplayName("Tentar adicionar um CEP inválido e mostrar a mensagem de erro")
    void testAddInvalidCep() {
        driver.get(pacienteUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement patientNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[1]/label/input")));
        patientNameField.sendKeys("bob.brown654");

        WebElement patientCepField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/input")));
        patientCepField.sendKeys("00000000");

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/button")));
        confirmButton.click();

        boolean isErrorDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div"))).isDisplayed();

        assertTrue(isErrorDisplayed, "A mensagem de erro para o CEP inválido não foi exibida!");
    }
    @Test
    @DisplayName("Não conseguir salvar atendimento sem nome do paciente preenchido")
    void testSaveAppointmentWithoutPatientName() {
        driver.get(pacienteUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement patientNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/input")));
        patientNumberField.sendKeys("13690000");

        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/button")));
        confirmButton.click();

        WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[3]/label/input")));
        dateField.sendKeys("2025-01-01");

        WebElement timeField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[4]/label/input")));
        timeField.sendKeys("10:30");

        WebElement specialtyField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[5]/label/input")));
        specialtyField.sendKeys("Otorrinolaringologista");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/button[1]")));
        saveButton.click();

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div")));

        boolean isErrorDisplayed = errorElement.isDisplayed();

        assertTrue(isErrorDisplayed, "A mensagem de erro para ausência do nome do paciente não foi exibida!");
    }
    @Test
    @DisplayName("Testar Botão de Voltar na Página Paciente")
    void testBackButtonOnPacientPage() {

        driver.get(pacienteUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/button[2]")));
            backButton.click();

            wait.until(ExpectedConditions.urlToBe("https://sitetc1kaykywaleskabreno.vercel.app/login"));

            String currentUrl = driver.getCurrentUrl();
            assertEquals("https://sitetc1kaykywaleskabreno.vercel.app/login", currentUrl, "A URL atual não é a esperada!");
        } catch (TimeoutException e) {
            fail("O botão de voltar não redirecionou para a página de login dentro do tempo esperado. Verifique se a funcionalidade está implementada corretamente.");
        }
    }

}
