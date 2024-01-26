package work.szczepanskimichal.mapper;

import org.mapstruct.Mapper;
import work.szczepanskimichal.entity.UserUpdateDto;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.entity.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toUserDto(User user);

    User toEntity(UserUpdateDto userUpdateDto);

    UserUpdateDto toUserUpdateDto(User user);

}
