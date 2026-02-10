package nl.quad.opentrivia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;

@Component
public class AnswerStore {

    private final ObjectMapper objectMapper;

    private final File answersFile ;

    public AnswerStore(ObjectMapper objectMapper, @Value("${answersLocation}") String answersLocation) {
        this.objectMapper = objectMapper;
        this.answersFile = new File(answersLocation);
    }

    public void storeAnswer(String question, String answer) {
        Map<String, String> answers = getStoredAnswers();
        answers.put(question, answer);
        storeAnswers(answers);
    }

    public String getAnswer(String question) {
        return getStoredAnswers().get(question);
    }

    private Map<String, String> getStoredAnswers() {
        return objectMapper.readValue(answersFile, new TypeReference<Map<String, String>>() {});
    }

    private void storeAnswers(Map<String, String> answers) {
        try (RandomAccessFile f = new RandomAccessFile(answersFile, "rw"); FileChannel channel = f.getChannel(); FileLock lock = channel.lock()) {
            channel.truncate(0);
            objectMapper.writeValue(f, answers);
            lock.release();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to store answers: %s".formatted(e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        try {
            answersFile.createNewFile();
            storeAnswers(Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file: %s".formatted(e.getMessage()));
        }

    }
}
