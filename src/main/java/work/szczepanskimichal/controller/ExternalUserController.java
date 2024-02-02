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

    @PatchMapping("/activate/{id}")
    void activateUser(@PathVariable("id") UUID userId) {
        userService.activateUser(userId);
    }
}
