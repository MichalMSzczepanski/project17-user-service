package work.szczepanskimichal.model.key;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "secret_keys")
@CompoundIndex(name = "userId_keyType_index", def = "{'userId': 1, 'keyType': 1}", unique = true)
public class SecretKey {
    @Id
    private String id;
    private UUID key;
    private UUID userId;
    private KeyType keyType;
    private LocalDateTime creationDate;
}


