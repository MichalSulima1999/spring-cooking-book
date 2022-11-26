package com.example.recipebook.recipe.specification;

import com.example.recipebook.recipe.Recipe;
import com.example.recipebook.search.SearchCriteria;
import com.example.recipebook.search.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecificationBuilder {
    private final List<SearchCriteria> params;

    public RecipeSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public final RecipeSpecificationBuilder with(String key,
                                                 String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final RecipeSpecificationBuilder with(SearchCriteria
                                                         searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification<Recipe> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification<Recipe> result =
                new RecipeSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++) {
            SearchCriteria criteria = params.get(idx);
            result = SearchOperation.getDataOption(criteria
                    .getDataOption()) == SearchOperation.ALL
                    ? Specification.where(result).and(new
                    RecipeSpecification(criteria))
                    : Specification.where(result).or(
                    new RecipeSpecification(criteria));
        }
        return result;
    }
}