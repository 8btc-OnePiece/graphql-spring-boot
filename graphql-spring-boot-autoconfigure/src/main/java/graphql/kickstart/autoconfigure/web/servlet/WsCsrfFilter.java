package graphql.kickstart.autoconfigure.web.servlet;

import static org.springframework.util.CollectionUtils.firstElement;

import graphql.kickstart.autoconfigure.web.servlet.GraphQLSubscriptionWebsocketProperties.CsrfProperties;
import jakarta.websocket.server.HandshakeRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class WsCsrfFilter {

  private final CsrfProperties csrfProperties;
  private final WsCsrfTokenRepository tokenRepository;

  void doFilter(HandshakeRequest request) {
    if (csrfProperties.isEnabled() && tokenRepository != null) {
      WsCsrfToken csrfToken = tokenRepository.loadToken(request);
      boolean missingToken = csrfToken == null;
      if (missingToken) {
        csrfToken = tokenRepository.generateToken(request);
        tokenRepository.saveToken(csrfToken, request);
      }

      String actualToken =
          firstElement(request.getParameterMap().get(csrfToken.getParameterName()));
      if (!Objects.equals(csrfToken.getToken(), actualToken)) {
        throw new IllegalStateException(
            "Invalid CSRF Token '"
                + actualToken
                + "' was found on the request parameter '"
                + csrfToken.getParameterName()
                + "'.");
      }
    }
  }
}
