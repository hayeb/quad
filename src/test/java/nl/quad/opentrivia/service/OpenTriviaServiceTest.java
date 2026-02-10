package nl.quad.opentrivia.service;

import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.Difficulty;
import nl.quad.opentrivia.client.opentrivia.model.Question;
import nl.quad.opentrivia.client.opentrivia.model.QuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.rest.dto.CheckResponseDto;
import nl.quad.opentrivia.rest.dto.CheckRequestDto;
import nl.quad.opentrivia.rest.dto.QuestionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class OpenTriviaServiceTest {

    @Mock
    private OpenTriviaClientService openTriviaClientService;

    @Mock
    private AnswerStore answerStore;

    @InjectMocks
    private OpenTriviaService openTriviaService;

    @Test
    void testQuestions() {
        Mockito.when(openTriviaClientService.getQuestions(anyInt(), anyInt(), any(), any()))
            .thenReturn(new QuestionsResponse(0, List.of(
                new Question(QuestionType.MULTIPLE, Difficulty.EASY, "Sports", "Who won the 2016 Formula 1 World Driver's Championship?", "Nico Rosberg", List.of("Lewis Hamilton", "Max Verstappen", "Kimi Raikkonen")))));
        List<QuestionDto> questions = openTriviaService.findQuestions(20, 21, Difficulty.EASY, QuestionType.MULTIPLE);
        assertThat(questions).hasSize(1);
        assertThat(questions).element(0).extracting(QuestionDto::value).isEqualTo("Who won the 2016 Formula 1 World Driver's Championship?");
        List<String> answers = questions.getFirst().answers();
        assertThat(answers).containsExactlyInAnyOrderElementsOf(List.of("Nico Rosberg", "Lewis Hamilton", "Max Verstappen", "Kimi Raikkonen"));
    }

    @Test
    void testCheckCorrect() {
        Mockito.when(answerStore.getAnswer("Who won the 2016 Formula 1 World Driver's Championship?")).thenReturn("Nico Rosberg");
        assertThat(openTriviaService.check(List.of(new CheckRequestDto("Who won the 2016 Formula 1 World Driver's Championship?", "Nico Rosberg"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new CheckResponseDto("Who won the 2016 Formula 1 World Driver's Championship?", true)));
    }

    @Test
    void testCheckIncorrect() {
        Mockito.when(answerStore.getAnswer("Who won the 2016 Formula 1 World Driver's Championship?")).thenReturn("Nico Rosberg");
        assertThat(openTriviaService.check(List.of(new CheckRequestDto("Who won the 2016 Formula 1 World Driver's Championship?", "Lewis Hamilton"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new CheckResponseDto("Who won the 2016 Formula 1 World Driver's Championship?", false)));
    }

    @Test
    void testCheckUnknown() {
        assertThat(openTriviaService.check(List.of(new CheckRequestDto("Unknown question", "Some answer"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new CheckResponseDto("Unknown question", false)));
    }


}