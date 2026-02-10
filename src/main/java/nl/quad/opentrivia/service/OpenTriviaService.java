package nl.quad.opentrivia.service;

import lombok.AllArgsConstructor;
import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.Difficulty;
import nl.quad.opentrivia.client.opentrivia.model.QuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.rest.dto.CheckResponseDto;
import nl.quad.opentrivia.rest.dto.CheckRequestDto;
import nl.quad.opentrivia.rest.dto.QuestionDto;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OpenTriviaService {

    private final OpenTriviaClientService openTriviaClientService;
    private final AnswerStore answerStore;

    public List<QuestionDto> findQuestions(Integer amount, Integer category, Difficulty difficulty, QuestionType questionType) {
        QuestionsResponse response = openTriviaClientService.getQuestions(amount, category, difficulty, questionType);

        if (response.responseCode() != 0) {
            throw new RuntimeException("Open Trivia returned response code %d".formatted(response.responseCode()));
        }

        return response.results().stream()
            .map(q -> {
                String question = StringEscapeUtils.unescapeHtml4(q.question());
                String correct = StringEscapeUtils.unescapeHtml4(q.correct());
                answerStore.storeAnswer(question, correct);

                List<String> answers = new ArrayList<>();
                answers.add(correct);
                answers.addAll(q.incorrect().stream().map(StringEscapeUtils::unescapeHtml4).toList());

                Collections.shuffle(answers);
                return new QuestionDto(question, answers);
            })
            .toList();
    }

    public List<CheckResponseDto> check(List<CheckRequestDto> answers) {
        return answers.stream()
            .map(a -> new CheckResponseDto(a.question(), Objects.equals(this.answerStore.getAnswer(a.question()), a.answer())))
            .toList();
    }
}
