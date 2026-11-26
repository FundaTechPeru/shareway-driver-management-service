package com.fundatech.shareway.drivermanagement.bdd;

import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import io.cucumber.java.en.When;

public class PublicProfileSteps {

    private final ScenarioContext context;
    private final UserRepository userRepository;

    public PublicProfileSteps(ScenarioContext context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    @When("I view the public profile of the driver {string}")
    public void iViewThePublicProfileOfTheDriver(String email) {
        User user = userRepository.findByEmail(User.normalizeEmail(email))
                .orElseThrow(() -> new IllegalStateException("No user registered with email " + email));
        context.getRequest("/api/v1/drivers/" + user.getId() + "/public-profile");
    }
}
