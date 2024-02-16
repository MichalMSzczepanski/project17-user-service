package work.szczepanskimichal.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import work.szczepanskimichal.enums.KeyType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "secret_keys")
public class SecretKey {
    @Id
    private String id;
    private UUID key;
    private UUID userId;
    private KeyType keyType;
    private LocalDateTime creationDate;
}


