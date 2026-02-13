package nl.quad.opentrivia.functionality.questions.rest.mapper;

import nl.quad.opentrivia.functionality.questions.model.Difficulty;
import nl.quad.opentrivia.functionality.questions.model.Question;
import nl.quad.opentrivia.functionality.questions.model.QuestionType;
import nl.quad.opentrivia.functionality.questions.rest.dto.DifficultyDto;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionDto;
import nl.quad.opentrivia.functionality.questions.rest.dto.QuestionTypeDto;
import nl.quad.opentrivia.functionality.questions.rest.service.ShuffleAnswersService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class QuestionsDtoMapper {

    @Autowired
    protected ShuffleAnswersService shuffleAnswersService;

    @Mapping(target = "answers", source = ".")
    public abstract QuestionDto toQuestionDto(Question question);

    public abstract QuestionType toQuestionType(QuestionTypeDto dto);

    public abstract Difficulty toDifficulty(DifficultyDto dto);

    List<String> shuffleAnswers(Question question) {
        return this.shuffleAnswersService.shuffleAnswers(question);
    }
}
