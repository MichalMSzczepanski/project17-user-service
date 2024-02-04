package work.szczepanskimichal.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "activation_keys")
public class ActivationKey {

    @Id
    private
    String id;
    private UUID key;
    private UUID userId;
    @CreatedDate
    private LocalDateTime creationDate;
}


