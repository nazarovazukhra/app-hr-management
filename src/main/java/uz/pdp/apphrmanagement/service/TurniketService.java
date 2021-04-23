package uz.pdp.apphrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.repository.TurniketRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TurniketService {


    final TurniketRepository turniketRepository;

    public TurniketService(TurniketRepository turniketRepository) {
        this.turniketRepository = turniketRepository;
    }


    public ApiResponse createTurniket(Turniket turniket) {

        boolean exists = turniketRepository.existsByLocation(turniket.getLocation());
        if (exists)
            return new ApiResponse("Turniket already exists in this area ", false);
        Turniket turniket1 = new Turniket();
        turniket1.setCreatedAt(turniket.getCreatedAt());
        turniket1.setLocation(turniket.getLocation());
        turniketRepository.save(turniket1);
        return new ApiResponse("Turniket created in  " + turniket.getLocation(), true);
    }

    public ApiResponse editTurniket(UUID id, Turniket turniket) {

        Optional<Turniket> optionalTurniket = turniketRepository.findById(id);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Such turniket not found", false);

        Turniket editingTurniket = optionalTurniket.get();
        editingTurniket.setLocation(turniket.getLocation());
        editingTurniket.setCreatedAt(turniket.getCreatedAt());
        turniketRepository.save(editingTurniket);
        return new ApiResponse("Turniket edited", true);

    }

    public List<Turniket> get() {
        return turniketRepository.findAll();
    }

    public Turniket getById(UUID id) {

        Optional<Turniket> optionalTurniket = turniketRepository.findById(id);
        return optionalTurniket.orElse(null);
    }

    public ApiResponse delete(UUID id) {

        boolean exists = turniketRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such turniket not found", false);

        turniketRepository.deleteById(id);
        return new ApiResponse("Turniket deleted", true);
    }
}
