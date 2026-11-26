package com.fundatech.shareway.drivermanagement.shared;

import java.util.List;
import java.util.function.Function;

/**
 * Framework-agnostic page of results, so the domain layer does not depend on Spring Data types.
 *
 * @param content       items on the current page
 * @param page          zero-based page index
 * @param size          page size requested
 * @param totalElements total number of matching items across all pages
 * @param totalPages    total number of pages
 */
public record PagedResult<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public <R> PagedResult<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mapped = content.stream().<R>map(mapper).toList();
        return new PagedResult<>(mapped, page, size, totalElements, totalPages);
    }
}
