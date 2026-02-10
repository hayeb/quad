package nl.quad.opentrivia.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnswerStoreTest {

    @TempDir
    private File tempDir;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AnswerStore answerStore;

    private File answersFile;

    @BeforeEach
    public void init() {
        Path testAnswers = tempDir.toPath().resolve("answers-test.json");
        answerStore = new AnswerStore(objectMapper, testAnswers.toString());
        answersFile = testAnswers.toFile();

        answerStore.init();
    }

    @Test
    void testInit() throws IOException {
        answerStore.init();

        String answersContents = FileUtils.readFileToString(answersFile, StandardCharsets.UTF_8);
        assertEquals(Map.of(), objectMapper.readValue(answersContents, new TypeReference<Map<String, String>>() {}));
    }

    @Test
    void testStoreAnswer() throws IOException {
        answerStore.storeAnswer("Q1", "A1");
        answerStore.storeAnswer("Q2", "A2");

        String answersContents = FileUtils.readFileToString(answersFile, StandardCharsets.UTF_8);
        assertEquals(Map.of("Q1", "A1", "Q2", "A2"), objectMapper.readValue(answersContents, new TypeReference<Map<String, String>>() {
        }));
    }

    @Test
    void testGetAnswer(){
        answerStore.storeAnswer("Q1", "A1");
        assertEquals("A1", answerStore.getAnswer("Q1"));
        assertNull(answerStore.getAnswer("Q2"));
    }

}