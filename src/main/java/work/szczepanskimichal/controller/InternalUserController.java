package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.entity.UserCreateDto;
import work.szczepanskimichal.entity.UserDto;
import work.szczepanskimichal.entity.UserUpdateDto;
import work.szczepanskimichal.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PostMapping("")
    public UserDto create(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
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

//    @PatchMapping("/{userId}")
//    public UserCreateDto patch(@PathVariable UUID userId, @RequestBody UserUpdatePasswordDto userUpdateDto) {
//        return userService.updatePassword(userId, userUpdateDto);
//    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
