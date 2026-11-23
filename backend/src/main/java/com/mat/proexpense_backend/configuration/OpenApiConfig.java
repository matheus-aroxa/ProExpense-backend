package com.mat.proexpense_backend.configuration;

import java.util.List;
import java.util.Map;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  private static final String BEARER_AUTH = "bearerAuth";
  private static final String PROBLEM_DETAIL = "ProblemDetail";
  private static final String PROBLEM_JSON = "application/problem+json";
  private static final String MISSING_TOKEN = "Missing or invalid authentication token";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("ProExpense API")
            .version("v1")
            .description("API for managing shared accounts, cards, categories and expenses"))
        .components(new Components()
            .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
  }

  @Bean
  public OpenApiCustomizer problemDetailErrorResponses() {
    return openApi -> {
      openApi.getComponents().addSchemas(PROBLEM_DETAIL, problemDetailSchema());

      Content problemContent = new Content().addMediaType(PROBLEM_JSON,
          new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + PROBLEM_DETAIL)));

      openApi.getPaths().values().forEach(path -> path.readOperations().forEach(operation -> {
        operation.getResponses().forEach((status, response) -> {
          if (status.startsWith("4") || status.startsWith("5")) {
            response.setContent(problemContent);
          }
        });

        if (operation.getSecurity() == null) {
          operation.getResponses().putIfAbsent("403", new ApiResponse().description(MISSING_TOKEN));
        }
      }));
    };
  }

  private Schema<?> problemDetailSchema() {
    return new ObjectSchema()
        .description("RFC 9457 error response")
        .addProperty("type", new StringSchema().format("uri").example("about:blank"))
        .addProperty("title", new StringSchema().example("Bad Request"))
        .addProperty("status", new IntegerSchema().example(400))
        .addProperty("detail", new StringSchema().example("One or more fields are invalid"))
        .addProperty("instance", new StringSchema().format("uri").example("/api/v1/expenses"))
        .addProperty("errors", new MapSchema()
            .description("Validation messages grouped by field, present only on validation errors")
            .additionalProperties(new ArraySchema().items(new StringSchema()))
            .example(Map.of("name", List.of("Name is required"))));
  }
}
