package graphql.kickstart.autoconfigure.web.servlet;

import static java.util.Collections.emptyList;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("graphql.servlet.subscriptions.websocket")
class GraphQLSubscriptionWebsocketProperties {

  private String path = "/subscriptions";
  private List<String> allowedOrigins = emptyList();
  private CsrfProperties csrf = new CsrfProperties();

  @Data
  static
  class CsrfProperties {

    private boolean enabled = false;
  }
}
