package com.example.recipebook.recipe.specification;

import com.example.recipebook.category.Category;
import com.example.recipebook.diet.Diet;
import com.example.recipebook.recipe.Recipe;
import com.example.recipebook.recipe.SkillLevel;
import com.example.recipebook.search.SearchCriteria;
import com.example.recipebook.search.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;

public class RecipeSpecification implements Specification<Recipe> {
    private final SearchCriteria searchCriteria;

    public RecipeSpecification(final SearchCriteria
                                       searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strToSearch = searchCriteria.getValue()
                .toString().toLowerCase();

        return switch (Objects.requireNonNull(
                SearchOperation.getSimpleOperation(
                        searchCriteria.getOperation()))) {
            case CONTAINS -> cb.like(cb.lower(root
                    .get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");
            case DOES_NOT_CONTAIN -> cb.notLike(cb.lower(root
                    .get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");
            case EQUAL -> searchCriteria.getFilterKey().equals("category") ?
                    cb.equal(recipeCategoryJoin(root).
                                    <String>get("name"),
                            searchCriteria.getValue().toString()) :
                    searchCriteria.getFilterKey().equals("diet") ?
                            cb.equal(recipeDietJoin(root).
                                            <String>get("name"),
                                    searchCriteria.getValue().toString()) :
                            searchCriteria.getFilterKey().equals("skillLevel") ?
                                    cb.equal(cb.lower(root
                                            .get(searchCriteria.getFilterKey())), SkillLevel.valueOf(strToSearch.toUpperCase())) :
                                    cb.equal(cb.lower(root
                                            .get(searchCriteria.getFilterKey())), strToSearch);
            case NOT_EQUAL -> cb.notEqual(cb.lower(root
                    .get(searchCriteria.getFilterKey())), strToSearch);
            case NULL -> cb.isNull(root.get(searchCriteria.getFilterKey()));
            case NOT_NULL -> cb.isNotNull(root.get(searchCriteria.getFilterKey()));
            case GREATER_THAN -> cb.greaterThan(root.get(searchCriteria.getFilterKey()),
                    searchCriteria.getValue().toString());
            case GREATER_THAN_EQUAL -> cb.greaterThanOrEqualTo(root.get(searchCriteria.getFilterKey()),
                    searchCriteria.getValue().toString());
            case LESS_THAN -> cb.lessThan(root.get(searchCriteria.getFilterKey()),
                    searchCriteria.getValue().toString());
            case LESS_THAN_EQUAL -> cb.lessThanOrEqualTo(root.get(searchCriteria.getFilterKey()),
                    searchCriteria.getValue().toString());
            case IS_TRUE -> cb.isTrue(root.get(searchCriteria.getFilterKey()));
            case IS_FALSE -> cb.isFalse(root.get(searchCriteria.getFilterKey()));
            default -> null;
        };
    }

    private Join<Recipe, Category> recipeCategoryJoin(Root<Recipe> root) {
        return root.join("category");
    }

    private Join<Recipe, Diet> recipeDietJoin(Root<Recipe> root) {
        return root.join("diet");
    }
}
