package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {


    final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        List<Role> roleList = roleService.getAll();
        return ResponseEntity.ok(roleList);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getBydId(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return ResponseEntity.status(role == null ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(role);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {

        ApiResponse apiResponse = roleService.delete(id);
        return ResponseEntity.status
                (apiResponse.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody Role role) {

        ApiResponse apiResponse = roleService.add(role);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editById(@PathVariable Integer id, @RequestBody Role role) {

        ApiResponse apiResponse = roleService.editById(id, role);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);
    }

}
