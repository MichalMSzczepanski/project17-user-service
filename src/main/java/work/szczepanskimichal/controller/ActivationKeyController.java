package work.szczepanskimichal.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.szczepanskimichal.model.ActivationKey;
import work.szczepanskimichal.service.ActivationKeyService;

import java.util.UUID;

@RestController
@RequestMapping("/activation-key")
@RequiredArgsConstructor
public class ActivationKeyController {

    private final ActivationKeyService activationKeyService;

    @GetMapping("/{userId}")
    ActivationKey getByUserId(@PathVariable("userId") UUID userId) {
        return activationKeyService.getByUserId(userId);
    }

}
