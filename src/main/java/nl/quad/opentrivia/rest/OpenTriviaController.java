package nl.quad.opentrivia.rest;

import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.client.opentrivia.model.Difficulty;
import nl.quad.opentrivia.client.opentrivia.model.QuestionType;
import nl.quad.opentrivia.rest.dto.Check;
import nl.quad.opentrivia.rest.dto.CheckAnswer;
import nl.quad.opentrivia.rest.dto.Question;
import nl.quad.opentrivia.service.OpenTriviaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenTriviaController {

    private final OpenTriviaService openTriviaService;

    @GetMapping("/questions")
    public List<Question> getQuestions(
        @RequestParam(name = "amount", required = false, defaultValue = "20")
        Integer amount,
        @RequestParam(name = "category", required = false)
        Integer category,
        @RequestParam(name = "difficulty", required = false)
        Difficulty difficulty,
        @RequestParam(name = "questionType", required = false)
        QuestionType questionType) {
        return this.openTriviaService.findQuestions(amount, category, difficulty, questionType);
    }

    @PostMapping("/checkanswers")
    public List<Check> checkAnswers(
        @RequestBody
        List<CheckAnswer> answers) {
        return this.openTriviaService.check(answers);
    }
}
