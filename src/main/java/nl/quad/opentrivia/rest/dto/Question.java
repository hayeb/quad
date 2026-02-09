package nl.quad.opentrivia.rest.dto;

import java.util.List;

public record Question(String value, List<String> answers ) {
}
