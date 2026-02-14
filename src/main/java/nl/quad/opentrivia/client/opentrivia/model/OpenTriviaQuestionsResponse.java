package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenTriviaQuestionsResponse(
    @JsonProperty("response_code")
    int responseCode,
    List<OpenTriviaQuestion> results) {
}
