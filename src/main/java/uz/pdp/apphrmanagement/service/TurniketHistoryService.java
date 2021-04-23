package uz.pdp.apphrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Turniket;
import uz.pdp.apphrmanagement.entity.TurniketHistory;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.TurniketType;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketHistoryDto;
import uz.pdp.apphrmanagement.repository.TurniketHistoryRepository;
import uz.pdp.apphrmanagement.repository.TurniketRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.Optional;

@Service
public class TurniketHistoryService {

    final TurniketHistoryRepository turniketHistoryRepository;
    final UserRepository userRepository;
    final TurniketRepository turniketRepository;

    public TurniketHistoryService(TurniketHistoryRepository turniketHistoryRepository, UserRepository userRepository, TurniketRepository turniketRepository) {
        this.turniketHistoryRepository = turniketHistoryRepository;
        this.userRepository = userRepository;
        this.turniketRepository = turniketRepository;
    }

    public ApiResponse enterAndExit(TurniketHistoryDto turniketHistoryDto, Integer number) {

        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketHistoryDto.getTurniketId());
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Such turniket not found", false);
        Optional<User> optionalUser = userRepository.findById(turniketHistoryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);

        Turniket turniket = optionalTurniket.get();
        User user = optionalUser.get();
        if (number == 1) {
            TurniketHistory turniketHistory = new TurniketHistory();
            turniketHistory.setTurniket(turniket);
            turniketHistory.setUser(user);
            turniketHistory.setTurniketTime(turniketHistoryDto.getTurniketTime());
            turniketHistory.setTurkinetType(TurniketType.ENTER);
            turniketHistoryRepository.save(turniketHistory);
            return new ApiResponse("User entered", true);
        }
        if (number == 2) {
            TurniketHistory turniketHistory = new TurniketHistory();
            turniketHistory.setTurniket(turniket);
            turniketHistory.setUser(user);
            turniketHistory.setTurniketTime(turniketHistoryDto.getTurniketTime());
            turniketHistory.setTurkinetType(TurniketType.EXIT);
            turniketHistoryRepository.save(turniketHistory);
            return new ApiResponse("User exited", true);
        }
        return new ApiResponse("Error in server", false);
    }
}
