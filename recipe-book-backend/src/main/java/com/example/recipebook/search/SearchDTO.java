package com.example.recipebook.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {
    private List<SearchCriteria> searchCriteriaList;
    private String dataOption;
    private String orderByField;
    private Boolean orderByAscending = true;
}
