package nl.quad.opentrivia.client.opentrivia.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OpenTriviaDifficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    @Getter
    @JsonValue
    private final String value;
}
