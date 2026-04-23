package ru.irenzaval.characters.service;

import jakarta.ws.rs.NotFoundException;
import java.util.List;

import ru.irenzaval.characters.model.Character;
import ru.irenzaval.characters.repository.CharacterRepository;

public class CharacterService {

    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public Character create(String name, String characterClass) {
        return repository.create(name, characterClass);
    }

    public List<Character> getAll() {
        return repository.getAll();
    }

    public Character getById(int id) {
        return getExistingCharacter(id);
    }

    public Character levelUp(int id) {
        getExistingCharacter(id);
        return repository.levelUp(id);
    }

    private Character getExistingCharacter(int id) {
        Character character = repository.getById(id);
        if (character == null) {
            throw new NotFoundException("Character not found");
        }
        return character;
    }
}