package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.service.TurniketService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {

    final TurniketService turniketService;

    public TurniketController(TurniketService turniketService) {
        this.turniketService = turniketService;
    }

    @PostMapping
    public HttpEntity<?> createTurniket(@RequestBody Turniket turniket) {
        ApiResponse apiResponse = turniketService.createTurniket(turniket);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping
    public HttpEntity<?> editTurniket(@RequestParam UUID id, @RequestBody Turniket turniket) {
        ApiResponse apiResponse = turniketService.editTurniket(id, turniket);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);

    }

    @GetMapping
    public HttpEntity<?> get() {
        List<Turniket> turniketList = turniketService.get();
        return ResponseEntity.ok(turniketList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        Turniket turniket = turniketService.getById(id);
        return ResponseEntity.status(turniket != null ? 200 : 409).body(turniket);
    }

    @DeleteMapping
    public HttpEntity<?>delete(@RequestParam UUID id){
        ApiResponse apiResponse=turniketService.delete(id);
        return ResponseEntity.status(apiResponse.getSuccess()?200:409).body(apiResponse);
    }
}
