package graphql.kickstart.spring.web.boot.metrics;

import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Rodrigues
 */
public class MetricsInstrumentation extends TracingInstrumentation {

    private MeterRegistry meterRegistry;

    private static final String QUERY_TIME_METRIC_NAME = "graphql.timer.query";
    private static final String RESOLVER_TIME_METRIC_NAME = "graphql.timer.resolver";
    private static final String ERROR_COUNTER_METRIC_NAME = "graphql.cunter.error";
    private static final String OPERATION_NAME_TAG = "operationName";
    private static final String OPERATION = "operation";
    private static final String UNKNOWN_NAME = "unknown";
    private static final String PARENT = "parent";
    private static final String FIELD = "field";
    private static final String PATH = "path";
    private static final String CODE = "code";
    private static final String CLASSIFICATION = "classification";
    private static final String TIMER_DESCRIPTION = "Timer that records the time to fetch the data by Operation Name";
    private static final String COUNTER_DESCRIPTION = "Counter that records the nums to fetch the data by Operation Name";

    private static final String TRACING = "tracing";
    private static final String EXECUTION = "execution";
    private static final String DURATION = "duration";
    private static final String VALIDATION = "validation";
    private static final String PARSING = "parsing";
    private static final String RESOLVERS = "resolvers";

    private Boolean tracingEnabled;

    public MetricsInstrumentation(MeterRegistry meterRegistry, Boolean tracingEnabled) {
        this.meterRegistry = meterRegistry;
        this.tracingEnabled = tracingEnabled;
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        transformTracingInfoToMicrometer(executionResult, parameters.getOperation());
        transformErrorInfoToMicrometer(executionResult, parameters.getOperation());
        return CompletableFuture.completedFuture(executionResult);
    }

    private void transformTracingInfoToMicrometer(ExecutionResult executionResult, String operationName) {
        if (executionResult.getExtensions() != null && executionResult.getExtensions().containsKey(TRACING)) {
            Map<String, Object> tracingData = (Map<String, Object>) executionResult.getExtensions().get(TRACING);
            Timer executionTimer = buildQueryTimer(operationName, EXECUTION);
            executionTimer.record((long) tracingData.get(DURATION), TimeUnit.NANOSECONDS);

            //These next 2 ifs might not run if the document is cached on the document provider
            if (tracingData.containsKey(VALIDATION) && ((Map<String, Object>) tracingData.get(VALIDATION)).containsKey(DURATION)) {
                Timer validationTimer = buildQueryTimer(operationName, VALIDATION);
                validationTimer.record((long) ((Map<String, Object>) tracingData.get(VALIDATION)).get(DURATION), TimeUnit.NANOSECONDS);
            }
            if (tracingData.containsKey(PARSING) && ((Map<String, Object>) tracingData.get(PARSING)).containsKey(DURATION)) {
                Timer parsingTimer = buildQueryTimer(operationName, PARSING);
                parsingTimer.record((long) ((Map<String, Object>) tracingData.get(PARSING)).get(DURATION), TimeUnit.NANOSECONDS);
            }

            if (((Map<String, String>) tracingData.get(EXECUTION)).containsKey(RESOLVERS)) {

                ((List<Map<String, Object>>) ((Map<String, Object>) tracingData.get(EXECUTION)).get(RESOLVERS)).forEach(field -> {

                    Timer fieldTimer = buildFieldTimer(operationName, RESOLVERS, (String) field.get("parentType"), (String) field.get("fieldName"));
                    fieldTimer.record((long) field.get(DURATION), TimeUnit.NANOSECONDS);

                });

            }

            if (!tracingEnabled) {
                executionResult.getExtensions().remove(TRACING);
            }
        }
    }

    private void transformErrorInfoToMicrometer(ExecutionResult executionResult, String operationName) {
        if (executionResult.getErrors() != null && executionResult.getErrors().size() > 0) {
            for (GraphQLError error : executionResult.getErrors()) {
                String path = null, code = null, classification = null;
                if (error.getPath() != null && error.getPath().size() > 0) {
                    path = error.getPath().get(0).toString();
                }
                if (error.getExtensions() != null && error.getExtensions().containsKey("code")) {
                    code = error.getExtensions().get("code").toString();
                }
                if (error.getExtensions() != null && error.getExtensions().containsKey("classification")) {
                    classification = error.getExtensions().get("classification").toString();
                }
                buildErrorCounter(operationName, path, code, classification).increment();
            }
        }
    }

    private Timer buildQueryTimer(String operationName, String operation) {
        return Timer.builder(QUERY_TIME_METRIC_NAME)
                .description(TIMER_DESCRIPTION)
                .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_NAME)
                .tag(OPERATION, operation)
                .register(meterRegistry);
    }

    private Timer buildFieldTimer(String operationName, String operation, String parent, String field) {
        return Timer.builder(RESOLVER_TIME_METRIC_NAME)
                .description(TIMER_DESCRIPTION)
                .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_NAME)
                .tag(PARENT, parent)
                .tag(FIELD, field)
                .tag(OPERATION, operation)
                .register(meterRegistry);
    }

    private Counter buildErrorCounter(String operationName, String path, String code, String classification) {
        return Counter.builder(ERROR_COUNTER_METRIC_NAME)
                .description(COUNTER_DESCRIPTION)
                .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_NAME)
                .tag(PATH, path != null ? path : UNKNOWN_NAME)
                .tag(CODE, code != null ? code : UNKNOWN_NAME)
                .tag(CLASSIFICATION, classification != null ? classification : UNKNOWN_NAME)
                .register(meterRegistry);
    }


}
