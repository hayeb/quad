package nl.quad.opentrivia.functionality.questions.rest;

import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaDifficulty;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestion;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ExtendWith(MockitoExtension.class)
class QuestionsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private OpenTriviaClientService openTriviaClientService;

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
    }

    @Test
    public void getQuestionsInvalidAmount() {
        restTestClient
            .get()
            .uri("http://localhost:%d/api/questions?amount=-1".formatted(port))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getQuestionsInvalidCategory() {
        Mockito.when(openTriviaClientService.getQuestions(anyInt(), eq(999), any(), any()))
            .thenReturn(new QuestionsResponse(2, null));
        restTestClient
            .get()
            .uri("http://localhost:%d/api/questions?category=999".formatted(port))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getQuestionsInvalidDifficulty() {
        restTestClient
            .get()
            .uri("http://localhost:%d/api/questions?difficulty=BLABLABLA".formatted(port))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getQuestionsInvalidQuestionType() {
        restTestClient
            .get()
            .uri("http://localhost:%d/api/questions?questionType=BLABLABLA".formatted(port))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

}