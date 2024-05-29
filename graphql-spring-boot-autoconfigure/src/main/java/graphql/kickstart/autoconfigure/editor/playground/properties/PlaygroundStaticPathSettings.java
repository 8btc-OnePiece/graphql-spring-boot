package graphql.kickstart.autoconfigure.editor.playground.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlaygroundStaticPathSettings {

  @NotBlank private String base = "/vendor/playground";
}
