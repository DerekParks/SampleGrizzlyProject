package com.company.someWebService;

import com.company.someWebService.util.ServerUtil;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.accesslog.AccessLogBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Starts the HTTP server for web service.
 */
public final class Main {

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Main.class); //Use slf4j for logging

  public static final String SERVICE_NAME = "/MyService";

  /**
   * Create a new HTTP Server with JAX-RS endpoints and Jackson.
   *
   * @param host host to start web server on
   * @param port TEST_PORT to start web server on
   * @return newly created server
   */
  public static HttpServer startServer(final String host, final int port) {
    final ResourceConfig rc =
        ServerUtil.registerDefaultProviders(new ResourceConfig().packages("com.company.someWebService.services")); //Register all classes in this class path.
    return ServerUtil.getNewServer(host, port,SERVICE_NAME, rc);
  }

  /**
   * Entry point for the jar. Setups HTTP server, starts it, and waits for requests.
   *
   * @param args Command-line arguments.
   * @throws IOException Any difficulty accessing data.
   */
  public static void main(final String[] args) throws IOException {
    BasicConfigurator.configure();
    final String host = "0.0.0.0";
    final int port = 8080;
    final HttpServer server = startServer(host, port);
    final AccessLogBuilder builder = new AccessLogBuilder("access.log");
    builder.instrument(server.getServerConfiguration());
    builder.build();

    try {

      LOG.info(String.format("Jersey app started with WADL available at %s \n\nCtrl-C to stop it...", "http://" + host + ':' + port  + SERVICE_NAME));
      server.start();

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          LOG.info("Shutdown...");
          server.shutdownNow();
        }
      });

      while (true) {
        Thread.sleep(Long.MAX_VALUE);
      }

    } catch (final Exception e) {
      //Ignore and keep going.
      LOG.warn("Server Error:", e);
    }
  }
}

