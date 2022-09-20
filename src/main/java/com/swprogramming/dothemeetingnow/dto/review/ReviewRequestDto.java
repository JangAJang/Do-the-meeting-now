package com.swprogramming.dothemeetingnow.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "점수를 입력해주세요")
    private Integer rate;

    @NotNull(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요")
    private String category_name;

}
