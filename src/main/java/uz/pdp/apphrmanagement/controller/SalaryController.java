package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.entity.Salary;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.service.SalaryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        List<Salary> salaryList = salaryService.getAll();
        return ResponseEntity.ok(salaryList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getBydId(@PathVariable UUID id) {
        Salary salary = salaryService.getBydId(id);
        return ResponseEntity.status(salary == null ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(salary);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {

        ApiResponse apiResponse = salaryService.delete(id);
        return ResponseEntity.status
                (apiResponse.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody SalaryDto salaryDto) {

        ApiResponse apiResponse = salaryService.add(salaryDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 200 : 409).body(apiResponse);

    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody SalaryDto salaryDto) {

        ApiResponse apiResponse = salaryService.editById(id, salaryDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);
    }
}
