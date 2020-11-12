package br.com.softplan.controllers;

import br.com.softplan.controllers.forms.SeemForm;
import br.com.softplan.models.Seem;
import br.com.softplan.models.dto.SeemDTO;
import br.com.softplan.services.ProcessService;
import br.com.softplan.services.SeemService;
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
@RequestMapping("/opinions")
@Api(tags = "Seem Micro-Services Controller", value = "SeemController", description = "Controller for Seem Micro-Services")
public class SeemController {

    @Autowired
    private SeemService seemService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAnyRole('TRIADOR', 'FINALIZADOR')")
    @GetMapping
    @ApiOperation(value = "Retrieves a list of all opinions", notes = "This endpoint retrieves all opinions")
    public ResponseEntity<List<SeemDTO>> findAll() {
        List<SeemDTO> opinions = seemService.index().stream().map(SeemDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(opinions);
    }

    @PreAuthorize("hasAnyRole('TRIADOR')")
    @PostMapping
    @Transactional
    @ApiOperation(value = "Create a new opinion for an process", notes = "This endpoint creates a new opinion")
    public ResponseEntity<List<SeemDTO>> save(@Validated(SeemForm.ValidationStepOne.class) @RequestBody SeemForm form) {
        List<Seem> seem = form.converter(processService, userService);
        List<SeemDTO> saved = seemService.storeAll(seem).stream().map(SeemDTO::new).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAnyRole('TRIADOR', 'FINALIZADOR')")
    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieves an specific opinion by unique identifier",
            notes = "This endpoint retrieves an opinion by unique identifier")
    public ResponseEntity<SeemDTO> details(@PathVariable Long id) {
        Seem seem = seemService.show(id);
        return ResponseEntity.ok(new SeemDTO(seem));
    }

    @PreAuthorize("hasAnyRole('FINALIZADOR')")
    @PutMapping("/{id}")
    @Transactional
    @ApiOperation(value = "Update an opinion by unique identifier",
            notes = "This endpoint update data from opinion by unique identifier")
    public ResponseEntity<SeemDTO> update(@PathVariable Long id, @Validated(SeemForm.ValidationStepTwo.class) @RequestBody SeemForm form) {
        Seem verify = seemService.show(id);
        if (verifyUser(verify, userService.authenticated().getId())) {
            Seem seem = seemService.update(form.update(id, seemService));
            return ResponseEntity.ok(new SeemDTO(seem));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("hasAnyRole('FINALIZADOR')")
    @DeleteMapping("/{id}")
    @Transactional
    @ApiOperation(value = "Delete an opinion by unique identifier",
            notes = "This endpoint deletes an opinion by unique identifier")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Seem seem = seemService.show(id);
        if (verifyUser(seem, userService.authenticated().getId())) {
            seemService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private boolean verifyUser(Seem seem, Long id) {
        return seem.getFinisher().getId().equals(id);
    }
}