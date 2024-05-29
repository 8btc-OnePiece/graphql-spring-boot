package graphql.kickstart.autoconfigure.web.servlet;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.HandshakeRequest;
import java.util.UUID;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

class WsSessionCsrfTokenRepository implements WsCsrfTokenRepository {

  private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

  private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME =
      HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");

  private String sessionAttributeName = DEFAULT_CSRF_TOKEN_ATTR_NAME;

  @Override
  public void saveToken(WsCsrfToken token, HandshakeRequest request) {
    HttpSession session = (HttpSession) request.getHttpSession();
    if (session != null) {
      if (token == null) {
        session.removeAttribute(this.sessionAttributeName);
      } else {
        session.setAttribute(this.sessionAttributeName, token);
      }
    }
  }

  @Override
  public WsCsrfToken loadToken(HandshakeRequest request) {
    HttpSession session = (HttpSession) request.getHttpSession();
    if (session == null) {
      return null;
    }
    return (WsCsrfToken) session.getAttribute(this.sessionAttributeName);
  }

  @Override
  public WsCsrfToken generateToken(HandshakeRequest request) {
    return new DefaultWsCsrfToken(UUID.randomUUID().toString(), DEFAULT_CSRF_PARAMETER_NAME);
  }
}
