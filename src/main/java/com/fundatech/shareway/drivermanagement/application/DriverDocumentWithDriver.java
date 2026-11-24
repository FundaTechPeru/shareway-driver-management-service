package com.fundatech.shareway.drivermanagement.application;

import com.fundatech.shareway.drivermanagement.domain.model.DriverDocument;
import com.fundatech.shareway.drivermanagement.domain.model.User;

/**
 * A driver document together with the driver that submitted it. The driver is resolved from the
 * {@code driverId} of the document so the administration panel can show who is waiting for a review
 * without a second call. {@code driver} may be {@code null} if the user no longer exists.
 */
public record DriverDocumentWithDriver(DriverDocument document, User driver) {
}
