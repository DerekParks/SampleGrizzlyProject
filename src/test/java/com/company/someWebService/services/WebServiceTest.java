package com.company.someWebService.services;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class WebServiceTest {

  private static final String ROOT = WebService.class.getSimpleName() + '/';
  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WebServiceTest.class); //Use slf4j for logging
  private HttpServer server = null;
  private WebTarget target = null;

  @Before
  public void setup() {
    server = RESTClientUtil.getTestServer(WebService.class); //only register webservice
    target = RESTClientUtil.getTestRESTClient();
  }

  @Test
  public void testGET() {
    final Response responseMsg = target.path(ROOT).request().get();

    Assert.assertEquals(Response.Status.OK.getStatusCode(), responseMsg.getStatus());
    try {
      LOG.info(new JSONArray(responseMsg.readEntity(String.class)).toString());
    } catch (JSONException e) {
      Assert.fail("Didn't get JSON back from server");
    }
  }

  @Test
  public void testPOST() {
    final WebService.POJO pojo = new WebService.POJO();
    pojo.testInt = 100;
    pojo.testString = "test123";

    final Response responseMsg = target.path(ROOT).request().post(Entity.json(pojo));
    Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), responseMsg.getStatus());
  }

  @Test
  public void testPUT() {
    final Response responseMsg = target.path(ROOT).queryParam("something","test123").request().put(Entity.json(""));
    Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), responseMsg.getStatus());
  }

  @Test
  public void testDelete() {
    final Response responseMsg = target.path(ROOT+"de305d54-75b4-431b-adb2-eb6b9e546014").request().delete();
    Assert.assertEquals(Response.Status.OK.getStatusCode(), responseMsg.getStatus());
  }

  @Test
  public void testDeleteError() {
    final Response responseMsg = target.path(ROOT+"badUUID").request().delete();
    Assert.assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), responseMsg.getStatus());
  }

  @After
  public void teardown() {
    if (server != null) {
      server.shutdownNow();
    }
  }
}
