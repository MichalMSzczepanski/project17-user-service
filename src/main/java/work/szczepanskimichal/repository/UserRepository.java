package work.szczepanskimichal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import work.szczepanskimichal.model.user.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email")
    int userWithEmailExists(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.active = true WHERE u.id = :id")
    int activateUser(@Param("id") UUID id);

    @Query("SELECT u.active FROM User u WHERE u.email = :email")
    Optional<Boolean> isUserActive(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password WHERE u.email  = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    Optional<String> findPasswordByEmail(@Param("email") String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<UUID> findIdByEmail(@Param("email") String email);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    Optional<String> findEmailById(@Param("id") UUID id);

}
