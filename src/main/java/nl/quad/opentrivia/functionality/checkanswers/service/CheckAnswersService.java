package nl.quad.opentrivia.functionality.checkanswers.service;

import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.functionality.answerstore.service.AnswerStore;
import nl.quad.opentrivia.functionality.checkanswers.model.Answer;
import nl.quad.opentrivia.functionality.checkanswers.model.Check;
import nl.quad.opentrivia.functionality.checkanswers.model.CheckResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckAnswersService {

    private final AnswerStore answerStore;

    /**
     * Check the provided answers using the {@link AnswerStore}
     *
     * @param answers
     * @return
     */
    public List<Check> checkAnswers(List<Answer> answers) {
        return answers.stream()
            .map(answer -> new Check(answer.question(), this.checkAnswer(answer)))
            .toList();
    }

    private CheckResult checkAnswer(Answer checkRequest) {
        return Optional.ofNullable(this.answerStore.getAnswer(checkRequest.question()))
            .map(a -> a.equals(checkRequest.answer()) ? CheckResult.CORRECT : CheckResult.INCORRECT)
            .orElse(CheckResult.UNKNOWN);

    }
}
