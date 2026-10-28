package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.dto.UserDto;
import com.vic8b.medicalclinic.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(CreateUserCommand command);
}
