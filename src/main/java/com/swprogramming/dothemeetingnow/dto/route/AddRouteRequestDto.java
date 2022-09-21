package com.swprogramming.dothemeetingnow.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRouteRequestDto {

    @NotNull(message = "지하철 호선을 입력하세요")
    private String line_name;

    @NotNull(message = "출발 역을 입력하세요")
    private String start;

    @NotNull(message = "도착 역을 입력하세요")
    private String end;

    @NotNull(message = "이동 시간을 입력하세요(분)")
    private Long minute;

    @NotNull(message = "이동 시간을 입력하세요(초)")
    private Long second;
}
