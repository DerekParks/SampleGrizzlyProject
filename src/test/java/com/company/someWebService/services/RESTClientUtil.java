package com.company.someWebService.services;

import com.company.someWebService.Main;
import com.company.someWebService.util.ServerUtil;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Common methods to test REST services.
 */
public final class RESTClientUtil {
  public static final String TEST_HOST = "localhost";
  public static final int TEST_PORT = 8089;


  /**
   * Create a new instance of grizzly http server for unit testing with all classes registered.
   *
   * @param registerClasses non default class to register as providers.
   * @return newly constructed server that isn't running.
   */
  public static HttpServer getTestServer(final Class<?>... registerClasses) {
    final ResourceConfig rc = getRegisterResourceConfig(registerClasses);
    return ServerUtil.getNewServer(TEST_HOST, TEST_PORT, Main.SERVICE_NAME, rc);
  }

  /**
   * Register default providers and provided classes.
   * @param registerClasses Register default providers and provided classes
   * @return Register default providers and provided classes
   */
  public static ResourceConfig getRegisterResourceConfig(Class<?>... registerClasses) {
    final ResourceConfig rc = ServerUtil.registerDefaultProviders(new ResourceConfig());
    for (final Class<?> c : registerClasses) {
      rc.register(c);
    }
    return rc;
  }


  /**
   * Rest client for testing.
   *
   * @param registerClasses classes to register
   * @return Rest client for testing
   */
  public static WebTarget getTestRESTClient(final Class<?>... registerClasses) {
    return getRESTClient(TEST_HOST, TEST_PORT, Main.SERVICE_NAME, registerClasses);
  }

  /**
   * Get a REST client for testing REST services.
   *
   * @param host            REST server host
   * @param port            REST server port
   * @param root            web root
   * @param registerClasses classes to register
   * @return newly constructed REST client.
   */
  public static WebTarget getRESTClient(final String host, final int port, final String root,
                                        final Class<?>... registerClasses) {
    return getRESTClient(ClientBuilder.newClient(), host, port, root, registerClasses);
  }

  /**
   * Get a REST client for testing REST services.
   *
   * @param client          Preconfigured REST client
   * @param host            REST server host
   * @param port            REST server port
   * @param root            web root
   * @param registerClasses classes to register
   * @return newly constructed REST client.
   */
  public static WebTarget getRESTClient(final Client client, final String host, final int port, final String root,
                                        final Class<?>... registerClasses) {


    final WebTarget result = client.target("http://" + host + ':' + port + root);
    ServerUtil.registerDefaultProviders(result);

    for (final Class<?> clazz : registerClasses) {
      result.register(clazz);
    }

    return result;
  }

}
