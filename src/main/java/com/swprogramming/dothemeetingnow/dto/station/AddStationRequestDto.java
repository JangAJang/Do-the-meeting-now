package com.swprogramming.dothemeetingnow.dto.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStationRequestDto {

    @NotNull(message = "역 이름을 입력해주세요")
    private String name;

    @NotNull(message = "호선을 입력해주세요")
    private String line_name;
}
