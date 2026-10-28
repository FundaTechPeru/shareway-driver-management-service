package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDriverDocumentRepository extends JpaRepository<DriverDocument, Long> {

    Optional<DriverDocument> findByIdAndDriverId(Long id, Long driverId);

    List<DriverDocument> findAllByDriverIdOrderByIdAsc(Long driverId);
}
