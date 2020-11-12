package br.com.softplan.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "Model used to map credentials to this object on JSON request")
public class CredentialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(dataType = "String", notes = "User Email - Email of user account",
            example = "john.harrys@gmail.com", required = true)
    private String email;

    @ApiModelProperty(dataType = "String",
            notes = "User Password - Secure password, user choice (We do not have access to your password, (encryption))",
            example = "John94aj48jr3", required = true)
    private String password;

    public CredentialDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}