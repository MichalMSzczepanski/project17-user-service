package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.model.*;
import work.szczepanskimichal.model.dto.UserCreateDto;
import work.szczepanskimichal.model.dto.UserDto;
import work.szczepanskimichal.model.dto.UserLoginDto;
import work.szczepanskimichal.model.dto.UserResetPasswordDetailsDto;
import work.szczepanskimichal.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDto dto) {
        return userService.login(dto);
    }

    //logout
    //invalidate jwt

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserResetPasswordDetailsDto dto) {
        userService.resetPassword(dto.getEmail());
        return ResponseEntity.ok().body(String.format("Sent password reset link to email: %s", dto.getEmail()));
    }

    @PostMapping("/set-new-password/{secretKey}")
    public ResponseEntity<String> setNewPassword(@PathVariable("secretKey") UUID secretKey,
                                                 @RequestBody UserResetPasswordDetailsDto dto) {
        userService.setNewPassword(secretKey, dto);
        return ResponseEntity.ok().body("Successfully updated password");
    }

    @PatchMapping("/activate/{userId}/{secretKey}")
    public ResponseEntity<String> activateUser(@PathVariable("userId") UUID userId,
                                               @PathVariable("secretKey") UUID secretKey) {
        userService.activateUser(userId, secretKey);
        return ResponseEntity.ok("Successfully activated user");
    }
}
