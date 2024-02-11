package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.entity.ActivationKey;
import work.szczepanskimichal.exception.InvalidActivationKeyException;
import work.szczepanskimichal.repository.ActivationKeyRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivationKeyService {

    private final ActivationKeyRepository activationKeyRepository;

    public ActivationKey getByUserId(UUID userId) {
        return activationKeyRepository.getKeyByUserId(userId).orElseThrow(() -> new InvalidActivationKeyException(userId));
    }

    public ActivationKey assignActivationKeyToUser(UUID userId, String email) {
        var newKey = ActivationKey.builder()
                .key(UUID.randomUUID())
                .userId(userId)
                .creationDate(LocalDateTime.now())
                .build();
        var key = activationKeyRepository.save(newKey);
        //email user with activation key
        log.info("Successfully created activation key for user: {}", userId);
        return key;
    }

    public void findByActivationKeyAndUserIdAndDelete(UUID activationKey, UUID userId) {
        var key =
                activationKeyRepository.findByKeyAndUserId(activationKey, userId).orElseThrow(() -> new InvalidActivationKeyException(userId));
        activationKeyRepository.deleteByKeyAndUserId(key.getKey(), userId);
        log.info("Successfully deleted activation key for user: {}", userId);
    }
}
