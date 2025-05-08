package com.se114p12.backend.mapper;

import org.mapstruct.MappingTarget;

/**
 * GenericMapper interface for mapping between DTOs and entities.
 * @param <I> : Inbound DTO (request)
 * @param <E> : Entity
 * @param <O> : Outbound DTO (response)
 */
public interface GenericMapper<E, I, O> {
    E requestToEntity(I request);

    O entityToResponse(E entity);

    E partialUpdate(I request, @MappingTarget E entity);
}
