package ru.irenzaval.characters.resource;

import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import ru.irenzaval.characters.model.Character;
import ru.irenzaval.characters.model.CharacterRequest;
import ru.irenzaval.characters.service.CharacterService;

@Path("/characters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CharacterResource {

    private final CharacterService service;

    public CharacterResource(CharacterService service) {
        this.service = service;
    }

    // CREATE
    @POST
    public Response create(@Valid CharacterRequest request) {
        Character created = service.create(request.name, request.characterClass);

        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    // GET ALL
    @GET
    public List<Character> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GET
    @Path("/{id}")
    public Character getById(@PathParam("id") int id) {
        return service.getById(id);
    }

    // LEVEL UP
    @POST
    @Path("/{id}/level-up")
    public Character levelUp(@PathParam("id") int id) {
        return service.levelUp(id);
    }
}