package nl.quad.opentrivia.functionality.checkanswers.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record AnswerDto(@NotEmpty String question, @NotEmpty String answer) {
}
