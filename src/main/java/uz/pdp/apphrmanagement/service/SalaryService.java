package uz.pdp.apphrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Salary;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.SalaryDto;
import uz.pdp.apphrmanagement.repository.SalaryRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {

    final SalaryRepository salaryRepository;
    final UserRepository userRepository;

    public SalaryService(SalaryRepository salaryRepository, UserRepository userRepository) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
    }

    public List<Salary> getAll() {
        return salaryRepository.findAll();
    }

    public Salary getBydId(UUID id) {
        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        return optionalSalary.orElse(null);
    }

    public ApiResponse delete(UUID id) {

        boolean exists = salaryRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such salary not found", false);

        salaryRepository.deleteById(id);
        return new ApiResponse("Salary deleted", true);
    }

    public ApiResponse add(SalaryDto salaryDto) {

        Optional<User> optionalUser = userRepository.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);

        Salary newSalary = new Salary();
        newSalary.setAmount(salaryDto.getAmount());
        newSalary.setDate(salaryDto.getDate());
        newSalary.setWorkStartDate(salaryDto.getWorkStartDate());
        newSalary.setWorkEndDate(salaryDto.getWorkEndDate());
        newSalary.setUserId(optionalUser.get());

        salaryRepository.save(newSalary);
        return new ApiResponse("Salary added", true);
    }

    public ApiResponse editById(UUID id, SalaryDto salaryDto) {

        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        if (!optionalSalary.isPresent())
            return new ApiResponse("Salary not found", false);

        Optional<User> optionalUser = userRepository.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);

        Salary editingSalary = optionalSalary.get();
        editingSalary.setAmount(salaryDto.getAmount());
        editingSalary.setDate(salaryDto.getDate());
        editingSalary.setWorkStartDate(salaryDto.getWorkStartDate());
        editingSalary.setWorkEndDate(salaryDto.getWorkEndDate());
        editingSalary.setUserId(optionalUser.get());

        salaryRepository.save(editingSalary);
        return new ApiResponse("Salary edited", true);
    }
}
