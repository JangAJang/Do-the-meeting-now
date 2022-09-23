package com.swprogramming.dothemeetingnow.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRouteRequestDto {

    @NotNull(message = "출발 역을 입력하세요, 처음 정류장은 -1로 표현")
    private String start;

    @NotNull(message = "출발 역의 호선을 입력하세요")
    private String line_start;

    @NotNull(message = "도착 역을 입력하세요, 마지막 정류장은 -1로 표현")
    private String end;

    @NotNull(message = "도착 역의 호선을 입력하세요")
    private String line_end;

    @NotNull(message = "이동 시간을 입력하세요(분), 처음이나 마지막 정류장은 0")
    private Long minute;

    @NotNull(message = "이동 시간을 입력하세요(초), 처음이나 마지막 정류장은 0")
    private Long second;
}
