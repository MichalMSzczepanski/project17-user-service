package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.*;
import work.szczepanskimichal.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExternalUserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.register(userCreateDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(String.format("Sent password reset link to email: %s", email));
    }

    @PostMapping("/set-new-password/{activationKey}")
    public ResponseEntity<String> setNewPassword(@PathVariable("activationKey") UUID activationKey,
                                                 @RequestBody UserResetPasswordDto userUpdatePasswordDto) {
        userService.setNewPassword(activationKey, userUpdatePasswordDto);
        return ResponseEntity.ok().body("Successfully updated password");
    }

    @PatchMapping("/activate/{userId}/{activationKey}")
    public ResponseEntity<String> activateUser(@PathVariable("userId") UUID userId,
                                               @PathVariable("activationKey") UUID activationKey) {
        userService.activateUser(userId, activationKey);
        return ResponseEntity.ok("Successfully activated user");
    }
}
