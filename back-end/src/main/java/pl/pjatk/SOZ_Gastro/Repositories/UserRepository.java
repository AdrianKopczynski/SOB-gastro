package pl.pjatk.SOZ_Gastro.Repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByLoginPin(String loginPin);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByLoginPin(String loginPin);

    void deleteByUsername(String username);

}
