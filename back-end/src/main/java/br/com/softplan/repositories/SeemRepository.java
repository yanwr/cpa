package br.com.softplan.repositories;

import br.com.softplan.models.Seem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeemRepository extends JpaRepository<Seem, Long> { }