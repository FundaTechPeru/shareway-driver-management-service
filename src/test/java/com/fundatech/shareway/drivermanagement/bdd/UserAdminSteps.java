package com.fundatech.shareway.drivermanagement.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import com.fundatech.shareway.drivermanagement.domain.model.Role;
import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserAdminSteps {

    private static final String USERS_PATH = "/api/v1/users";

    private final ScenarioContext context;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminSteps(ScenarioContext context, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.context = context;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Given("the following users exist:")
    public void theFollowingUsersExist(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            String email = row.get("email");
            if (userRepository.existsByEmail(email)) {
                continue;
            }
            String hash = passwordEncoder.encode(DriverSteps.DEFAULT_PASSWORD);
            Role role = Role.valueOf(row.get("role"));
            User user = switch (role) {
                case ADMIN -> User.createAdmin(email, hash, row.get("fullName"), "+51900000001");
                case PASSENGER -> User.registerPassenger(email, hash, row.get("fullName"), "+51900000001");
                case DRIVER -> throw new IllegalArgumentException("Seeding DRIVER users is not supported here");
            };
            userRepository.save(user);
        }
    }

    @When("I list users")
    public void iListUsers() {
        context.getRequest(USERS_PATH);
    }

    @When("I list users filtered by role {string}")
    public void iListUsersFilteredByRole(String role) {
        context.getRequest(USERS_PATH + "?role=" + role);
    }

    @When("I list users filtered by full name {string}")
    public void iListUsersFilteredByFullName(String fullName) {
        context.getRequest(USERS_PATH + "?fullName=" + fullName);
    }

    @When("I list users filtered by email {string}")
    public void iListUsersFilteredByEmail(String email) {
        context.getRequest(USERS_PATH + "?email=" + email);
    }

    @When("I list users with page {int} and size {int}")
    public void iListUsersWithPageAndSize(int page, int size) {
        context.getRequest(USERS_PATH + "?page=" + page + "&size=" + size);
    }

    @Then("the user listing reports at least {int} total elements")
    public void theUserListingReportsAtLeastTotalElements(int minimum) {
        int total = ((Number) context.read("$.totalElements")).intValue();
        assertThat(total).isGreaterThanOrEqualTo(minimum);
    }

    @Then("every listed user has role {string}")
    public void everyListedUserHasRole(String role) {
        List<String> roles = context.read("$.content[*].role");
        assertThat(roles).isNotEmpty().allMatch(role::equals);
    }

    @Then("the user listing contains the email {string}")
    public void theUserListingContainsTheEmail(String email) {
        List<String> emails = context.read("$.content[*].email");
        assertThat(emails).contains(email);
    }

    @Then("the user listing page contains {int} items")
    public void theUserListingPageContainsItems(int count) {
        List<?> content = context.read("$.content");
        assertThat(content).hasSize(count);
    }
}
