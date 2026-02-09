package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Question(
        @JsonProperty("type")
        QuestionType questionType,
        Difficulty difficulty,
        String category,
        String question,

        @JsonProperty("correct_answer")
        String correct,

        @JsonProperty("incorrect_answers")
        List<String> incorrect) {
}
