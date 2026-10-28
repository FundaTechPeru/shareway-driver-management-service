package com.fundatech.shareway.drivermanagement.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import com.fundatech.shareway.drivermanagement.domain.repository.DriverDocumentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DriverDocumentRepositoryAdapter implements DriverDocumentRepository {

    private final SpringDataDriverDocumentRepository jpaRepository;

    public DriverDocumentRepositoryAdapter(SpringDataDriverDocumentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DriverDocument save(DriverDocument document) {
        return jpaRepository.save(document);
    }

    @Override
    public Optional<DriverDocument> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<DriverDocument> findByIdAndDriverId(Long id, Long driverId) {
        return jpaRepository.findByIdAndDriverId(id, driverId);
    }

    @Override
    public List<DriverDocument> findAllByDriverId(Long driverId) {
        return jpaRepository.findAllByDriverIdOrderByIdAsc(driverId);
    }
}
