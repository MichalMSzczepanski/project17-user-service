package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.user.dto.UserDto;
import work.szczepanskimichal.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/internal/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/all-users")
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

}
