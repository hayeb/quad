package nl.quad.opentrivia.functionality.categories.rest.mapper;

import nl.quad.opentrivia.functionality.categories.model.Category;
import nl.quad.opentrivia.functionality.categories.rest.dto.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriesDtoMapper {

    CategoryDto toCategoryDto(Category category);
}
