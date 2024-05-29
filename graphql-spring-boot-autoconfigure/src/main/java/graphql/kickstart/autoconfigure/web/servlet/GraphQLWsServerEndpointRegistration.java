package graphql.kickstart.autoconfigure.web.servlet;

import graphql.kickstart.servlet.GraphQLWebsocketServlet;
import java.util.ArrayList;
import java.util.List;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.Lifecycle;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

/**
 * @author Andrew Potter
 */
public class GraphQLWsServerEndpointRegistration extends ServerEndpointRegistration
    implements Lifecycle {

  private static final String ALL = "*";
  private final GraphQLWebsocketServlet servlet;
  private final WsCsrfFilter csrfFilter;
  private final List<String> allowedOrigins;

  public GraphQLWsServerEndpointRegistration(
      String path,
      GraphQLWebsocketServlet servlet,
      WsCsrfFilter csrfFilter,
      List<String> allowedOrigins) {
    super(path, servlet);
    this.servlet = servlet;
    if (allowedOrigins == null || allowedOrigins.isEmpty()) {
      this.allowedOrigins = List.of(ALL);
    } else {
      this.allowedOrigins = new ArrayList<>(allowedOrigins);
    }
    this.csrfFilter = csrfFilter;
  }

  @Override
  public boolean checkOrigin(String originHeaderValue) {
    if (originHeaderValue == null || originHeaderValue.isBlank()) {
      return allowedOrigins.contains(ALL);
    }
    if (allowedOrigins.contains(ALL)) {
      return true;
    }
    String originToCheck = trimTrailingSlash(originHeaderValue);
    return allowedOrigins.stream()
        .map(this::trimTrailingSlash)
        .anyMatch(originToCheck::equalsIgnoreCase);
  }

  private String trimTrailingSlash(String origin) {
    return (origin.endsWith("/") ? origin.substring(0, origin.length() - 1) : origin);
  }

  @Override
  public void modifyHandshake(
      ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    super.modifyHandshake(sec, request, response);
    csrfFilter.doFilter(request);
    servlet.modifyHandshake(sec, request, response);
  }

  @Override
  public void start() {
    // do nothing
  }

  @Override
  public void stop() {
    servlet.beginShutDown();
  }

  @Override
  public boolean isRunning() {
    return !servlet.isShutDown();
  }
}
