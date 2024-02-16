package work.szczepanskimichal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import work.szczepanskimichal.model.LoginResponse;

public interface LoginResponseRepository extends MongoRepository<LoginResponse, String> {
}

