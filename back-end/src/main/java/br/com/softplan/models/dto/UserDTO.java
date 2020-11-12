package br.com.softplan.models.dto;

import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel(description = "Model to return a User with no redundant information for the user")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(dataType = "Long", notes = "UserID - Unique identifier of user", example = "1",
            allowEmptyValue = true)
    private Long id;
    @ApiModelProperty(dataType = "String", notes = "User Name - Name of user", example = "John Harrys",
            required = true)
    private String name;
    @ApiModelProperty(dataType = "String", notes = "User Email - Email of user account",
            example = "john.harrys@gmail.com", required = true)
    private String email;
    @ApiModelProperty(dataType = "String", notes = "User Profile - Profile of user", example = "TRIADOR",
            required = true)
    private Set<Profiles> profile = new HashSet<>();

    public UserDTO() { }

    public UserDTO(User currentUser) {
        this.id = currentUser.getId();
        this.email = currentUser.getEmail();
        this.name = currentUser.getName();
        this.profile = currentUser.getProfile();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Profiles> getProfile() {
        return this.profile.stream().collect(Collectors.toSet());
    }

    public void setProfile(Profiles profiles) {
        this.profile.add(profiles);
    }
}