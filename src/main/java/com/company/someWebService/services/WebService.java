package com.company.someWebService.services;


import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * A dummy web service that demonstrates some feature of JAX-RS.
 */
@Path("WebService/")
public class WebService {
  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WebService.class); //Use slf4j for logging

  public static class POJO {
    public String testString = "";
    public int testInt = 0;
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  public Response post(final POJO pojoFromJSON) {
    LOG.info(String.format("POSTED %s and %d\n", pojoFromJSON.testString, pojoFromJSON.testInt));
    return Response.accepted().build();
  }

  /**
   * Get n random numbers.
   *
   * @param n number of random integers
   * @return JSON list of random integers
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public List<Integer> getRandom(@QueryParam("n") int n) {
    return new Random().ints(n).boxed().collect(Collectors.toList());
  }

  @GET
  @Path("streamOut/")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getRandomStreamOut(@QueryParam("n") int n) {
    StreamingOutput stream = os -> {
      final Writer writer = new BufferedWriter(new OutputStreamWriter(os));
      writer.write('[');

      final Random r = new Random();
      for(long i=1; i <= n; i++) {
        writer.write(r.nextInt()+"");
        if(i != n) {
          writer.write(",");
        }
      }
      writer.write(']');
      writer.flush();
    };

    return Response.ok(stream).build();
  }

  @PUT
  public Response put(@QueryParam("something") String something) {
    LOG.info(String.format("PUT %s\n", something));
    throw new UnsupportedOperationException();
  }

  @DELETE
  @Path("{id}")
  public Response del(@PathParam("id") String id) {
    final UUID uuid;
    try {
      uuid = UUID.fromString(id);
    } catch (IllegalArgumentException ex) {
      return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    LOG.info(String.format("DELETEing %s\n", uuid.toString()));
    return Response.ok().build();
  }

  @PUT
  @Path("put/{someId}/")
  @Consumes({MediaType.APPLICATION_JSON})
  public Response putFile(
                       @PathParam("someId") final long someId,
                       final InputStream inputStream) throws IOException {
    System.out.println(someId);

    try(final Reader reader = new InputStreamReader(inputStream);
        final BufferedReader br = new BufferedReader(reader)) {
      String inputline;
      while((inputline = br.readLine()) != null) {
        System.out.println(inputline);
      }
    }


    return Response.ok().build();
  }
}
