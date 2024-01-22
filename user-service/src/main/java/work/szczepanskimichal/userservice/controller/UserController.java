package work.szczepanskimichal.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.szczepanskimichal.userservice.entity.UserDto;
import work.szczepanskimichal.userservice.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserDto create(UserDto userDto) {
        return userService.createUser(userDto);
    }

    //read

    //update

    //delete

    //login

    //register

}
