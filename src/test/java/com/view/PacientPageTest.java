package com.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

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
}
