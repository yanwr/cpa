package br.com.softplan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ApiModel(description = "Model to create a Seem")
public class Seem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(dataType = "Long", notes = "SeemID - Unique identifier of seem", example = "1",
            allowEmptyValue = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(dataType = "Process Object",
            notes = "Process - Object that represents the link between opinion and process",
            example = "Process Object or ID", required = true)
    private Process process;

    @ApiModelProperty(dataType = "String", notes = "Description - Describes the seem",
            example = "The test process was completed due to lack of financial resources", required = true)
    private String description;

    @JsonIgnoreProperties(value = {"opinions"})
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(dataType = "Finisher User",
            notes = "Seem User Finisher - Object that represent the finisher user designated" +
                    " to give an opinion on the process", example = "Object of User", required = true)
    private User finisher;

    public Seem(String description) {
        this.description = description;
    }

    public void setFinisher(User finisher) {
        this.finisher = finisher;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Seem(Process process, User finisher) {
        this.process = process;
        this.finisher = finisher;
    }
}