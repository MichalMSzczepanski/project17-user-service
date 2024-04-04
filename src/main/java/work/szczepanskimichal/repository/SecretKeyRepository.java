package work.szczepanskimichal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import work.szczepanskimichal.model.key.KeyType;
import work.szczepanskimichal.model.key.SecretKey;

import java.util.Optional;
import java.util.UUID;

public interface SecretKeyRepository extends MongoRepository<SecretKey, String> {

    Optional<SecretKey> getKeyByUserIdAndKeyType(UUID userId, KeyType keyType);

    int deleteByKeyAndUserId(UUID secretKey, UUID userId);

    int deleteByKeyAndUserIdAndKeyType(UUID key, UUID userId, KeyType keyType);

    Optional<SecretKey> getSecretKeyByKeyAndUserId(UUID key, UUID userId);

    void deleteAllByUserId(UUID userID);
}

