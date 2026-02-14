package nl.quad.opentrivia.functionality.categories.rest;

import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.functionality.categories.rest.dto.CategoryDto;
import nl.quad.opentrivia.functionality.categories.rest.mapper.CategoriesDtoMapper;
import nl.quad.opentrivia.functionality.categories.service.CategoriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    private final CategoriesDtoMapper categoriesDtoMapper;

    @GetMapping
    public List<CategoryDto> getCategories() {
        return this.categoriesService.getCategories()
            .stream()
            .map(categoriesDtoMapper::toCategoryDto)
            .toList();
    }
}
