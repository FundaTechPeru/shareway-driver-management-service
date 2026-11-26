package com.fundatech.shareway.drivermanagement.application;

import java.util.List;
import java.util.UUID;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentStatus;
import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import com.fundatech.shareway.drivermanagement.domain.repository.DocumentStorage;
import com.fundatech.shareway.drivermanagement.domain.repository.DriverDocumentRepository;
import com.fundatech.shareway.drivermanagement.domain.repository.UserRepository;
import com.fundatech.shareway.drivermanagement.shared.exception.DomainValidationException;
import com.fundatech.shareway.drivermanagement.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DriverDocumentService {

    static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024;
    private static final int MAX_FILENAME_LENGTH = 255;

    private final DriverDocumentRepository documentRepository;
    private final DocumentStorage documentStorage;
    private final UserRepository userRepository;

    public DriverDocumentService(DriverDocumentRepository documentRepository, DocumentStorage documentStorage,
                                 UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.documentStorage = documentStorage;
        this.userRepository = userRepository;
    }

    @Transactional
    public DriverDocument upload(UUID driverId, DocumentUpload upload) {
        byte[] content = upload.content();
        if (content == null || content.length == 0) {
            throw new DomainValidationException("file", "File must not be empty");
        }
        if (content.length > MAX_FILE_SIZE_BYTES) {
            throw new DomainValidationException("file", "File must not exceed 5 MB");
        }
        DocumentFileFormat format = DocumentFileFormat.detect(upload.contentType(), content)
                .orElseThrow(() -> new DomainValidationException("file", "File must be a PDF, JPEG or PNG"));

        String storageKey = documentStorage.store(content, format.extension());
        try {
            return documentRepository.save(DriverDocument.upload(driverId, upload.type(),
                    sanitizeFilename(upload.originalFilename(), format), format.contentType(), content.length, storageKey));
        } catch (RuntimeException ex) {
            documentStorage.delete(storageKey);
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<DriverDocument> listForDriver(UUID driverId) {
        return documentRepository.findAllByDriverId(driverId);
    }

    /**
     * Lists documents for the administration panel, each one paired with the driver that submitted it.
     * A {@code null} status returns every document; otherwise only those in that status.
     */
    @Transactional(readOnly = true)
    public List<DriverDocumentWithDriver> listByStatus(DocumentStatus status) {
        List<DriverDocument> documents = status == null
                ? documentRepository.findAll()
                : documentRepository.findAllByStatus(status);
        return documents.stream()
                .map(document -> new DriverDocumentWithDriver(document,
                        userRepository.findById(document.getDriverId()).orElse(null)))
                .toList();
    }

    @Transactional(readOnly = true)
    public DriverDocument getForDriver(UUID driverId, UUID documentId) {
        return documentRepository.findByIdAndDriverId(documentId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    private static String sanitizeFilename(String originalFilename, DocumentFileFormat format) {
        String filename = StringUtils.getFilename(StringUtils.cleanPath(originalFilename == null ? "" : originalFilename));
        if (!StringUtils.hasText(filename)) {
            return "document" + format.extension();
        }
        return filename.length() > MAX_FILENAME_LENGTH ? filename.substring(filename.length() - MAX_FILENAME_LENGTH) : filename;
    }
}
