package nl.quad.opentrivia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnswerStore {

    private static final File ANSWERS = new File("answers.json");

    private final ObjectMapper objectMapper;

    public void storeAnswer(String question, String answer) {
        Map<String, String> answers = getStoredAnswers();
        answers.put(question, answer);
        storeAnswers(answers);
    }

    public String getAnswer(String question) {
        return getStoredAnswers().get(question);
    }

    private Map<String, String> getStoredAnswers() {
        try {
            ANSWERS.createNewFile();
            return objectMapper.readValue(ANSWERS, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to read answers: %s".formatted(e.getMessage()));
        }
    }

    private void storeAnswers(Map<String, String> answers) {
        objectMapper.writeValue(ANSWERS, answers);
    }

    public void init() {
        storeAnswers(Map.of());
    }
}
