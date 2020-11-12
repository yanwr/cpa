package br.com.softplan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ApiModel(description = "Model to create a process")
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(dataType = "Long", notes = "ProcessID - Unique identifier of process", example = "1",
            allowEmptyValue = true)
    private Long id;

    @ApiModelProperty(dataType = "String", notes = "Process Name - Name of process", example = "Test Process",
            required = true)
    private String name;

    @ApiModelProperty(dataType = "String", notes = "Process Description - Describes the process",
            example = "Test Process is a process that fights for equal rights", required = true)
    private String description;


    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"process"})
    @ApiModelProperty(dataType = "List of Opinions", notes = "List of objects that represent the opinions linked to the process",
            example = "Log in to the system with the finisher user, specifying the Bearer token in the request header")
    private Set<Seem> opinions = new HashSet<>();

    public void addOpinion(Seem seem) {
        this.opinions.add(seem);
    }

    public Process(String name, String description) {
        this.name = name;
        this.description = description;
    }
}