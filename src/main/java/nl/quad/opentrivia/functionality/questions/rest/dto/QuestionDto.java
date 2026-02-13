package nl.quad.opentrivia.functionality.questions.rest.dto;

import java.util.List;

public record QuestionDto(String question, List<String> answers) {
}
