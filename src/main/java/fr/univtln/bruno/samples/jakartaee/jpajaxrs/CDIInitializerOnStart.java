package fr.univtln.bruno.samples.jakartaee.jpajaxrs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CDIInitializerOnStart {

    @Inject
    MessageDAO messageDAO;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object object) {
        log.info("Init CDI singleton");
        //messageDAO.create(Message.createMessage("Test init massage"));
    }

}
