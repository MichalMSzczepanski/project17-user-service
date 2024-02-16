package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.UserCreateDto;
import work.szczepanskimichal.model.UserDto;
import work.szczepanskimichal.model.UserUpdateDto;
import work.szczepanskimichal.model.UserUpdatePasswordDto;
import work.szczepanskimichal.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PostMapping("")
    public UserDto create(@RequestBody UserCreateDto userDto) {
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

    @PatchMapping("/update/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UserUpdateDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @PatchMapping("/update-password/{email}")
    public ResponseEntity<String> updatePassword(@PathVariable String email,
                                                 @RequestBody UserUpdatePasswordDto userDto) {
        userService.updatePassword(email, userDto);
        return ResponseEntity.ok("Successfully updated password");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Successfully deleted user");
    }
}
