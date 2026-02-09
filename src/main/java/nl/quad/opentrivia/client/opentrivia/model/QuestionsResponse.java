package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record QuestionsResponse(
        @JsonProperty("response_code")
        int responseCode,
        List<Question> results) {
}
