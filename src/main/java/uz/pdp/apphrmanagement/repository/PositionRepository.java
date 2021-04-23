package uz.pdp.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.apphrmanagement.entity.Position;


@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    boolean existsByPosition(String position);
    Position findAllById(Integer id);
}
