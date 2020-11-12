package br.com.softplan.controllers.forms;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(description = "Model to apply endpoint business logic to the user object")
public class UserForm {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}