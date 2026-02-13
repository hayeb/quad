package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OpenTriviaQuestionType {
    MULTIPLE_CHOICE("multiple"),
    BOOLEAN("boolean");

    @Getter
    @JsonValue
    private final String value;
}
