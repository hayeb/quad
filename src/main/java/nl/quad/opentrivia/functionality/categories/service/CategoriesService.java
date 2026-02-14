package nl.quad.opentrivia.functionality.categories.service;

import lombok.RequiredArgsConstructor;
import nl.quad.opentrivia.client.opentrivia.OpenTriviaClientService;
import nl.quad.opentrivia.functionality.categories.mapper.CategoriesMapper;
import nl.quad.opentrivia.functionality.categories.model.Category;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final OpenTriviaClientService openTriviaClientService;

    private final CategoriesMapper categoriesMapper;

    public List<Category> getCategories() {
        return openTriviaClientService.getCategories().categories()
            .stream()
            .map(categoriesMapper::toCategory)
            .sorted(Comparator.comparing(Category::name))
            .toList();
    }
}
