package br.com.softplan.services;

import br.com.softplan.models.Process;
import java.util.List;

public interface ProcessService {
    List<Process> index();

    Process show(Long id);

    Process store(Process process);

    Process update(Process process);

    void delete(Long id);

    List<Process> processesNoOpinionByUserId(Long userId);
}