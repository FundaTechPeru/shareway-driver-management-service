package com.fundatech.shareway.drivermanagement.application;

import com.fundatech.shareway.drivermanagement.domain.model.DocumentType;

public record DocumentUpload(DocumentType type, String originalFilename, String contentType, byte[] content) {
}
