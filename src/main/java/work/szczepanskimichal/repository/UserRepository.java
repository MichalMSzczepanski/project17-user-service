package work.szczepanskimichal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import work.szczepanskimichal.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email")
    int userWithEmailExists(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.active = true WHERE u.id = :id")
    int activateUser(@Param("id") UUID id);

    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String findPasswordById(@Param("id") UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    int updatePassword(@Param("id") UUID id, @Param("password") String password);


}
