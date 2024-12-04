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
                ExpectedConditions.presenceOfElementLocated(By.xpath("(//ul)[1]/li[last()]"))
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

        List<WebElement> initialDoctorList = driver.findElements(By.xpath("(//ul)[1]/li"));
        long initialCount = initialDoctorList.stream()
                .filter(element -> element.getText().contains(existingDoctorUsername))
                .count();

        usernameField.sendKeys(existingDoctorUsername);
        passwordField.sendKeys(existingDoctorPassword);
        addButton.click();

        handleAlert();

        List<WebElement> updatedDoctorList = driver.findElements(By.xpath("(//ul)[1]/li"));
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

        int initialListSize = driver.findElements(By.xpath("(//ul)[1]/li")).size();

        usernameField.sendKeys(newDoctorUsername);
        addButton.click();

        handleAlert();

        int updatedListSize = driver.findElements(By.xpath("(//ul)[1]/li")).size();

        assertEquals(initialListSize, updatedListSize, "O médico foi adicionado mesmo sem senha!");
    }

    @Test
    @DisplayName("Should Not Add Doctor Without Name")
    void shouldNotAddDoctorWithoutName() {
        driver.get(adminUrl);

        WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(0);
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Médico']"));

        String newDoctorPassword = generatePassword();

        List<WebElement> initialDoctorList = driver.findElements(By.xpath("(//ul)[1]/li"));

        boolean hasExistingEmptyNameDoctor = initialDoctorList.stream().anyMatch(doctor -> {
            String doctorText = doctor.getText();
            System.out.println("Texto do médico existente: " + doctorText); // Para debug
            return doctorText.contains("Usuário: ,");
        });
        assertFalse(hasExistingEmptyNameDoctor, "Já existe um médico com nome vazio na lista antes do teste!");

        passwordField.sendKeys(newDoctorPassword);
        addButton.click();

        handleAlert();

        List<WebElement> updatedDoctorList = driver.findElements(By.xpath("(//ul)[1]/li"));

        List<WebElement> newDoctors = updatedDoctorList.subList(initialDoctorList.size(), updatedDoctorList.size());
        boolean hasNewEmptyNameDoctor = newDoctors.stream().anyMatch(doctor -> {
            String doctorText = doctor.getText();
            System.out.println("Texto do novo médico: " + doctorText); // Para debug
            return doctorText.contains("Usuário: ,");
        });

        assertFalse(hasNewEmptyNameDoctor, "Um médico com nome vazio foi adicionado após a tentativa!");
    }

    @Test
    @DisplayName("Should Delete Doctor")
    void shouldDeleteDoctor() {
        driver.get(adminUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastDoctorElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("(//ul)[1]/li[last()]"))
        );

        String lastDoctorText = lastDoctorElement.getText();
        WebElement deleteButton = lastDoctorElement.findElement(By.xpath(".//button[contains(@class, 'buttonlixeira')]"));

        deleteButton.click();

        handleAlert();

        boolean doctorDeleted = driver.findElements(By.xpath("(//ul)[1]/li"))
                .stream()
                .noneMatch(element -> element.getText().contains(lastDoctorText));

        assertTrue(doctorDeleted, "O médico ainda está presente na lista após a exclusão!");
    }

    @Test
    @DisplayName("Should Add New Patient")
    void shouldAddPatient() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElements(By.xpath("//input[@placeholder='Usuário']")).get(1);
        WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(1);
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

        String newPatientUsername = generateUsername();
        String newPatientPassword = generatePassword();

        usernameField.sendKeys(newPatientUsername);
        passwordField.sendKeys(newPatientPassword);
        addButton.click();

        handleAlert();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastPatientElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("(//ul)[2]/li[last()]"))
        );

        String lastPatientText = lastPatientElement.getText();
        assertTrue(
                lastPatientText.contains(newPatientUsername) && lastPatientText.contains(newPatientPassword),
                "O último item da lista não corresponde ao paciente recém-adicionado!"
        );
    }

    @Test
    @DisplayName("Should Not Add Repeated Patient")
    void shouldNotAddRepeatedPatient() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElements(By.xpath("//input[@placeholder='Usuário']")).get(1);
        WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(1);
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

        String existingPatientUsername = "bob.brown654";
        String existingPatientPassword = "BobPass654!";

        List<WebElement> initialPatientList = driver.findElements(By.xpath("(//ul)[2]/li"));
        long initialCount = initialPatientList.stream()
                .filter(element -> element.getText().contains(existingPatientUsername))
                .count();

        usernameField.sendKeys(existingPatientUsername);
        passwordField.sendKeys(existingPatientPassword);
        addButton.click();

        handleAlert();

        List<WebElement> updatedPatientList = driver.findElements(By.xpath("(//ul)[2]/li"));
        long updatedCount = updatedPatientList.stream()
                .filter(element -> element.getText().contains(existingPatientUsername))
                .count();

        assertEquals(initialCount, updatedCount, "O paciente duplicado foi adicionado novamente!");
    }

    @Test
    @DisplayName("Should Not Add Patient Without Password")
    void shouldNotAddPatientWithoutPassword() {
        driver.get(adminUrl);

        WebElement usernameField = driver.findElements(By.xpath("//input[@placeholder='Usuário']")).get(1);
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

        String newPatientUsername = generateUsername();

        List<WebElement> initialPatientList = driver.findElements(By.xpath("(//ul)[2]/li"));

        for (WebElement patient : initialPatientList) {
            if (!patient.getText().contains("Senha: ")) {
                WebElement deleteButton = patient.findElement(By.xpath(".//button[contains(@class, 'buttonlixeira')]"));
                deleteButton.click();
                handleAlert();
            }
        }

        long initialCount = driver.findElements(By.xpath("(//ul)[2]/li"))
                .stream()
                .filter(element -> element.getText().contains(newPatientUsername))
                .count();

        usernameField.sendKeys(newPatientUsername);
        addButton.click();

        handleAlert();

        long updatedCount = driver.findElements(By.xpath("(//ul)[2]/li"))
                .stream()
                .filter(element -> element.getText().contains(newPatientUsername))
                .count();

        assertEquals(initialCount, updatedCount, "O paciente foi adicionado mesmo sem senha!");
    }

    @Test
    @DisplayName("Should Not Add Patient Without Name")
    void shouldNotAddPatientWithoutName() {
        driver.get(adminUrl);

        WebElement passwordField = driver.findElements(By.xpath("//input[@placeholder='Senha']")).get(1);
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Adicionar Paciente']"));

        String newPatientPassword = generatePassword();

        boolean hasExistingEmptyNamePatient = driver.findElements(By.xpath("(//ul)[2]/li"))
                .stream()
                .anyMatch(patient -> patient.getText().contains("Usuário: ,"));
        assertFalse(hasExistingEmptyNamePatient, "Já existe um paciente com nome vazio na lista antes do teste!");

        passwordField.sendKeys(newPatientPassword);
        addButton.click();

        handleAlert();

        boolean hasNewEmptyNamePatient = driver.findElements(By.xpath("(//ul)[2]/li"))
                .stream()
                .anyMatch(patient -> patient.getText().contains("Usuário: ,"));
        assertFalse(hasNewEmptyNamePatient, "Um paciente com nome vazio foi adicionado após a tentativa!");
    }

    @Test
    @DisplayName("Should Delete Patient")
    void shouldDeletePatient() {
        driver.get(adminUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement lastPatientElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("(//ul)[2]/li[last()]"))
        );

        String lastPatientText = lastPatientElement.getText();
        WebElement deleteButton = lastPatientElement.findElement(By.xpath(".//button[contains(@class, 'buttonlixeira')]"));

        deleteButton.click();

        handleAlert();

        boolean patientDeleted = driver.findElements(By.xpath("(//ul)[2]/li"))
                .stream()
                .noneMatch(element -> element.getText().contains(lastPatientText));

        assertTrue(patientDeleted, "O paciente ainda está presente na lista após a exclusão!");
    }
}