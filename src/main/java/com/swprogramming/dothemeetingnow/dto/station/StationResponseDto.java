package com.swprogramming.dothemeetingnow.dto.station;

import com.swprogramming.dothemeetingnow.entity.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationResponseDto {

    String station_name;

    String line_name;

    public static StationResponseDto toDto(Station station){
        StationResponseDto stationResponseDto = StationResponseDto
                .builder()
                .station_name(station.getName())
                .line_name(station.getLine().getName())
                .build();
        return stationResponseDto;
    }

}
