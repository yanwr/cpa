package br.com.softplan.controllers;

import br.com.softplan.controllers.forms.ProcessForm;
import br.com.softplan.models.Process;
import br.com.softplan.models.dto.ProcessDTO;
import br.com.softplan.services.ProcessService;
import br.com.softplan.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/processes")
@Api(tags = "Process Micro-Services Controller", value = "ProcessController", description = "Controller for Process Micro-Services")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('TRIADOR', 'FINALIZADOR')")
    @GetMapping
    @ApiOperation(value = "Retrieves a list of all processes", notes = "This endpoint retrieves all processes")
    public ResponseEntity<List<ProcessDTO>> index() {
        List<ProcessDTO> processes = processService.index().stream().map(ProcessDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(processes);
    }

    @PreAuthorize("hasAnyRole('TRIADOR')")
    @PostMapping
    @Transactional
    @ApiOperation(value = "Create a new process", notes = "This endpoint creates a new process")
    public ResponseEntity<ProcessDTO> save(@Validated(ProcessForm.ValidationStepOne.class) @RequestBody ProcessForm form) {
            Process process = form.converter();
            ProcessDTO saved = new ProcessDTO(processService.store(process));
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAnyRole('TRIADOR', 'FINALIZADOR')")
    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieves an specific process by unique identifier",
            notes = "This endpoint retrieves an process by unique identifier")
    public ResponseEntity<ProcessDTO> details(@PathVariable Long id) {
        Process process = processService.show(id);
        return ResponseEntity.ok(new ProcessDTO(process));
    }

    @PreAuthorize("hasAnyRole('TRIADOR')")
    @PutMapping("/{id}")
    @Transactional
    @ApiOperation(value = "Update an process by unique identifier",
            notes = "This endpoint update data from process by unique identifier")
    public ResponseEntity<?> update(@PathVariable Long id, @Validated(ProcessForm.ValidationStepTwo.class) @RequestBody ProcessForm form) {
        Process process = processService.update(form.update(id, processService));
        return ResponseEntity.ok(new ProcessDTO(process));
    }

    @PreAuthorize("hasAnyRole('TRIADOR')")
    @DeleteMapping("/{id}")
    @Transactional
    @ApiOperation(value = "Delete an process by unique identifier",
            notes = "This endpoint deletes an process by unique identifier")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        processService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('FINALIZADOR')")
    @GetMapping("/no-opinion")
    @ApiOperation(value = "Retrieves a list of all processes without opinion",
            notes = "This endpoint retrieves all processes without opinion")
    public ResponseEntity<List<ProcessDTO>> findProcessesWithoutOpinion() {
        List<ProcessDTO> processes = processService.processesNoOpinionByUserId(userService.authenticated().getId())
                .stream().map(process -> new ProcessDTO(process, userService.authenticated().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(processes);
    }
}
