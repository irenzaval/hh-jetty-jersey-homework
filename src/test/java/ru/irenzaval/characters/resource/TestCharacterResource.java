package ru.irenzaval.characters.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;

import ru.irenzaval.characters.model.Character;
import ru.irenzaval.characters.repository.CharacterRepository;
import ru.irenzaval.characters.service.CharacterService;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacterResource extends JerseyTest {

    private CharacterService service;

    @Override
    protected Application configure() {

        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        jdbi.useHandle(handle -> handle.execute("""
                    CREATE TABLE IF NOT EXISTS characters (
                        id IDENTITY PRIMARY KEY,
                        name VARCHAR(50),
                        characterClass VARCHAR(50),
                        level INT,
                        experience INT
                    )
                """));
        jdbi.useHandle(handle -> handle.execute("DELETE FROM characters"));

        CharacterRepository repository = new CharacterRepository(jdbi);
        service = new CharacterService(repository);

        return new ResourceConfig()
                .register(new CharacterResource(service));
    }

    @Test
    void getAllShouldReturn200() {
        Response response = target("/characters").request().get();

        assertEquals(200, response.getStatus());
    }

    @Test
    void createShouldReturnCharacter() {
        String json = """
                    {
                      "name": "Arthas",
                      "characterClass": "Warrior"
                    }
                """;

        Response response = target("/characters")
                .request()
                .post(Entity.json(json));

        assertEquals(201, response.getStatus());

        String body = response.readEntity(String.class);
        assertTrue(body.contains("Arthas"));
    }

    @Test
    void getByIdShouldReturnCharacter() {
        Character character = service.create("Test", "Mage");

        Response response = target("/characters/" + character.getId())
                .request()
                .get();

        assertEquals(200, response.getStatus());

        String body = response.readEntity(String.class);
        assertTrue(body.contains("Test"));
    }
}
