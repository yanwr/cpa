package br.com.softplan.models;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration
@ConfigurationProperties("msg.validacao")
public class MsgError {
    private String  storeProcess;
    private String  updateProcess;
    private String  withoutProcess;

    private String  withoutSeem;
    private String  storeSeem;
    private String  updateSeem;

    private String  withoutUser;
    private String  storeUser;
    private String  updateUser;
}