package com.fundatech.shareway.drivermanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;

public interface DriverDocumentRepository {

    DriverDocument save(DriverDocument document);

    Optional<DriverDocument> findById(Long id);

    Optional<DriverDocument> findByIdAndDriverId(Long id, Long driverId);

    List<DriverDocument> findAllByDriverId(Long driverId);
}
