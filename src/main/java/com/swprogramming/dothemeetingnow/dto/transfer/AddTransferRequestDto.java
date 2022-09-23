package com.swprogramming.dothemeetingnow.dto.transfer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddTransferRequestDto {

    @NotNull(message = "호선을 입력하세요")
    private String from_line;

    @NotNull(message = "호선을 입력하세요")
    private String to_line;

    @NotNull(message = "역 이름을 입력하세요")
    private String station_name;

    @NotNull(message = "시간(분)을 입력하세요")
    private Long min;

    @NotNull(message = "시간(초)를 입력하세요")
    private Long sec;

}
