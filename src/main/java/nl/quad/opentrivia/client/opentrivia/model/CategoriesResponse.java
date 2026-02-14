package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoriesResponse(
    @JsonProperty("trivia_categories")
    List<OpenTriviaCategory> categories) {
}
