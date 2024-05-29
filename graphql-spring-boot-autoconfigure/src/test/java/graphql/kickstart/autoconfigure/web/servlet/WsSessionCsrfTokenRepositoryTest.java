package graphql.kickstart.autoconfigure.web.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.HandshakeRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WsSessionCsrfTokenRepositoryTest {

  public static final String TOKEN_SESSION_ATTRIBUTE_NAME =
      "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
  @Mock private HandshakeRequest handshakeRequest;
  @Mock private HttpSession httpSession;
  @Mock private WsCsrfToken csrfToken;
  private WsSessionCsrfTokenRepository tokenRepository = new WsSessionCsrfTokenRepository();

  @Test
  void givenNoSession_whenSaveToken_thenDoesNotThrow() {
    when(handshakeRequest.getHttpSession()).thenReturn(null);
    assertDoesNotThrow(() -> tokenRepository.saveToken(csrfToken, handshakeRequest));
  }

  @Test
  void givenNoToken_whenSaveToken_thenRemovesFromSession() {
    when(handshakeRequest.getHttpSession()).thenReturn(httpSession);
    tokenRepository.saveToken(null, handshakeRequest);
    verify(httpSession).removeAttribute(TOKEN_SESSION_ATTRIBUTE_NAME);
  }

  @Test
  void givenToken_whenSaveToken_thenSetsInSession() {
    when(handshakeRequest.getHttpSession()).thenReturn(httpSession);
    tokenRepository.saveToken(csrfToken, handshakeRequest);
    verify(httpSession).setAttribute(TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken);
  }

  @Test
  void givenNoSession_whenLoadToken_thenReturnNull() {
    when(handshakeRequest.getHttpSession()).thenReturn(null);
    WsCsrfToken csrfToken = tokenRepository.loadToken(handshakeRequest);
    assertThat(csrfToken).isNull();
  }

  @Test
  void givenTokenInSession_whenLoadToken_thenReturnTokenFromSession() {
    when(handshakeRequest.getHttpSession()).thenReturn(httpSession);
    when(httpSession.getAttribute(TOKEN_SESSION_ATTRIBUTE_NAME)).thenReturn(csrfToken);
    WsCsrfToken loadedToken = tokenRepository.loadToken(handshakeRequest);
    assertThat(loadedToken).isEqualTo(csrfToken);
  }

  @Test
  void whenGenerateToken_thenContainsUUID() {
    var generatedToken = tokenRepository.generateToken(handshakeRequest);
    assertDoesNotThrow(() -> UUID.fromString(generatedToken.getToken()));
  }

  @Test
  void whenGenerateToken_thenContainsCorrectParameterName() {
    var generatedToken = tokenRepository.generateToken(handshakeRequest);
    assertThat(generatedToken.getParameterName()).isEqualTo("_csrf");
  }
}
