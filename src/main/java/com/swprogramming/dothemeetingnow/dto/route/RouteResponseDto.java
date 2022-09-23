package com.swprogramming.dothemeetingnow.dto.route;

import com.swprogramming.dothemeetingnow.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponseDto {

    private String line_name;

    private String start;

    private String end;

    private Long min;

    private Long second;

    public static RouteResponseDto toDto(Route route){
        RouteResponseDto routeResponseDto= RouteResponseDto.builder()
                .line_name(route.getLine().getName())
                .start(route.getStart().getName())
                .end(route.getEnd().getName())
                .min(route.getTime()/60)
                .second(route.getTime()%60)
                .build();
        return routeResponseDto;
    }
}
