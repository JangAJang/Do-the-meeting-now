package com.swprogramming.dothemeetingnow.service;
import com.swprogramming.dothemeetingnow.config.jwt.TokenProvider;
import com.swprogramming.dothemeetingnow.dto.*;
import com.swprogramming.dothemeetingnow.dto.member.*;
import com.swprogramming.dothemeetingnow.entity.Authority;
import com.swprogramming.dothemeetingnow.entity.Member;
import com.swprogramming.dothemeetingnow.entity.RefreshToken;
import com.swprogramming.dothemeetingnow.exception.*;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto){
        validateRegisterRequest(registerRequestDto);
        Member member = new Member();
        member.setUsername(registerRequestDto.getUsername());
        member.setNickname(registerRequestDto.getNickname());
        member.setPassword(bCryptPasswordEncoder.encode(registerRequestDto.getPassword()));
        member.setAuthority(Authority.ROLE_USER);
        member.setCity(registerRequestDto.getCity());
        member.setStreet(registerRequestDto.getStreet());
        member.setEmail(registerRequestDto.getEmail());
        member.setPhone(registerRequestDto.getPhone());
        memberRepository.save(member);
        return RegisterResponseDto.toDto(member);
    }


    @Transactional
    public TokenResponseDto signIn(LoginRequestDto loginRequestDto){
        validateSignInRequest(loginRequestDto);
        TokenDto tokenDto = getTokenDto(loginRequestDto);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    @Transactional
    public TokenResponseDto reissue(ReissueRequestDto req) {
        validateRefreshToken(req);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(getAuthentication(req).getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        validateTokenInfo(refreshToken,req);
        TokenDto tokenDto = tokenProvider.generateTokenDto(getAuthentication(req));
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private void validateRegisterRequest(RegisterRequestDto registerRequestDto){
        validateNickname(registerRequestDto);
        validateEmail(registerRequestDto);
        validatePassword(registerRequestDto);
        validatePhone(registerRequestDto);
        validateUsername(registerRequestDto);
    }

    private void validateUsername(RegisterRequestDto registerRequestDto){
        if(memberRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new UsernameAlreadyExistException();
        }
    }

    private void validateNickname(RegisterRequestDto registerRequestDto){
        if(memberRepository.findByNickname(registerRequestDto.getNickname()).isPresent()){
        throw new NicknameAlreadyExistException();
        }
    }

    private void validateEmail(RegisterRequestDto registerRequestDto){
        if(memberRepository.findByEmail(registerRequestDto.getEmail()).isPresent()){
        throw new EmailAlreadyExistException();
        }
    }

    private void validatePhone(RegisterRequestDto registerRequestDto){
        if(memberRepository.findByPhone(registerRequestDto.getPhone()).isPresent()){
            throw new PhoneAlreadyExistException();
        }
    }

    private void validatePassword(RegisterRequestDto registerRequestDto){
        if(!registerRequestDto.getPassword().equals(registerRequestDto.getPassword_check())){
            throw new PasswordNotEqualException();
        }
    }

    private Member validateSignInRequest(LoginRequestDto loginRequestDto){
        Member member = validateMemberExistence(loginRequestDto);
        validatePasswordCorrection(loginRequestDto, member);
        return member;
    }

    private Member validateMemberExistence(LoginRequestDto loginRequestDto){
        return memberRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(FailToLoginException::new);
    }

    private void validatePasswordCorrection(LoginRequestDto loginRequestDto, Member member){
        if(bCryptPasswordEncoder.encode(loginRequestDto.getPassword()).equals(member.getPassword())){
            throw new PasswordNotEqualException();
        }
    }

    private void validateRefreshToken(ReissueRequestDto req){
        if (!tokenProvider.validateToken(req.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private TokenDto getTokenDto(LoginRequestDto loginRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    private Authentication getAuthentication(ReissueRequestDto req){
        return tokenProvider.getAuthentication(req.getAccessToken());
    }

    private void validateTokenInfo(RefreshToken refreshToken, ReissueRequestDto req){
        if (!refreshToken.getValue().equals(req.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }}
}
