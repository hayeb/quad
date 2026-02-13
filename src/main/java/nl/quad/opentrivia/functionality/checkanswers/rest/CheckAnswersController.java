package nl.quad.opentrivia.functionality.checkanswers.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.AnswerDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.mapper.CheckAnswersDtoMapper;
import nl.quad.opentrivia.functionality.checkanswers.service.CheckAnswersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkanswers")
@RequiredArgsConstructor
public class CheckAnswersController {

    private final CheckAnswersService checkAnswersService;

    private final CheckAnswersDtoMapper checkAnswersDtoMapper;

    @PostMapping
    public List<CheckDto> checkAnswers(
        @RequestBody
        @Valid
        @NotEmpty
        List<AnswerDto> answers) {
        return this.checkAnswersService.checkAnswers(answers.stream().map(checkAnswersDtoMapper::toAnswer).toList())
            .stream()
            .map(checkAnswersDtoMapper::toCheckDto)
            .toList();
    }
}
