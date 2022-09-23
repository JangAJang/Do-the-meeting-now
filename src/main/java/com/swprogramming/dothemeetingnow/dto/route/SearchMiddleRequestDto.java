package com.swprogramming.dothemeetingnow.dto.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchMiddleRequestDto {

    @NotNull(message = "호선의 이름을 입력해주세요")
    private String line_name;

    @NotNull(message = "역의 이름을 입력해주세요")
    private String station_name;
}
