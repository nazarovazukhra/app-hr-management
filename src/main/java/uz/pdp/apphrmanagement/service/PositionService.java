package uz.pdp.apphrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Position;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.repository.PositionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<Position> getAll() {
        return positionRepository.findAll();
    }

    public Position getById(Integer id) {

        Optional<Position> optionalPosition = positionRepository.findById(id);
        return optionalPosition.orElse(null);
    }

    public ApiResponse delete(Integer id) {

        boolean exists = positionRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such position not found", false);

        positionRepository.deleteById(id);
        return new ApiResponse("Position deleted", true);
    }

    public ApiResponse add(Position position) {

        boolean exists = positionRepository.existsByPosition(position.getPosition());
        if (exists)
            return new ApiResponse("Such position already exists", false);

        Position newPosition = new Position();
        newPosition.setPosition(position.getPosition());

        if (position.getPosition().equalsIgnoreCase("DIRECTOR")) {
            newPosition.setPositionNumber(0);
        }

        if (position.getPosition().equalsIgnoreCase("HR_MANAGER")) {
            newPosition.setPositionNumber(1);
        }

        if (position.getPosition().equalsIgnoreCase("WORKER")) {
            newPosition.setPositionNumber(2);
        }

        positionRepository.save(newPosition);
        return new ApiResponse("Position added", true);
    }

    public ApiResponse editById(Integer id, Position position) {

        Optional<Position> optionalPosition = positionRepository.findById(id);
        if (!optionalPosition.isPresent())
            return new ApiResponse("Such position not found", false);

        Position editingPosition = optionalPosition.get();
        editingPosition.setPosition(position.getPosition());
        editingPosition.setPositionNumber(position.getPositionNumber());
        positionRepository.save(editingPosition);
        return new ApiResponse("Position edited", true);
    }
}
