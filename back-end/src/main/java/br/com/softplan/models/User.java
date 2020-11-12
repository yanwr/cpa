package br.com.softplan.models;

import br.com.softplan.models.dto.UserDTO;
import br.com.softplan.models.enuns.Profiles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@ApiModel(description = "Model to create a user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(dataType = "Long", notes = "UserID - Unique identifier of user"
            , example = "1", allowEmptyValue = true)
    private Long id;

    @ApiModelProperty(dataType = "String", notes = "User Name - Name of user", example = "John Harrys"
            , required = true)
    @NotEmpty(message = "Nome não pode estar vazio")
    private String name;

    @Column(unique = true)
    @ApiModelProperty(dataType = "String", notes = "User Email - Email of user account",
            example = "john.harrys@gmail.com", required = true)
    @Email(message = "Email não é válido")
    @NotEmpty(message = "Email não pode estar vazio")
    private String email;

    @ApiModelProperty(dataType = "String",
            notes = "User Password - Secure password, user choice (We do not have access to your password, (encryption))",
            example = "John94aj48jr3", required = true)
    @NotEmpty(message = "Senha não pode estar vazia")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PROFILES")
    @ApiModelProperty(dataType = "Integer", notes = "User Profile - Profile of user", example = "TRIADOR"
            , required = true)

    private Set<Integer> profile = new HashSet<>();

    @OneToMany(mappedBy = "finisher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ApiModelProperty(dataType = "List of Opinions",
            notes = "User List of Opinions - List of objects that represent the opinions linked to the user",
            example = "List of Opinions")
    @JsonIgnoreProperties(value = {"finisher"})
    private Set<Seem> opinions = new HashSet<>();


    public User() {

    }

    public User(String name, String email, String password, Profiles profile) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.setProfile(profile);
    }

    public User(Long id, Set<Integer> profile, Set<Seem> opinions) {
        this.id = id;
        this.profile = profile;
        this.opinions = opinions;
    }

    public User update(UserDTO update, User current) {
        current.setName(update.getName());
        current.setEmail(update.getEmail());
        current.setProfile(update.getProfile().stream().map(profiles ->
                Profiles.toEnum(profiles.getCod())).collect(Collectors.toList()).get(0));
        return current;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Profiles> getProfile() {
        return this.profile.stream().map(Profiles::toEnum).collect(Collectors.toSet());
    }

    public void setProfile(Profiles profiles) {
        this.profile.clear();
        this.profile.add(profiles.getCod());
    }

    public Set<Seem> getOpinions() {
        return opinions;
    }

    public void addOpinion(Seem seem) {
        this.opinions.add(seem);
    }

    public void setOpinions(Set<Seem> seem) { this.opinions = seem; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}