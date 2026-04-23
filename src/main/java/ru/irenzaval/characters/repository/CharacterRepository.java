package ru.irenzaval.characters.repository;

import org.jdbi.v3.core.Jdbi;

import ru.irenzaval.characters.model.Character;

import java.util.List;

public class CharacterRepository {

    private final Jdbi jdbi;

    public CharacterRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    private Character map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Character character = new Character();
        character.setId(rs.getInt("id"));
        character.setName(rs.getString("name"));
        character.setCharacterClass(rs.getString("characterClass"));
        character.setLevel(rs.getInt("level"));
        character.setExperience(rs.getInt("experience"));
        return character;
    }

    public Character create(String name, String characterClass) {
        jdbi.useHandle(handle -> handle.createUpdate("""
                    INSERT INTO characters(name, characterClass, level, experience)
                    VALUES (:name, :class, :level, :exp)
                """)
                .bind("name", name)
                .bind("class", characterClass)
                .bind("level", 1)
                .bind("exp", 0)
                .execute());

        return jdbi.withHandle(handle -> handle.createQuery("""
                    SELECT id, name, characterClass, level, experience
                    FROM characters
                    ORDER BY id DESC
                    LIMIT 1
                """)
                .map((rs, ctx) -> map(rs))
                .one());
    }

    public List<Character> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("""
                    SELECT id, name, characterClass, level, experience
                    FROM characters
                """)
                .map((rs, ctx) -> map(rs))
                .list());
    }

    public Character getById(int id) {
        return jdbi.withHandle(handle -> handle.createQuery("""
                    SELECT id, name, characterClass, level, experience
                    FROM characters
                    WHERE id = :id
                """)
                .bind("id", id)
                .map((rs, ctx) -> map(rs))
                .findOne()
                .orElse(null));
    }

    public Character levelUp(int id) {
        jdbi.useHandle(handle -> handle.createUpdate("""
                    UPDATE characters
                    SET level = level + 1,
                        experience = experience + 100
                    WHERE id = :id
                """)
                .bind("id", id)
                .execute());

        return getById(id);
    }
}