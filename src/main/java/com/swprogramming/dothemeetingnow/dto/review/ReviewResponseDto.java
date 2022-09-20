package com.swprogramming.dothemeetingnow.dto.review;

import com.swprogramming.dothemeetingnow.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

    private String nickname;

    private String category_name;

    private Integer rate;

    private String content;

    public static ReviewResponseDto toDto(Review review){
        ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                .category_name(review.getCategory().getName())
                .rate(review.getRate())
                .nickname(review.getMember().getNickname())
                .content(review.getContent())
                .build();
        return reviewResponseDto;
    }
}
