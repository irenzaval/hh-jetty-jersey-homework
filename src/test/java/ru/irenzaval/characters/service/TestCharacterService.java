package ru.irenzaval.characters.service;

import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.irenzaval.characters.model.Character;
import ru.irenzaval.characters.repository.CharacterRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacterService {

    private CharacterService service;

    @BeforeEach
    void setup() {
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
    }

    @Test
    void createShouldCreateCharacter() {
        Character character = service.create("Arthas", "Warrior");

        assertNotNull(character);
        assertEquals("Arthas", character.name);
        assertEquals("Warrior", character.characterClass);
        assertEquals(1, character.level);
    }

    @Test
    void getAllShouldReturnCharacters() {
        service.create("Arthas", "Mage");
        service.create("Shon", "Warrior");

        List<Character> list = service.getAll();

        assertEquals(2, list.size());
    }

    @Test
    void getByIdShouldReturnCharacter() {
        Character created = service.create("Hero", "Mage");

        Character found = service.getById(created.id);

        assertNotNull(found);
        assertEquals(created.id, found.id);
    }

    @Test
    void levelUpShouldIncreaseLevel() {
        Character character = service.create("Hero", "Warrior");

        Character updated = service.levelUp(character.id);

        assertEquals(2, updated.level);
        assertEquals(100, updated.experience);
    }
}