package uz.pdp.apphrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getById(Integer id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.orElse(null);
    }

    public ApiResponse delete(Integer id) {

        boolean exists = roleRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such role not found", false);

        roleRepository.deleteById(id);
        return new ApiResponse("Role deleted", true);
    }

    public ApiResponse add(Role role) {

        boolean existsByRoleName = roleRepository.existsByRoleName(role.getRoleName());
        if (existsByRoleName)
            return new ApiResponse("Such role already exists", false);

        Role newRole = new Role();
        newRole.setRoleName(role.getRoleName());
        roleRepository.save(newRole);

        return new ApiResponse("Role added", true);
    }

    public ApiResponse editById(Integer id, Role role) {

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent())
            return new ApiResponse("Such role not found", false);

        Role editingRole = optionalRole.get();
        editingRole.setRoleName(role.getRoleName());
        roleRepository.save(editingRole);
        return new ApiResponse("Role edited", false);
    }
}
