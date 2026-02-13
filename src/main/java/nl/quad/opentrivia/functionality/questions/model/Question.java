package nl.quad.opentrivia.functionality.questions.model;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record Question(
    QuestionType questionType,
    Difficulty difficulty,
    String category,
    String question,
    String correctAnswer,
    List<String> incorrectAnswers) {

}
