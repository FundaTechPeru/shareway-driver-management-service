package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDriverDocumentRepository extends JpaRepository<DriverDocument, UUID> {

    Optional<DriverDocument> findByIdAndDriverId(UUID id, UUID driverId);

    List<DriverDocument> findAllByDriverIdOrderByUploadedAtAsc(UUID driverId);
}
