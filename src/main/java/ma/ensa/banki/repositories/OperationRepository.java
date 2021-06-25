package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}
