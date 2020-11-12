package br.com.softplan.models.dto;

import br.com.softplan.models.Process;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "Model to return a Process with no redundant information for the user")
public class ProcessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(dataType = "Long", notes = "ProcessID - Unique identifier of process", example = "1",
            allowEmptyValue = true)
    private Long id;

    @ApiModelProperty(dataType = "String", notes = "Process Name - Name of process", example = "Test Process",
            required = true)
    private String name;

    @ApiModelProperty(dataType = "String", notes = "Process Description - Describes the process",
            example = "Test Process is a process that fights for equal rights", required = true)
    private String description;

    @ApiModelProperty(dataType = "List of Seem Object", notes = "List of Seem Object - Identify the opinions linked to process",
            example = "List of Seem object", allowEmptyValue = true)
    private Set<SeemDTO> opinions;

    public ProcessDTO(Process process) {
        this.id = process.getId();
        this.name = process.getName();
        this.description = process.getDescription();
        this.opinions = process.getOpinions().stream().map(SeemDTO::new).collect(Collectors.toSet());
        if (process.getOpinions().isEmpty()) {
            this.opinions = null;
        }
    }

    public ProcessDTO(Process process, Long userId) {
        this.id = process.getId();
        this.name = process.getName();
        this.description = process.getDescription();
        this.opinions = process.getOpinions().stream().map(SeemDTO::new).collect(Collectors.toSet())
                .stream().filter(seemDTO -> seemDTO.getFinisher().getId().equals(userId)).collect(Collectors.toSet());
        this.opinions = opinions.stream().filter(seemDTO -> seemDTO.getFinisher()
                .getId().equals(userId)).collect(Collectors.toSet());
        if (process.getOpinions().isEmpty()) {
            this.opinions = null;
        }
    }
}