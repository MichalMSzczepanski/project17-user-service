package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.enums.KeyType;
import work.szczepanskimichal.exception.SecretKeyNotAssignedException;
import work.szczepanskimichal.model.SecretKey;
import work.szczepanskimichal.exception.InvalidSecretKeyException;
import work.szczepanskimichal.repository.SecretKeyRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecretKeyService {

    private final SecretKeyRepository secretKeyRepository;

    public SecretKey getByUserIdAndKeyType(UUID userId, KeyType keyType) {
        return secretKeyRepository.getKeyByUserIdAndKeyType(userId, keyType)
                .orElseThrow(() -> new SecretKeyNotAssignedException(userId));
    }

    public SecretKey assignSecretKeyToUser(UUID userId, KeyType keyType) {
        var newKey = SecretKey.builder()
                .key(UUID.randomUUID())
                .keyType(keyType)
                .userId(userId)
                .creationDate(LocalDateTime.now())
                .build();
        var key = secretKeyRepository.save(newKey);
        log.info("Successfully created secret key of type {} for user: {}", keyType, userId);
        return key;
    }

    public SecretKey validateSecretKey(UUID userId, UUID key) {
        return secretKeyRepository.getSecretKeyByKeyAndUserId(key, userId).orElseThrow(InvalidSecretKeyException::new);
    }

    public void deleteByUserIdAndKey(UUID secretKey, UUID userId, KeyType keyType) {
        var updatedRecords = secretKeyRepository.deleteByKeyAndUserIdAndKeyType(secretKey, userId, keyType);
        if (updatedRecords > 0) {
            log.info("Successfully deleted secret key for user: {}", userId);
        } else {
            throw new InvalidSecretKeyException();
        }
    }
}
