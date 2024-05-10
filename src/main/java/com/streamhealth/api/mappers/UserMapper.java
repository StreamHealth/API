package com.streamhealth.api.mappers;

import com.streamhealth.api.dtos.SignUpDto;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
