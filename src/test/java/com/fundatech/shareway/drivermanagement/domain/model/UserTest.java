package com.fundatech.shareway.drivermanagement.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void registersPassengerWithNormalizedData() {
        User user = User.registerPassenger("  Ana.Torres@UPC.edu.pe ", "hash", " Ana Torres ", " +51987654321 ");

        assertThat(user.getEmail()).isEqualTo("ana.torres@upc.edu.pe");
        assertThat(user.getFullName()).isEqualTo("Ana Torres");
        assertThat(user.getPhone()).isEqualTo("+51987654321");
        assertThat(user.getRole()).isEqualTo(Role.PASSENGER);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void normalizesEmailToTrimmedLowerCase() {
        assertThat(User.normalizeEmail(" Luis.Rojas@UPC.EDU.PE ")).isEqualTo("luis.rojas@upc.edu.pe");
    }
}
