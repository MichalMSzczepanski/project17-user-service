package work.szczepanskimichal.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.userservice.entity.UserDto;
import work.szczepanskimichal.userservice.mapper.UserMapper;
import work.szczepanskimichal.userservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        //todo validate all fields user creation
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }
}
