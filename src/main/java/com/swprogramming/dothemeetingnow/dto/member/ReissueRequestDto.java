package com.swprogramming.dothemeetingnow.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReissueRequestDto {
    private String accessToken;
    private String refreshToken;
}
