package com.swprogramming.dothemeetingnow.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRouteRequestDto {

    @NotNull(message = "출발역을 입력하세요")
    private String start_station;

    @NotNull(message = "출발역의 호선을 입력하세요")
    private String start_line;

    @NotNull(message = "도착역을 입력하세요")
    private String end_station;

    @NotNull(message = "도착역의 호선을 입력하세요")
    private String end_line;
}
