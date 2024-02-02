package work.szczepanskimichal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import work.szczepanskimichal.entity.UserUpdateDto;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.entity.UserDto;
import work.szczepanskimichal.service.HashingService;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    HashingService hashingservice;

    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    public abstract User toEntity(UserDto userDto);

    public abstract UserDto toUserDto(User user);

    public abstract UserUpdateDto toUserUpdateDto(User user);

    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    public abstract User toEntity(UserUpdateDto userUpdateDto);

    @Named("hashPassword")
    public String hashPassword(String password) {
        return hashingservice.hashPassword(password);
    }
}
