package com.extrawest.bdd_cpo_ocpi.containers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;

import java.util.ArrayList;
import java.util.List;

public abstract class ContainerBase {
    private static final int CONTAINER_PORT = 27017;
    private static final int HOST_PORT = 27017;
    private static final String LOG_TO_WAIT = ".*Connection accepted.*\\n";
    private static final String IMAGE = "mongo:6.0.3";
    static GenericContainer mongoContainer;

    static {
        /*  Connector to be ready needs to wait "Connection accepted" log message in Docker container two times.
         First "Connection accepted" is connection from current app and second "Connection accepted" is from Server
         that are under testing*/
        mongoContainer = new GenericContainer(IMAGE)
                .withExposedPorts(CONTAINER_PORT)
                .waitingFor(Wait.forLogMessage(LOG_TO_WAIT, 2))
                .withAccessToHost(true)
                .withReuse(true);

        List<String> portBindings = new ArrayList<>();
        portBindings.add(String.format("%s:%s", HOST_PORT, CONTAINER_PORT));
        mongoContainer.setPortBindings(portBindings);
    }

    static {
        Startables.deepStart(mongoContainer).join();
        System.out.println("Port" + mongoContainer.getPortBindings() + "" + mongoContainer.getHost());
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
    }
}