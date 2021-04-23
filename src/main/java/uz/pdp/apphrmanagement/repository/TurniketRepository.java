package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Turniket;

import java.util.UUID;


@Repository
public interface TurniketRepository extends JpaRepository<Turniket, UUID> {

    boolean existsByLocation(String location);
}
