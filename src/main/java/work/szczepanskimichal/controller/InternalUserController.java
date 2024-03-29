package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.LoginResponse;
import work.szczepanskimichal.model.user.dto.UserDto;
import work.szczepanskimichal.model.user.dto.UserLoginDto;
import work.szczepanskimichal.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/internal/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PostMapping("/authenticate-credentials")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody UserLoginDto dto) {
        return userService.authenticate(dto);
    }

    @GetMapping("/all-users")
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

}
