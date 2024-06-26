package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.user.dto.UserCreateDto;
import work.szczepanskimichal.model.user.dto.UserDto;
import work.szczepanskimichal.model.user.dto.UserResetPasswordDto;
import work.szczepanskimichal.model.user.dto.UserSetPasswordDto;
import work.szczepanskimichal.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/public/user")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserResetPasswordDto dto) {
        userService.resetPassword(dto.getUserId());
        return ResponseEntity.ok().body(String.format("Sent password reset link to user with id: %s", dto.getUserId()));
    }

    @PostMapping("/set-new-password/{secretKey}")
    public ResponseEntity<String> setNewPassword(@PathVariable("secretKey") UUID secretKey,
                                                 @RequestBody UserSetPasswordDto dto) {
        userService.setNewPassword(secretKey, dto);
        return ResponseEntity.ok().body("Successfully updated password");
    }

    @GetMapping("/activate/{userId}/{secretKey}")
    public ResponseEntity<String> activateUser(@PathVariable("userId") UUID userId,
                                               @PathVariable("secretKey") UUID secretKey) {
        userService.activateUser(userId, secretKey);
        return ResponseEntity.ok("Successfully activated user");
    }
}
