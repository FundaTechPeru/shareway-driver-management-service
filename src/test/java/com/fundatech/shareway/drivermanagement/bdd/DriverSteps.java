package com.fundatech.shareway.drivermanagement.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class DriverSteps {

    static final String DEFAULT_PASSWORD = "Secret123";
    private static final String DRIVER_PROFILE_PATH = "/api/v1/users/me/driver-profile";

    private final ScenarioContext context;

    public DriverSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        context.clearToken();
    }

    @Given("I have registered my driver profile with license number {string}")
    public void iHaveRegisteredMyDriverProfile(String licenseNumber) {
        registerDriverProfile(context, licenseNumber);
    }

    @Given("a driver is registered with email {string} and license number {string}")
    public void aDriverIsRegistered(String email, String licenseNumber) {
        AuthSteps.registerUser(context, email, DEFAULT_PASSWORD);
        AuthSteps.logIn(context, email, DEFAULT_PASSWORD);
        registerDriverProfile(context, licenseNumber);
    }

    @Given("I am logged in as a driver with email {string}")
    public void iAmLoggedInAsADriver(String email) {
        String licenseNumber = "D%09d".formatted(Math.floorMod(email.hashCode(), 1_000_000_000));
        aDriverIsRegistered(email, licenseNumber);
    }

    @Given("I am logged in as a passenger with email {string}")
    public void iAmLoggedInAsAPassenger(String email) {
        AuthSteps.registerUser(context, email, DEFAULT_PASSWORD);
        AuthSteps.logIn(context, email, DEFAULT_PASSWORD);
    }

    @When("I register my driver profile with license number {string} and emergency contact {string}, {string}, {string}")
    public void iRegisterMyDriverProfile(String licenseNumber, String name, String phone, String relationship) {
        Map<String, Object> contact = new HashMap<>();
        contact.put("name", name);
        contact.put("phone", phone);
        contact.put("relationship", relationship);
        context.postJson(DRIVER_PROFILE_PATH, Map.of("licenseNumber", licenseNumber, "emergencyContact", contact));
    }

    @When("I register my driver profile with license number {string} and no emergency contact")
    public void iRegisterMyDriverProfileWithoutContact(String licenseNumber) {
        context.postJson(DRIVER_PROFILE_PATH, Map.of("licenseNumber", licenseNumber));
    }

    @When("I request my driver profile")
    public void iRequestMyDriverProfile() {
        context.getRequest(DRIVER_PROFILE_PATH);
    }

    static void registerDriverProfile(ScenarioContext context, String licenseNumber) {
        context.postJson(DRIVER_PROFILE_PATH, Map.of(
                "licenseNumber", licenseNumber,
                "emergencyContact", Map.of("name", "Rosa Diaz", "phone", "+51911222333", "relationship", "Mother")));
        assertThat(context.lastStatus()).as(context.lastBody()).isEqualTo(201);
    }
}
