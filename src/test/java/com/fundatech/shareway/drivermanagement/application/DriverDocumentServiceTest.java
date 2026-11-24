package com.fundatech.shareway.drivermanagement.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentStatus;
import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;
import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import com.fundatech.shareway.drivermanagement.domain.model.EmergencyContact;
import com.fundatech.shareway.drivermanagement.domain.model.User;
import com.fundatech.shareway.drivermanagement.domain.repository.DocumentStorage;
import com.fundatech.shareway.drivermanagement.domain.repository.DriverDocumentRepository;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;

class DriverDocumentServiceTest {

    private final DriverDocumentRepository documentRepository = mock(DriverDocumentRepository.class);
    private final DocumentStorage documentStorage = mock(DocumentStorage.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final DriverDocumentService service =
            new DriverDocumentService(documentRepository, documentStorage, userRepository);

    @Test
    void listByStatusPairsEachDocumentWithItsDriver() {
        UUID driverId = UUID.randomUUID();
        DriverDocument document = document(driverId, DocumentType.DRIVERS_LICENSE);
        User driver = driver("maria.lopez@upc.edu.pe", "Maria Lopez", "Q1234567");
        when(documentRepository.findAllByStatus(DocumentStatus.PENDING)).thenReturn(List.of(document));
        when(userRepository.findById(driverId)).thenReturn(Optional.of(driver));

        List<DriverDocumentWithDriver> result = service.listByStatus(DocumentStatus.PENDING);

        assertThat(result).singleElement().satisfies(pair -> {
            assertThat(pair.document()).isSameAs(document);
            assertThat(pair.driver()).isSameAs(driver);
        });
        verifyNoInteractions(documentStorage);
    }

    @Test
    void listByStatusLeavesTheDriverNullWhenTheUserIsGone() {
        UUID driverId = UUID.randomUUID();
        DriverDocument document = document(driverId, DocumentType.NATIONAL_ID);
        when(documentRepository.findAllByStatus(DocumentStatus.PENDING)).thenReturn(List.of(document));
        when(userRepository.findById(driverId)).thenReturn(Optional.empty());

        List<DriverDocumentWithDriver> result = service.listByStatus(DocumentStatus.PENDING);

        assertThat(result).singleElement().satisfies(pair -> {
            assertThat(pair.document()).isSameAs(document);
            assertThat(pair.driver()).isNull();
        });
    }

    @Test
    void listByStatusWithoutAStatusReturnsEveryDocument() {
        UUID driverId = UUID.randomUUID();
        DriverDocument document = document(driverId, DocumentType.CRIMINAL_RECORD);
        when(documentRepository.findAll()).thenReturn(List.of(document));
        when(userRepository.findById(driverId)).thenReturn(Optional.of(driver("jose@upc.edu.pe", "Jose Ramos", "R7654321")));

        List<DriverDocumentWithDriver> result = service.listByStatus(null);

        assertThat(result).hasSize(1);
        verify(documentRepository).findAll();
        verify(documentRepository, org.mockito.Mockito.never()).findAllByStatus(any());
    }

    private static DriverDocument document(UUID driverId, DocumentType type) {
        return DriverDocument.upload(driverId, type, type + ".pdf", "application/pdf", 1024, type + "-key");
    }

    private static User driver(String email, String fullName, String licenseNumber) {
        User user = User.registerPassenger(email, "hash", fullName, "+51987654321");
        user.registerAsDriver(licenseNumber, new EmergencyContact("Rosa", "+51911222333", "Mother"));
        return user;
    }
}
