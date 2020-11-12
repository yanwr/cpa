package br.com.softplan.repositories;

import br.com.softplan.models.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    @Query(value = "SELECT p FROM Process p JOIN Seem s ON s.process = p AND s.finisher.id = ?1 WHERE s.description IS NULL")
    List<Process> processesNoOpinionByUserId(Long userId);
}
