package com.swprogramming.dothemeetingnow.exception;

import com.swprogramming.dothemeetingnow.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        return Response.failure(500, e.getMessage().toString());
    }

    //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(MemberNotAuthorized.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotAuthorizedException() {
        return Response.failure(404, "권한이 없습니다 ");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "유저를 찾을 수 없습니다 ");
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response usernameAlreadyExistException(){
        return Response.failure(404, "중복되는 아이디입니다. ");
    }

    @ExceptionHandler(PhoneAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response phoneAlreadyExistException(){
        return Response.failure(404, "중복된 전화번호입니다. ");
    }

    @ExceptionHandler(NicknameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response nicknameAlreadyExistException(){
        return Response.failure(404, "중복된 닉네임입니다.");
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response emailAlreadyExistException(){
        return Response.failure(404, "중복된 이메일입니다.");
    }

    @ExceptionHandler(PasswordNotEqualException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response passwordNotEqualException(){
        return Response.failure(404, "비밀번호가 일치하지 않습니다. ");
    }

    @ExceptionHandler(FailToLoginException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response loginFailException(){
        return Response.failure(404, "로그인에 실패했습니다. ");
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reviewNotFoundException(){
        return Response.failure(404, "리뷰를 찾을 수 없습니다. ");
    }

    @ExceptionHandler(LineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response lineNotFoundException(){
        return Response.failure(404, "해당 호선을 찾을 수 없습니다. ");
    }

    @ExceptionHandler(StationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response stationNotFoundException(){
        return Response.failure(404, "요청하신 역을 찾을 수 없습니다. ");
    }

    @ExceptionHandler(RouteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response routeNotFoundException(){
        return Response.failure(404, "해당 노선을 찾을 수 없습니다. ");
    }

    @ExceptionHandler(LineNotSameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response lineNotSameException(){
        return Response.failure(404, "출발역과 도착역의 호선 정보가 일치하지 않습니다. ");
    }

    @ExceptionHandler(RouteEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response routeEmptyException(){
        return Response.failure(404, "등록된 경로가 없습니다. ");
    }

    @ExceptionHandler(LineSameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response lineSameException(){
        return Response.failure(404, "같은 호선으로 환승할 수 없습니다. ");
    }

    @ExceptionHandler(TransferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response transferNotFoundException(){
        return Response.failure(404, "해당 환승정보를 찾을 수 없습니다. ");
    }

    @ExceptionHandler(StationSameException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response stationSameException(){
        return Response.failure(404, "동일한 역으로는 경로를 구할 수 없습니다. ");
    }
}
