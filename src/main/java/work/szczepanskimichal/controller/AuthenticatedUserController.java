package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.user.dto.UserCreateDto;
import work.szczepanskimichal.model.user.dto.UserDto;
import work.szczepanskimichal.model.user.dto.UserUpdateDto;
import work.szczepanskimichal.model.user.dto.UserUpdatePasswordDto;
import work.szczepanskimichal.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class AuthenticatedUserController {

    private final UserService userService;

    @PostMapping("")
    public UserDto create(@RequestBody UserCreateDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable UUID userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/update/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UserUpdateDto dto) {
        return userService.updateUser(userId, dto);
    }

    @PatchMapping("/update-password/{email}")
    public ResponseEntity<String> updatePassword(@PathVariable String email,
                                                 @RequestBody UserUpdatePasswordDto dto) {
        userService.updatePassword(email, dto);
        return ResponseEntity.ok("Successfully updated password");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Successfully deleted user");
    }
}
