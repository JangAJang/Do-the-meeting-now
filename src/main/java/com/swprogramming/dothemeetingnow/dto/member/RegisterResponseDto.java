package com.swprogramming.dothemeetingnow.dto.member;

import com.swprogramming.dothemeetingnow.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponseDto {

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String city;

    private String street;


    public static RegisterResponseDto toDto(Member member){
        RegisterResponseDto registerResponseDto = RegisterResponseDto.builder()
                .city(member.getCity())
                .street(member.getStreet())
                .email(member.getEmail())
                .phone(member.getPhone())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .build();
        return registerResponseDto;
    }
}
