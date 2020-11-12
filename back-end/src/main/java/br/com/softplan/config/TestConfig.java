package br.com.softplan.config;

import br.com.softplan.models.Process;
import br.com.softplan.models.Seem;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.repositories.ProcessRepository;
import br.com.softplan.repositories.SeemRepository;
import br.com.softplan.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private SeemRepository seemRepository;


    @Autowired
    private BCryptPasswordEncoder crypto;

    @Override
    public void run(String... args) throws Exception {

        User userAdmin = new User(
                "admin", "administrador@gmail.com", crypto.encode("admin"), Profiles.ADMIN);
        User userFinalizador = new User(
                "finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        User userFinalizador2 = new User(
                "finalizador2", "final2@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        User userTriador = new User(
                "triador", "triador@gmail.com", crypto.encode("triador"), Profiles.TRIADOR);

        userRepository.saveAll(Arrays.asList(userAdmin, userFinalizador, userTriador, userFinalizador2));

        Process process = new Process(null, "Processo Teste", "Blablabla", null);
        Process process2 = new Process(null, "Processo Teste2", "Blablabla", null);
        Process process3 = new Process(null, "Processo Teste3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque condimentum ultrices leo, fringilla ultrices metus maximus ut. Sed semper mattis lectus, sed vehicula nisi. Sed mattis"
                , null);
        processRepository.saveAll(Arrays.asList(process, process2, process3));

        Seem seem = new Seem("Processo finalizado por falta de provas.");
        seem.setFinisher(userFinalizador);
        seem.setProcess(process);
        Seem seem2 = new Seem(null);
        seem2.setFinisher(userFinalizador2);
        seem2.setProcess(process2);
        Seem seem4 = new Seem(null);
        seem4.setFinisher(userFinalizador2);
        seem4.setProcess(process3);
        seemRepository.saveAll(Arrays.asList(seem, seem2, seem4));
    }
}