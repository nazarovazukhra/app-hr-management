package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByRoleName(String roleName);
    Role findAllById(Integer id);

}
