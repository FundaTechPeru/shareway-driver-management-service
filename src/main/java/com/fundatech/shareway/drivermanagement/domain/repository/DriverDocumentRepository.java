package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;

public interface DriverDocumentRepository {

    DriverDocument save(DriverDocument document);

    Optional<DriverDocument> findById(UUID id);

    Optional<DriverDocument> findByIdAndDriverId(UUID id, UUID driverId);

    List<DriverDocument> findAllByDriverId(UUID driverId);
}
