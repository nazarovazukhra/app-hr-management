package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.TurniketHistory;

import java.util.UUID;


@Repository
public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, UUID> {


}
