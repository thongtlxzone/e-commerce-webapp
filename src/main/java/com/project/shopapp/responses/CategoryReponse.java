package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.CategoryEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryReponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("category")
    private CategoryDTO category;
}
