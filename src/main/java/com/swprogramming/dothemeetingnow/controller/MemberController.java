package com.swprogramming.dothemeetingnow.controller;

import com.swprogramming.dothemeetingnow.dto.member.LoginRequestDto;
import com.swprogramming.dothemeetingnow.dto.member.RegisterRequestDto;
import com.swprogramming.dothemeetingnow.dto.member.ReissueRequestDto;
import com.swprogramming.dothemeetingnow.response.Response;
import com.swprogramming.dothemeetingnow.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원가입", notes = "회원가입 페이지 입니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join")
    public Response register(@RequestBody RegisterRequestDto registerRequestDto){
        return Response.success(memberService.register(registerRequestDto));
    }

    @ApiOperation(value = "로그인", notes = "로그인 페이지 입니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sign-in")
    public Response signin(@RequestBody LoginRequestDto loginRequestDto){
        return Response.success(memberService.signIn(loginRequestDto));
    }

    @ApiOperation(value = "토큰 재발행", notes = "리프레쉬 토큰을 재발행합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reissue")
    public Response reissue(@RequestHeader ReissueRequestDto reissueRequestDto){
        return Response.success(memberService.reissue(reissueRequestDto));
    }

    @ApiOperation(value = "관리자 페이지", notes = "관지라 페이지입니다. ")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/admin")
    public Response admin(){
        return Response.success();
    }

    @ApiOperation(value = "매니저 페이지", notes = "매니저 페이지 입니다.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/manager")
    public Response manager(){
        return Response.success();
    }
}
