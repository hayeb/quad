package nl.quad.opentrivia.functionality.checkanswers.rest;

import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.AnswerDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckResultDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ExtendWith(MockitoExtension.class)
public class CheckAnswersControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private AnswerStore answerStore;

    @Test
    public void getQuestionsTest() {
        Mockito.when(answerStore.getAnswer(eq("Q1"))).thenReturn("True");
        Mockito.when(answerStore.getAnswer(eq("Q2"))).thenReturn("False");
        Mockito.when(answerStore.getAnswer(eq("Q3"))).thenReturn("True");
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

    @Test
    public void getQuestionsInvalidRequest() {
        restTestClient.post()
            .uri("http://localhost:%d/api/checkanswers".formatted(port))
            .body(List.of(
                new AnswerDto("", "")
            )).exchange().expectStatus().is4xxClientError();
    }
}
