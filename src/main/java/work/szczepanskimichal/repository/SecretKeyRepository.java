package work.szczepanskimichal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import work.szczepanskimichal.enums.KeyType;
import work.szczepanskimichal.model.SecretKey;

import java.util.Optional;
import java.util.UUID;

public interface SecretKeyRepository extends MongoRepository<SecretKey, String> {

    Optional<SecretKey> getKeyByUserIdAndKeyType(UUID userId, KeyType keyType);

    int deleteByKeyAndUserIdAndKeyType(UUID key, UUID userId, KeyType keyType);

    Optional<SecretKey> getSecretKeyByKeyAndUserId(UUID key, UUID userId);

}

