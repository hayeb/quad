package nl.quad.opentrivia.client.opentrivia;

import nl.quad.opentrivia.client.opentrivia.exception.OpenTriviaClientException;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaDifficulty;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestion;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class OpenTriviaClientService {

    private final RestClient restClient;

    public OpenTriviaClientService(RestClient.Builder builder, @Value("${opentrivia.baseUrl}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public QuestionsResponse getQuestions(int amount, Integer category, OpenTriviaDifficulty difficulty, OpenTriviaQuestionType questionType) {
        // https://opentdb.com/api.php?amount=10&category=21&difficulty=medium&type=multiple
        // For now, no support for session tokens
        ResponseEntity<QuestionsResponse> response = restClient.get().uri("/api.php?amount={amount}&category={category}&difficulty={difficulty}&type={type}", Map.of(
                "amount", amount,
                "category", Optional.ofNullable(category).map(Object::toString).orElse(""),
                "difficulty", Optional.ofNullable(difficulty).map(OpenTriviaDifficulty::getValue).orElse(""),
                "type", Optional.ofNullable(questionType).map(OpenTriviaQuestionType::getValue).orElse("")
            ))
            .accept(APPLICATION_JSON)
            .retrieve()
            .toEntity(QuestionsResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<OpenTriviaQuestion> decodedQuestions = response.getBody().results().stream()
                .map(this::decodeQuestion)
                .toList();
            return new QuestionsResponse(response.getBody().responseCode(), decodedQuestions);
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new OpenTriviaClientException("API returned response code: %d".formatted(response.getStatusCode().value()));
        } else {
            throw new OpenTriviaClientException("API returned response code 0 but response body was null");
        }
    }

    private OpenTriviaQuestion decodeQuestion(OpenTriviaQuestion openTriviaQuestion) {
        return openTriviaQuestion.toBuilder()
            .question(StringEscapeUtils.unescapeHtml4(openTriviaQuestion.question()))
            .correctAnswer(StringEscapeUtils.unescapeHtml4(openTriviaQuestion.correctAnswer()))
            .incorrectAnswers(openTriviaQuestion.incorrectAnswers().stream().map(StringEscapeUtils::unescapeHtml4).toList())
            .build();
    }
}
