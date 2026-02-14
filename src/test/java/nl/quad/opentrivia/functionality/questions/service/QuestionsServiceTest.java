package nl.quad.opentrivia.functionality.questions.service;

import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaDifficulty;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestion;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestionType;
import nl.quad.opentrivia.client.opentrivia.model.QuestionsResponse;
import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.questions.model.Difficulty;
import nl.quad.opentrivia.functionality.questions.model.Question;
import nl.quad.opentrivia.functionality.questions.model.QuestionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class QuestionsServiceTest {

    @MockitoBean
    private OpenTriviaClientService openTriviaClientService;

    @MockitoBean
    private AnswerStore answerStore;

    @Autowired
    private QuestionsService questionsService;

    @Test
    void testQuestions() {
        Mockito.when(openTriviaClientService.getQuestions(anyInt(), anyInt(), any(), any()))
            .thenReturn(new QuestionsResponse(0, List.of(
                new OpenTriviaQuestion(OpenTriviaQuestionType.MULTIPLE_CHOICE, OpenTriviaDifficulty.EASY, "Sports", "Who won the 2016 Formula 1 World Driver's Championship?", "Nico Rosberg", List.of("Lewis Hamilton", "Max Verstappen", "Kimi Raikkonen")))));

        List<Question> questions = questionsService.findQuestions(20, 21, Difficulty.EASY, QuestionType.MULTIPLE_CHOICE);
        assertThat(questions).hasSize(1);
        assertThat(questions).element(0).extracting(Question::question).isEqualTo("Who won the 2016 Formula 1 World Driver's Championship?");
        assertThat(questions.getFirst().correctAnswer()).isEqualTo("Nico Rosberg");
        assertThat(questions.getFirst().incorrectAnswers()).isEqualTo(List.of("Lewis Hamilton", "Max Verstappen", "Kimi Raikkonen"));

        Mockito.verify(answerStore, Mockito.times(1)).storeAnswer("Who won the 2016 Formula 1 World Driver's Championship?", "Nico Rosberg");
    }
}