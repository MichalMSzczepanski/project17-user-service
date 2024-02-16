package work.szczepanskimichal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import work.szczepanskimichal.model.ActivationKey;

import java.util.Optional;
import java.util.UUID;

public interface ActivationKeyRepository extends MongoRepository<ActivationKey, String> {

    Optional<ActivationKey> getKeyByUserId(UUID userId);

    int deleteByKeyAndUserId(UUID activationKey, UUID userId);

    boolean existsByKeyAndUserId(UUID activationKey, UUID userId);

}

