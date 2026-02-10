package nl.quad.opentrivia.rest.dto;

import java.util.List;

public record QuestionDto(String value, List<String> answers ) {
}
