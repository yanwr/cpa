package br.com.softplan.controllers.forms;

import br.com.softplan.models.Process;
import br.com.softplan.services.ProcessService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ApiModel(description = "Model to apply endpoint business logic to the process object")
public class ProcessForm {

    public interface ValidationStepOne {
    }

    public interface ValidationStepTwo {
    }

    @NotEmpty(message = "Nome não pode estar vazio", groups = {ValidationStepOne.class})
    @ApiModelProperty(dataType = "String", notes = "Process Name - Name of process", example = "Test Process",
            required = true)
    private String name;

    @NotEmpty(message = "Descrição não pode estar vazia", groups = {ValidationStepOne.class, ValidationStepTwo.class})
    @ApiModelProperty(dataType = "String", notes = "Process Description - Describes the process",
            example = "Test Process is a process that fights for equal rights", required = true)
    private String description;

    public Process converter() {
        return new Process(name, description);
    }

    public Process update(Long id, ProcessService processService) {
        Process process = processService.show(id);
        if (!this.name.isEmpty()) {
            process.setName(this.name);
        }
        process.setDescription(this.description);

        return process;
    }
}