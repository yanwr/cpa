package br.com.softplan.controllers.forms;

import br.com.softplan.models.Process;
import br.com.softplan.models.Seem;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.services.ProcessService;
import br.com.softplan.services.SeemService;
import br.com.softplan.services.UserService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ApiModel(description = "Model to apply endpoint business logic to the seem object")
public class SeemForm {

    public interface ValidationStepOne {
    }

    public interface ValidationStepTwo {
    }

    @NotEmpty(message = "Descrição não pode estar vazia", groups = {ValidationStepTwo.class})
    @ApiModelProperty(dataType = "String", notes = "Description - Describes the seem",
            example = "The test process was completed due to lack of financial resources", required = true)
    private String description;

    @NotNull(message = "Processo precisa ser selecionado", groups = {ValidationStepOne.class})
    @Min(value = 1, message = "Processo selecionado é inválido", groups = {ValidationStepOne.class})
    @ApiModelProperty(dataType = "Long", notes = "ProcessID - Unique identifier of a process " +
            "(this attribute represents the relationship between opinion and process)", example = "1",
            allowEmptyValue = true)
    private Long process;

    @NotNull(message = "Usuários precisam ser selecionados", groups = {ValidationStepOne.class})
    @ApiModelProperty(dataType = "Long", notes = "Users Id's - Unique identifiers of finishers users " +
            "(this attribute represents the relationship between opinion and users)", example = "[1,2]",
            allowEmptyValue = true)
    private Set<Long> users;

    public List<Seem> converter(ProcessService processService, UserService userService) {
        Process process = processService.show(this.process);
        List<Seem> opinions = new ArrayList<>();
        for (Long u : users) {
            User user = userService.show(u);
            if (isFinisher(user)) {
                opinions.add(new Seem(process, user));
            }
        }
        return opinions;
    }

    public Seem update(Long id, SeemService seemService) {
        Seem seem = seemService.show(id);
        seem.setDescription(this.description);
        return seem;
    }

    private Boolean isFinisher(User user) {
        return user.getProfile().contains(Profiles.FINALIZADOR);
    }
}