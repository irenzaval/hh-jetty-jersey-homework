package ru.irenzaval.characters.repository;

import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.irenzaval.characters.model.Character;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCharacterRepository {

    private CharacterRepository repository;

    @BeforeEach
    void setup() {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        jdbi.useHandle(handle -> handle.execute("""
                    CREATE TABLE IF NOT EXiSTS characters (
                        id IDENTITY PRIMARY KEY,
                        name VARCHAR(50),
                        characterClass VARCHAR(50),
                        level INT,
                        experience INT
                    )
                """));

        jdbi.useHandle(handle -> handle.execute("DELETE FROM characters"));

        repository = new CharacterRepository(jdbi);
    }

    @Test
    void createShouldSaveCharacter() {
        Character character = repository.create("Arthas", "Warrior");

        assertNotNull(character);
        assertEquals("Arthas", character.getName());
        assertEquals("Warrior", character.getCharacterClass());
        assertEquals(1, character.getLevel());
    }

    @Test
    void getAllShouldReturnCharacters() {
        repository.create("Athas", "Mage");
        repository.create("Shon", "Warrior");

        List<Character> list = repository.getAll();

        assertEquals(2, list.size());
    }

    @Test
    void getByIdShouldReturnCharacter() {
        Character created = repository.create("Test", "Mage");

        Character found = repository.getById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void getByIdShouldReturnNullifNotFound() {
        Character character = repository.getById(999);

        assertNull(character);
    }

    @Test
    void levelUpShouldIncreaseLevel() {
        Character character = repository.create("Hero", "Warrior");

        Character updated = repository.levelUp(character.getId());

        assertEquals(2, updated.getLevel());
        assertEquals(100, updated.getExperience());
    }
}
