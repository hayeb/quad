package nl.quad.opentrivia.functionality.categories.mapper;

import nl.quad.opentrivia.client.opentrivia.model.OpenTriviaCategory;
import nl.quad.opentrivia.functionality.categories.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    Category toCategory(OpenTriviaCategory category);


}
