package ru.irenzaval.characters.model;

public class Character {

    public int id;
    public String name;
    public String characterClass;
    public int level;
    public int experience;

    public Character() {
    }

    public Character(int id, String name, String characterClass, int level, int experience) {
        this.id = id;
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.experience = experience;
    }
}
