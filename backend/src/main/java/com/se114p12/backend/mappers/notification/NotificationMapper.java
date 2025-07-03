package com.se114p12.backend.mappers.notification;

import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.entities.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "userId", ignore = true)
    NotificationResponseDTO toDTO(Notification notification);
}

