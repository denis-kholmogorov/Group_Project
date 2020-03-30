package project.repositories;

import org.springframework.data.repository.CrudRepository;
import project.models.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
