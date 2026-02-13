package nl.quad.opentrivia.functionality.questions.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.functionality.questions.rest.dto.DifficultyDto;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionDto;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionTypeDto;
import nl.quad.opentrivia.functionality.questions.rest.mapper.QuestionsDtoMapper;
import nl.quad.opentrivia.functionality.questions.service.QuestionsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionsController {

    private final QuestionsService questionsService;

    private final QuestionsDtoMapper dtoMapper;

    @GetMapping
    public List<QuestionDto> getQuestions(
        @RequestParam(name = "amount", required = false, defaultValue = "20")
        @Min(1)
        @Max(50)
        @Validated
        Integer amount,
        @RequestParam(name = "category", required = false)
        Integer category,
        @RequestParam(name = "difficulty", required = false)
        DifficultyDto difficulty,
        @RequestParam(name = "questionType", required = false)
        QuestionTypeDto questionType) {
        return this.questionsService.findQuestions(
                amount,
                category,
                dtoMapper.toDifficulty(difficulty),
                dtoMapper.toQuestionType(questionType))
            .stream()
            .map(dtoMapper::toQuestionDto)
            .toList();
    }
}
