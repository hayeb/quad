package nl.quad.opentrivia.client.opentrivia;

import nl.quad.opentrivia.client.opentrivia.exception.OpenTriviaException;
import nl.quad.opentrivia.client.opentrivia.model.Difficulty;
import nl.quad.opentrivia.client.opentrivia.model.QuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class OpenTriviaClientService {

    private final RestClient restClient;

    public OpenTriviaClientService(RestClient.Builder builder, @Value("${opentrivia.baseUrl}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl)
            .configureMessageConverters(cb -> cb.withJsonConverter(
                new JacksonJsonHttpMessageConverter(JsonMapper.builder()
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                    .build())
            )).build();
    }

    public QuestionsResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType questionType) {
        // https://opentdb.com/api.php?amount=10&category=21&difficulty=medium&type=multiple
        // For now, no support for session tokens
        ResponseEntity<QuestionsResponse> response = restClient.get().uri("/api.php?amount={amount}&category={category}&difficulty={difficulty}&type={type}", Map.of(
                "amount", amount,
                "category", Optional.ofNullable(category).map(Object::toString).orElse(""),
                "difficulty", Optional.ofNullable(difficulty).map(Enum::name).map(String::toLowerCase).orElse(""),
                "type", Optional.ofNullable(questionType).map(Enum::name).map(String::toLowerCase).orElse("")
            ))
            .accept(APPLICATION_JSON)
            .retrieve().toEntity(QuestionsResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new OpenTriviaException("API returned response code: %d".formatted(response.getStatusCode().value()));

    }
}
