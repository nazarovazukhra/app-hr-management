package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Salary;

import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, UUID> {
}
