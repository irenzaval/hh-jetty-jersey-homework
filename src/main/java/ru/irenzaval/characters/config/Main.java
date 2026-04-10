package ru.irenzaval.characters.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import ru.irenzaval.characters.model.Character;
import ru.irenzaval.characters.repository.CharacterRepository;
import ru.irenzaval.characters.resource.CharacterResource;
import ru.irenzaval.characters.service.CharacterService;

public class Main {

    public static void main(String[] args) throws Exception {

        Jdbi jdbi = Jdbi.create("jdbc:h2:./characterdb");
        jdbi.registerRowMapper(BeanMapper.factory(Character.class));

        jdbi.useHandle(handle -> handle.execute("""
                        CREATE TABLE IF NOT EXISTS characters(
                        id IDENTITY PRIMARY KEY,
                        name VARCHAR(50),
                        characterClass VARCHAR(50),
                        level INT,
                        experience INT
                        )
                """));

        CharacterRepository repository = new CharacterRepository(jdbi);
        CharacterService service = new CharacterService(repository);

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(server, "/");

        ResourceConfig config = new ResourceConfig();

        config.register(new CharacterResource(service));

        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setServlet(new ServletContainer(config));

        context.addServlet(servletHolder, "/*");

        server.start();
        server.join();
    }
}
