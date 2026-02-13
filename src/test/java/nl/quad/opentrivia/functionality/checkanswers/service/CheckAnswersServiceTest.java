package nl.quad.opentrivia.functionality.checkanswers.service;

import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.checkanswers.model.Answer;
import nl.quad.opentrivia.functionality.checkanswers.model.Check;
import nl.quad.opentrivia.functionality.checkanswers.model.CheckResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CheckAnswersServiceTest {

    @MockitoBean
    private AnswerStore answerStore;

    @Autowired
    private CheckAnswersService checkAnswersService;

    @Test
    void testCheckCorrect() {
        Mockito.when(answerStore.getAnswer("Who won the 2016 Formula 1 World Driver's Championship?")).thenReturn("Nico Rosberg");
        assertThat(checkAnswersService.checkAnswers(List.of(new Answer("Who won the 2016 Formula 1 World Driver's Championship?", "Nico Rosberg"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new Check("Who won the 2016 Formula 1 World Driver's Championship?", CheckResult.CORRECT)));
    }

    @Test
    void testCheckIncorrect() {
        Mockito.when(answerStore.getAnswer("Who won the 2016 Formula 1 World Driver's Championship?")).thenReturn("Nico Rosberg");
        assertThat(checkAnswersService.checkAnswers(List.of(new Answer("Who won the 2016 Formula 1 World Driver's Championship?", "Lewis Hamilton"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new Check("Who won the 2016 Formula 1 World Driver's Championship?", CheckResult.INCORRECT)));
    }

    @Test
    void testCheckUnknown() {
        assertThat(checkAnswersService.checkAnswers(List.of(new Answer("Unknown question", "Some answer"))))
            .containsExactlyInAnyOrderElementsOf(List.of(new Check("Unknown question", CheckResult.UNKNOWN)));
    }
}
