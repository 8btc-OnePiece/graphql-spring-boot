package graphql.kickstart.playground.boot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@JsonComponent
public class ResourceSerializer extends JsonSerializer<Resource> {
    @Override
    public void serialize(final Resource value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        final String content = StreamUtils.copyToString(value.getInputStream(), StandardCharsets.UTF_8);
        gen.writeString(content);
    }
}
