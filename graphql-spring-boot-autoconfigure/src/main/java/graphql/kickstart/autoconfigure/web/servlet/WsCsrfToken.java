package graphql.kickstart.autoconfigure.web.servlet;

import java.io.Serializable;

public interface WsCsrfToken extends Serializable {

    String getToken();

    String getParameterName();
}
