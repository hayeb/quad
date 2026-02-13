package nl.quad.opentrivia.functionality.checkanswers.rest.mapper;

import nl.quad.opentrivia.functionality.checkanswers.model.Answer;
import nl.quad.opentrivia.functionality.checkanswers.model.Check;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.AnswerDto;
import nl.quad.opentrivia.functionality.checkanswers.rest.dto.CheckDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckAnswersDtoMapper {

    Answer toAnswer(AnswerDto answer);

    CheckDto toCheckDto(Check check);
}
