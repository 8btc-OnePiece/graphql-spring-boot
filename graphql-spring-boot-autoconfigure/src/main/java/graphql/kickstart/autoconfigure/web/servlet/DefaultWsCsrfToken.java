package graphql.kickstart.autoconfigure.web.servlet;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class DefaultWsCsrfToken implements WsCsrfToken {

  private final String token;
  private final String parameterName;

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public String getParameterName() {
    return parameterName;
  }
}
