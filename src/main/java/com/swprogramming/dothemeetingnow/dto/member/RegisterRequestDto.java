package com.swprogramming.dothemeetingnow.dto.member;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RegisterRequestDto {

    @NotNull(message = "아이디를 입력하세요")
    private String username;

    @NotNull(message = "비밀번호를 입력하세요")
    private String password;

    @NotNull(message = "비밀번호를 다시 입력하세요")
    private String password_check;

    @NotNull(message = "이메일을 입력하세요")
    private String email;

    @NotNull(message = "전화번호를 입력하세요")
    private String phone;

    @NotNull(message = "닉네임을 입력하세요")
    private String nickname;

    @NotNull(message = "사는 도시를 입력하세요")
    private String city;

    @NotNull(message = "사는 지역명을 입력하세요")
    private String street;




}
