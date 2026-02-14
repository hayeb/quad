package nl.quad.opentrivia.functionality.questions.mapper;

import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaDifficulty;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestion;
import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaQuestionType;
import nl.quad.opentrivia.functionality.questions.model.Difficulty;
import nl.quad.opentrivia.functionality.questions.model.Question;
import nl.quad.opentrivia.functionality.questions.model.QuestionType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionsMapper {
    OpenTriviaDifficulty toOpenTriviaDifficulty(Difficulty difficulty);

    OpenTriviaQuestionType toOpenTriviaQuestionType(QuestionType questionType);

    Question toQuestion(OpenTriviaQuestion openTriviaQuestion);
}
