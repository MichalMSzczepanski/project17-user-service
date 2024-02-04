package work.szczepanskimichal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.szczepanskimichal.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExternalUserController {

    private final UserService userService;

    //login

    //register

    //forgotPassword

    @PatchMapping("/activate/{userId}/{activationKey}")
    void activateUser(@PathVariable("userId") UUID userId, @PathVariable("activationKey") UUID activationKey) {
        userService.activateUser(userId, activationKey);
    }
}
