package nl.quad.opentrivia.functionality.questions.rest.service;

import nl.quad.opentrivia.functionality.questions.model.Question;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ShuffleAnswersService {

    public List<String> shuffleAnswers(Question question) {
        List<String> allAnswers = new ArrayList<>();
        allAnswers.add(question.correctAnswer());
        allAnswers.addAll(question.incorrectAnswers());
        Collections.shuffle(allAnswers);
        return allAnswers;
    }
}
