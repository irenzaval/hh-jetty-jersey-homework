package ru.irenzaval.characters.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CharacterRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(max = 50)
    public String name;

    @NotBlank(message = "Class must not be empty")
    public String characterClass;
}
