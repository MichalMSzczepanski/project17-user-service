package work.szczepanskimichal.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Document(collection = "login_responses")
public class LoginResponse {
    @Id
    private String id;
    private UUID userId;
    private String email;
    private HttpStatus responseStatus;
    private String message;
    private LocalDateTime timeStamp;
}
