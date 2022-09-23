package com.swprogramming.dothemeetingnow.dto.route;

import com.swprogramming.dothemeetingnow.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponseDto {

    private String line_start;

    private String start;

    private String line_end;

    private String end;

    private Long min;

    private Long second;

    public static RouteResponseDto toDto(Route route){
        RouteResponseDto routeResponseDto= RouteResponseDto.builder()
                .line_start(route.getStart().getLine().getName())
                .start(route.getStart().getName())
                .end(route.getEnd().getName())
                .line_end(route.getEnd().getLine().getName())
                .min(route.getTime()/60)
                .second(route.getTime()%60)
                .build();
        return routeResponseDto;
    }
}
