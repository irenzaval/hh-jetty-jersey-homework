package ru.irenzaval.characters.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        InputStream stream = requestContext.getEntityStream();
        byte[] bodyBytes = stream.readAllBytes();

        String body = new String(bodyBytes, StandardCharsets.UTF_8);

        System.out.println("REQUEST: ");
        System.out.println(requestContext.getMethod() + " " + requestContext.getUriInfo().getPath());
        System.out.println("Body: " + body);

        requestContext.setEntityStream(new ByteArrayInputStream(bodyBytes));
    }
}
