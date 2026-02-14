package nl.quad.opentrivia.functionality.categories.rest;

import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaCategoriesResponse;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaCategory;
import nl.quad.opentrivia.functionality.categories.rest.dto.CategoryDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ExtendWith(MockitoExtension.class)
public class CategoriesControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private OpenTriviaClientService openTriviaClientService;

    @Test
    public void getCategoriesTest() {
        Mockito.when(openTriviaClientService.getCategories())
            .thenReturn(new OpenTriviaCategoriesResponse(List.of(
                new OpenTriviaCategory(4L, "Cat 4"),
                new OpenTriviaCategory(3L, "Cat 3"),
                new OpenTriviaCategory(2L, "Cat 2"),
                new OpenTriviaCategory(1L, "Cat 1")
            )));
        restTestClient
            .get()
            .uri("http://localhost:%d/api/categories".formatted(port))
            .exchangeSuccessfully()
            .expectBody(CategoryDto[].class)
            .isEqualTo(new CategoryDto[]{
                new CategoryDto(1L, "Cat 1"),
                new CategoryDto(2L, "Cat 2"),
                new CategoryDto(3L, "Cat 3"),
                new CategoryDto(4L, "Cat 4"),
            });
    }
}

