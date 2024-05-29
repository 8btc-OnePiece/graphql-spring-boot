package graphql.kickstart.autoconfigure.web.servlet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import graphql.kickstart.servlet.GraphQLWebsocketServlet;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GraphQLWsServerEndpointRegistrationTest {

  private static final String PATH = "/subscriptions";

  @Mock private GraphQLWebsocketServlet servlet;
  @Mock private WsCsrfFilter csrfFilter;

  @ParameterizedTest
  @CsvSource(
      value = {"https://trusted.com", "NULL", "' '"},
      nullValues = {"NULL"})
  void givenDefaultAllowedOrigins_whenCheckOrigin_thenReturnTrue(String origin) {
    var registration = createRegistration();
    var allowed = registration.checkOrigin("null".equals(origin) ? null : origin);
    assertThat(allowed).isTrue();
  }

  private GraphQLWsServerEndpointRegistration createRegistration(String... allowedOrigins) {
    return new GraphQLWsServerEndpointRegistration(
        PATH, servlet, csrfFilter, List.of(allowedOrigins));
  }

  @ParameterizedTest(name = "{index} => allowedOrigin=''{0}'', originToCheck=''{1}''")
  @CsvSource(
      delimiterString = "|",
      textBlock =
          """
    *                    | https://trusted.com
    https://trusted.com  | https://trusted.com
    https://trusted.com/ | https://trusted.com
    https://trusted.com/ | https://trusted.com/
    https://trusted.com  | https://trusted.com/
""")
  void givenAllowedOrigins_whenCheckOrigin_thenReturnTrue(
      String allowedOrigin, String originToCheck) {
    var registration = createRegistration(allowedOrigin);
    var allowed = registration.checkOrigin(originToCheck);
    assertThat(allowed).isTrue();
  }
}
