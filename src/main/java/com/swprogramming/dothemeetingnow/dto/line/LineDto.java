package com.swprogramming.dothemeetingnow.dto.line;

import com.swprogramming.dothemeetingnow.entity.Line;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineDto {

    @NotNull(message = "호선을 입력해주세요")
    private String line_name;

    public static LineDto toDto(Line line){
        LineDto lineDto = LineDto.builder()
                .line_name(line.getName())
                .build();
        return lineDto;
    }
}
