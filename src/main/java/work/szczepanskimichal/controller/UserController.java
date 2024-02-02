package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.entity.UserUpdateDto;
import work.szczepanskimichal.entity.UserDto;
import work.szczepanskimichal.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable UUID userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/all-users")
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto patch(@PathVariable UUID userId, @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }

}
