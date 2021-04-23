package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagement.entity.Task;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findAllByTaskName(String taskName);

    Optional<Task> findAllByTaskNameAndResId(String taskName, UUID resId);
}
