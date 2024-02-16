package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.ActivationKey;
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
        return activationKeyRepository.getKeyByUserId(userId).orElseThrow(InvalidActivationKeyException::new);
    }

    public ActivationKey assignActivationKeyToUser(UUID userId) {
        var newKey = ActivationKey.builder()
                .key(UUID.randomUUID())
                .userId(userId)
                .creationDate(LocalDateTime.now())
                .build();
        var key = activationKeyRepository.save(newKey);
        //todo email user with activation key
        log.info("Successfully created activation key for user: {}", userId);
        return key;
    }

    public boolean isActivationKeyValid(UUID userId, UUID key) {
        return activationKeyRepository.existsByKeyAndUserId(userId, key);

    }

    public void deleteByUserIdAndKey(UUID activationKey, UUID userId) {
        var updatedRecords = activationKeyRepository.deleteByKeyAndUserId(activationKey, userId);
        if (updatedRecords > 0) {
            log.info("Successfully deleted activation key for user: {}", userId);
        } else {
            throw new InvalidActivationKeyException();
        }
    }
}
