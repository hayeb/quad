package nl.quad.opentrivia.integration;

import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaDifficulty;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestion;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.AnswerDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckResultDto;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ExtendWith(MockitoExtension.class)
public class AnswerQuestionsIntegrationTest {

    @TempDir
    private static File tempDir;

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private OpenTriviaClientService openTriviaClientService;

    @TestBean
    private AnswerStore answerStore;

    static AnswerStore answerStore() {
        return new AnswerStore(new ObjectMapper(), tempDir.toPath().resolve("answers-test.json").toString());
    }

    /**
     * Questions may be retrieved via the /questions API anD answers checked via the /checkanswers API.
     */
    @Test
    public void getQuestionsTest() {
        Mockito.when(openTriviaClientService.getQuestions(10, 1, OpenTriviaDifficulty.MEDIUM, OpenTriviaQuestionType.BOOLEAN))
            .thenReturn(new QuestionsResponse(0, List.of(
                new OpenTriviaQuestion(OpenTriviaQuestionType.BOOLEAN, OpenTriviaDifficulty.MEDIUM, "Cat 1", "Q1", "True", List.of("False")),
                new OpenTriviaQuestion(OpenTriviaQuestionType.BOOLEAN, OpenTriviaDifficulty.MEDIUM, "Cat 1", "Q2", "False", List.of("True")),
                new OpenTriviaQuestion(OpenTriviaQuestionType.BOOLEAN, OpenTriviaDifficulty.MEDIUM, "Cat 1", "Q3", "False", List.of("True")),
                new OpenTriviaQuestion(OpenTriviaQuestionType.BOOLEAN, OpenTriviaDifficulty.MEDIUM, "Cat 1", "Q4", "True", List.of("False"))
            )));
        restTestClient
            .get()
            .uri("http://localhost:%d/api/questions?amount=10&category=1&difficulty=MEDIUM&questionType=BOOLEAN".formatted(port))
            .exchangeSuccessfully()
            .expectBody(QuestionDto[].class)
            .value(questions -> {
                assertThat(questions).hasSize(4);
                assertThat(questions[0]).extracting(QuestionDto::question).isEqualTo("Q1");
                assertThat(questions[0].answers()).containsExactlyInAnyOrder("True", "False");
                assertThat(questions[1]).extracting(QuestionDto::question).isEqualTo("Q2");
                assertThat(questions[1].answers()).containsExactlyInAnyOrder("True", "False");
                assertThat(questions[2]).extracting(QuestionDto::question).isEqualTo("Q3");
                assertThat(questions[2].answers()).containsExactlyInAnyOrder("True", "False");
                assertThat(questions[3]).extracting(QuestionDto::question).isEqualTo("Q4");
                assertThat(questions[3].answers()).containsExactlyInAnyOrder("True", "False");
            });

        restTestClient.post()
            .uri("http://localhost:%d/api/checkanswers".formatted(port))
            .body(List.of(
                new AnswerDto("Q1", "True"), // Correct
                new AnswerDto("Q2", "True"), // Incorrect
                new AnswerDto("Q13371337", "True") // Unknown question
            ))
            .exchangeSuccessfully().expectBody(CheckDto[].class)
            .isEqualTo(new CheckDto[]{
                new CheckDto("Q1", CheckResultDto.CORRECT),
                new CheckDto("Q2", CheckResultDto.INCORRECT),
                new CheckDto("Q13371337", CheckResultDto.UNKNOWN)
            });

    }
}
