package br.com.softplan.models.dto;

import br.com.softplan.models.Seem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "Model to return a Seem with no redundant information for the user")
public class SeemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(dataType = "Long", notes = "SeemID - Unique identifier of seem", example = "1",
            allowEmptyValue = true)
    private Long id;

    @ApiModelProperty(dataType = "String", notes = "Description - Describes the seem",
            example = "The test process was completed due to lack of financial resources",
            required = true)
    private String description;

    @ApiModelProperty(dataType = "Long", notes = "ProcessID - Unique identifier of process", example = "1",
            allowEmptyValue = true)
    private Long process;

    @ApiModelProperty(dataType = "User Object", notes = "Finisher User - Identifier finisher user linked to opinion",
            example = "1", allowEmptyValue = true)
    private UserDTO finisher;

    public SeemDTO(Seem seem) {
        this.id = seem.getId();
        this.description = seem.getDescription();
        this.process = seem.getProcess().getId();
        this.finisher = new UserDTO(seem.getFinisher());
    }
}