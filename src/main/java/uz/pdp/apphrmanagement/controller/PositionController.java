package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Position;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.service.PositionService;

import java.util.List;

@RestController
@RequestMapping("/api/position")
public class PositionController {

    final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        List<Position> positionList = positionService.getAll();
        return ResponseEntity.ok(positionList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {

        Position position = positionService.getById(id);
        return ResponseEntity.status(position == null ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(position);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {

        ApiResponse apiResponse = positionService.delete(id);
        return ResponseEntity.status(apiResponse.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);

    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody Position position) {

        ApiResponse apiResponse = positionService.add(position);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);

    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id, @RequestBody Position position) {

        ApiResponse apiResponse = positionService.editById(id, position);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);

    }

}
