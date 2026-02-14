package nl.quad.opentrivia.client.opentrivia;

import nl.quad.opentrivia.client.opentrivia.exception.OpenTriviaClientException;
import nl.quad.opentrivia.client.opentrivia.model.*;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public OpenTriviaQuestionsResponse getQuestions(int amount, Integer category, OpenTriviaDifficulty difficulty, OpenTriviaQuestionType questionType) {
        // https://opentdb.com/api.php?amount=10&category=21&difficulty=medium&type=multiple
        // For now, no support for session tokens
        ResponseEntity<OpenTriviaQuestionsResponse> response = restClient.get().uri("/api.php?amount={amount}&category={category}&difficulty={difficulty}&type={type}", Map.of(
                "amount", amount,
                "category", Optional.ofNullable(category).map(Object::toString).orElse(""),
                "difficulty", Optional.ofNullable(difficulty).map(OpenTriviaDifficulty::getValue).orElse(""),
                "type", Optional.ofNullable(questionType).map(OpenTriviaQuestionType::getValue).orElse("")
            ))
            .accept(APPLICATION_JSON)
            .retrieve()

            // Ignore HTTP 400 error codes because OpenTDB still sends a valid QuestionsResponse with a usable error code.s
            .onStatus(HttpStatusCode::is4xxClientError, (req, r) -> {
            })
            .toEntity(OpenTriviaQuestionsResponse.class);

        OpenTriviaQuestionsResponse questionsResponse = handleOpenTriviaResponse(response, "Questions API");
        List<OpenTriviaQuestion> decodedQuestions = Optional.of(questionsResponse)
            .map(OpenTriviaQuestionsResponse::results)
            .map(questions -> questions.stream()
                .map(this::decodeQuestion)
                .toList()).orElse(null);
        return new OpenTriviaQuestionsResponse(questionsResponse.responseCode(), decodedQuestions);
    }

    public OpenTriviaCategoriesResponse getCategories() {
        return handleOpenTriviaResponse(
            restClient.get().uri("/api_category.php").accept(APPLICATION_JSON).retrieve().toEntity(OpenTriviaCategoriesResponse.class),
            "Categories API");
    }

    private OpenTriviaQuestion decodeQuestion(OpenTriviaQuestion openTriviaQuestion) {
        return openTriviaQuestion.toBuilder()
            .question(StringEscapeUtils.unescapeHtml4(openTriviaQuestion.question()))
            .correctAnswer(StringEscapeUtils.unescapeHtml4(openTriviaQuestion.correctAnswer()))
            .incorrectAnswers(openTriviaQuestion.incorrectAnswers().stream().map(StringEscapeUtils::unescapeHtml4).toList())
            .build();
    }

    private <T> T handleOpenTriviaResponse(ResponseEntity<T> response, String api) {
        if (response.getBody() != null) {
            return response.getBody();
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new OpenTriviaClientException("%s returned response code: %d".formatted(api, response.getStatusCode().value()));
        } else {
            throw new OpenTriviaClientException("%s returned response code 200 OK but response body was null".formatted(api));
        }
    }
}
