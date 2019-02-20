package partib.groupProject.probableCauses.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import partib.groupProject.probableCauses.backend.controller.InvalidCallException;
import partib.groupProject.probableCauses.backend.controller.ServerConnector;
import partib.groupProject.probableCauses.backend.testModel.Event;
import partib.groupProject.probableCauses.backend.testModel.Group;
import partib.groupProject.probableCauses.backend.testModel.GroupRepository;

import java.util.Collections;
import java.util.stream.Stream;

@Component
public class Initializer implements CommandLineRunner {

    private final GroupRepository repository;

    public Initializer(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) {
        Stream.of("Denver JUG", "Utah JUG", "Seattle JUG", "Richmond JUG")
                .forEach(name -> repository.save(new Group(name)));

        Group djug = repository.findByName("Denver JUG");
        Event e = Event.builder().title("Full Stack Reactive")
                .description("Reactive with Spring Boot + React")
                .build();
        djug.setEvents(Collections.singleton(e));
        repository.save(djug);

        repository.findAll().forEach(System.out::println);
    }
}
