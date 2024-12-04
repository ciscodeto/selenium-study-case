package com.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorPageTest extends BaseTest {
    private final String doctorUrl = "https://sitetc1kaykywaleskabreno.vercel.app/medico";

    @Test
    @DisplayName("Tentar salvar atendimento com médico não existente verificando a lista na página Admin")
    void testSaveAppointmentWithNonExistentDoctor() {

        driver.get("https://sitetc1kaykywaleskabreno.vercel.app/admin");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> doctorList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul/li")));
        boolean doctorExists = doctorList.stream()
                .anyMatch(doctor -> doctor.getText().equals("Dr. Inexistente"));
        assertFalse(doctorExists, "O médico 'Dr. Inexistente' foi encontrado na lista, mas não deveria existir.");

        driver.get(doctorUrl);

        WebElement doctorNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[1]/label/input")));
        doctorNameField.sendKeys("Dr. Inexistente");

        WebElement doctorSelectField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/label/select/option[2]")));
        doctorSelectField.click();

        WebElement patientNumberField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[3]/input")));
        patientNumberField.sendKeys("13690000");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[3]/button")));
        saveButton.click();

        WebElement observationsField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[4]/label/textarea")));
        observationsField.sendKeys("Paciente liberado");

        String currentUrl = driver.getCurrentUrl();
        assertNotEquals("https://sitetc1kaykywaleskabreno.vercel.app/confirmacao", currentUrl,
                "A URL não deveria ser de confirmação de atendimento salvo!");
    }
}