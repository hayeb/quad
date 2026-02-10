package nl.quad.opentrivia.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.client.opentrivia.model.Difficulty;
import nl.quad.opentrivia.client.opentrivia.model.QuestionType;
import nl.quad.opentrivia.rest.dto.CheckResponseDto;
import nl.quad.opentrivia.rest.dto.CheckRequestDto;
import nl.quad.opentrivia.rest.dto.QuestionDto;
import nl.quad.opentrivia.service.OpenTriviaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenTriviaController {

    private final OpenTriviaService openTriviaService;

    @GetMapping("/questions")
    public List<QuestionDto> getQuestions(
        @RequestParam(name = "amount", required = false, defaultValue = "20")
        @Min(1)
        @Max(50)
        @Validated
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
    public List<CheckResponseDto> checkAnswers(
        @RequestBody
        List<CheckRequestDto> answers) {
        return this.openTriviaService.check(answers);
    }
}
