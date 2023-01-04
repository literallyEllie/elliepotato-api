package de.elliepotato.elliepotatoapi.endpoint.auth.repository;

import de.elliepotato.elliepotatoapi.endpoint.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository for registered uses with the API.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find a user by their username.
     *
     * @param username Username to query.
     * @return Optional user field.
     */
    Optional<User> findByUsername(String username);

}
