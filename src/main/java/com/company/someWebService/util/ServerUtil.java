package com.company.someWebService.util;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

import javax.ws.rs.core.Configurable;

/**
 * Utilities for building REST server.
 */
public class ServerUtil {

  /**
   * Add resource config that scans for JAX-RS and Thrift resources and providers
   * @param <T> Type of Configurable
   * @param rc input Resource Config
   * @return ResourceConfig with registered components
   */
  public static <T extends Configurable<T>> T registerDefaultProviders(final T rc) {
    rc.register(JacksonFeature.class); //Jackson translates POJOs to REST
    rc.register(ExToResponse.class);  //Exceptions get converted to string stack traces
    return rc;
  }

  /**
   * Create a new instance of grizzly http server.
   * @param host Host of the server
   * @param port Port of the server
   * @param root Root of the web server
   * @param rc Resource config for the server
   * @return newly constructed server that isn't running.
   */
  public static HttpServer getNewServer(final String host, final int port, final String root, final ResourceConfig rc) {
    return GrizzlyHttpServerFactory
        .createHttpServer(URI.create("http://" + host + ':' + port + root), rc);
  }

}
