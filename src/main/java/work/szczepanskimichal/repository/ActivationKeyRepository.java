package work.szczepanskimichal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import work.szczepanskimichal.entity.ActivationKey;

import java.util.Optional;
import java.util.UUID;

public interface ActivationKeyRepository extends MongoRepository<ActivationKey, String> {

    Optional<ActivationKey> getKeyByUserId(UUID userId);

    Optional<ActivationKey> findByKeyAndUserId(UUID activationKey, UUID userId);

    void deleteByKeyAndUserId(UUID activationKey, UUID userId);

}

