package nl.quad.opentrivia.client.opentrivia;

import nl.quad.opentrivia.client.opentrivia.model.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OpenTriviaClientService.class)
class OpenTriviaClientServiceTest {
    @Autowired
    private OpenTriviaClientService openTriviaClientService;
    @Autowired
    private MockRestServiceServer server;

    @Test
    public void getQuestions() throws IOException {
        InputStream questionsResponse = getClass().getResourceAsStream("/client/opentrivia/getQuestionsResponse.json");
        assertThat(questionsResponse).isNotNull();

        server.expect(requestTo("https://opentdb.com/api.php?amount=10&category=&difficulty=&type="))
            .andRespond(withSuccess(IOUtils.toString(questionsResponse, StandardCharsets.UTF_8), MediaType.APPLICATION_JSON));

        OpenTriviaQuestionsResponse questions = openTriviaClientService.getQuestions(10, null, null, null);
        assertThat(questions).extracting(OpenTriviaQuestionsResponse::responseCode).isEqualTo(0);
        assertThat(questions.results()).containsExactly(
            new OpenTriviaQuestion(OpenTriviaQuestionType.MULTIPLE_CHOICE, OpenTriviaDifficulty.EASY, "General Knowledge", "What company developed the vocaloid Hatsune Miku?", "Crypton Future Media", List.of("Sega",
                "Sony",
                "Yamaha Corporation")),
            new OpenTriviaQuestion(OpenTriviaQuestionType.BOOLEAN, OpenTriviaDifficulty.MEDIUM, "Politics", "The 2016 United States Presidential Election is the first time Hillary Clinton has run for President.", "False", List.of("True"))
        );
    }

    @Test
    public void getQuestionsError() throws IOException {
        InputStream questionsResponse = getClass().getResourceAsStream("/client/opentrivia/getQuestionsResponseError.json");
        assertThat(questionsResponse).isNotNull();

        server.expect(requestTo("https://opentdb.com/api.php?amount=10&category=&difficulty=&type="))
            .andRespond(withBadRequest()
                .body(IOUtils.toString(questionsResponse, StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON));

        OpenTriviaQuestionsResponse questions = openTriviaClientService.getQuestions(10, null, null, null);
        assertThat(questions).extracting(OpenTriviaQuestionsResponse::responseCode).isEqualTo(1);
    }

    @Test
    public void getCategories() throws IOException {
        InputStream categoriesResponse = getClass().getResourceAsStream("/client/opentrivia/getCategoriesResponse.json");
        assertThat(categoriesResponse).isNotNull();

        server.expect(requestTo("https://opentdb.com/api_category.php"))
            .andRespond(withSuccess(IOUtils.toString(categoriesResponse, StandardCharsets.UTF_8), MediaType.APPLICATION_JSON));

        OpenTriviaCategoriesResponse categories = openTriviaClientService.getCategories();
        assertThat(categories.categories()).containsExactly(
            new OpenTriviaCategory(9L, "General Knowledge"),
            new OpenTriviaCategory(10L, "Entertainment: Books"),
            new OpenTriviaCategory(11L, "Entertainment: Film")
        );
    }

}