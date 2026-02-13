package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record OpenTriviaQuestion(
    @JsonProperty("type")
    OpenTriviaQuestionType questionType,
    OpenTriviaDifficulty difficulty,
    String category,
    String question,

    @JsonProperty("correct_answer")
    String correctAnswer,

    @JsonProperty("incorrect_answers")
    List<String> incorrectAnswers) {
}
