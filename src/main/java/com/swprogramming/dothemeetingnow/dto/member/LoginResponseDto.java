package com.swprogramming.dothemeetingnow.dto.member;

import com.swprogramming.dothemeetingnow.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String nickname;

    public static LoginResponseDto toDto(Member member){
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .nickname(member.getNickname())
                .build();
        return loginResponseDto;
    }
}
