package nl.quad.opentrivia.functionality.questions.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.mapper.OpenTriviaClientMapper;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.exception.OpenTriviaException;
import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.questions.model.Difficulty;
import nl.quad.opentrivia.functionality.questions.model.Question;
import nl.quad.opentrivia.functionality.questions.model.QuestionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionsService {

    private final OpenTriviaClientService openTriviaClientService;
    private final AnswerStore answerStore;
    private final OpenTriviaClientMapper openTriviaClientMapper;

    /**
     * Retrieves questions from the OpenTDB API and stores the correct answer for future checking using the {@link AnswerStore}
     *
     * @param amount       Amount of questions to return validated to be >= 1 && <= 50
     * @param category     Optional category to search in
     * @param difficulty   Optional difficulty filter
     * @param questionType Optional question type filter
     */
    public List<Question> findQuestions(
        @Min(1) @Max(50) @Valid Integer amount,
        Integer category,
        Difficulty difficulty,
        QuestionType questionType) {

        QuestionsResponse response = openTriviaClientService.getQuestions(
            amount,
            category,
            openTriviaClientMapper.toOpenTriviaDifficulty(difficulty),
            openTriviaClientMapper.toOpenTriviaQuestionType(questionType));

        if (response.responseCode() != 0) {
            throw new OpenTriviaException("Open Trivia returned response code %d: %s".formatted(response.responseCode(), openTriviaResponseText(response.responseCode())));
        }

        return response.results().stream()
            .map(openTriviaClientMapper::toQuestion)
            .peek(question -> answerStore.storeAnswer(question.question(), question.correctAnswer()))
            .toList();
    }

    private String openTriviaResponseText(int i) {
        return switch (i) {
            case 1 -> "No results from OpenTrivia";
            case 2 -> "Invalid parameters";
            case 3 -> "Token not found";
            case 4 -> "Token empty";
            case 5 -> "Rate limited, please try again later";
            default -> "Unknown error from OpenTrivia API";
        };
    }


}
