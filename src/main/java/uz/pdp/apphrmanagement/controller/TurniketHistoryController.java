package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TurniketHistoryDto;
import uz.pdp.apphrmanagement.service.TurniketHistoryService;

@RestController
@RequestMapping("/api/turniketHistory")
public class TurniketHistoryController {

    final TurniketHistoryService turniketHistoryService;

    public TurniketHistoryController(TurniketHistoryService turniketHistoryService) {
        this.turniketHistoryService = turniketHistoryService;
    }

    @PostMapping
    public HttpEntity<?> enterAndExit(@RequestBody TurniketHistoryDto turniketHistoryDto, @RequestParam Integer number) {
        ApiResponse apiResponse = turniketHistoryService.enterAndExit(turniketHistoryDto, number);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }
}
