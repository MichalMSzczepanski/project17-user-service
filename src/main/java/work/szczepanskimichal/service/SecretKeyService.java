package work.szczepanskimichal.service;

import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.exception.SecretKeyException;
import work.szczepanskimichal.model.key.KeyType;
import work.szczepanskimichal.exception.SecretKeyNotAssignedException;
import work.szczepanskimichal.model.key.SecretKey;
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
        log.info("Successfully assigned secret key of type {} to user: {}", keyType, userId);
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

    public void deleteByUserIdAndKey(UUID secretKey, UUID userId) {
        var updatedRecords = secretKeyRepository.deleteByKeyAndUserId(secretKey, userId);
        if (updatedRecords > 0) {
            log.info("Successfully deleted secret key for user: {}", userId);
        } else {
            throw new InvalidSecretKeyException();
        }
    }

    public void deleteByUserId(UUID userId) {
        secretKeyRepository.deleteAllByUserId(userId);
    }
}
