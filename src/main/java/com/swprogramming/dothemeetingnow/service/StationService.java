package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.station.AddStationRequestDto;
import com.swprogramming.dothemeetingnow.dto.station.StationResponseDto;
import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Station;
import com.swprogramming.dothemeetingnow.exception.LineNotFoundException;
import com.swprogramming.dothemeetingnow.repository.LineRepository;
import com.swprogramming.dothemeetingnow.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public StationResponseDto addStation(AddStationRequestDto addStationRequestDto){
        Line line = lineRepository.findByName(addStationRequestDto.getName()).orElseThrow();
        Station station = Station.builder()
                .line(line)
                .name(addStationRequestDto.getName())
                .build();
        stationRepository.save(station);
        return StationResponseDto.toDto(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponseDto> getAllStations(){
        return getStation();
    }

    @Transactional(readOnly = true)
    public List<StationResponseDto> searchStationByName(String name){
        List<StationResponseDto> stationResponseDtos = getStation();
        for(StationResponseDto stationResponseDto: stationResponseDtos){
            if(!stationResponseDto.getStation_name().contains(name)){
                stationResponseDtos.remove(stationResponseDto);
            }
        }
        return stationResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<StationResponseDto> searchStationByLine(String line_name) {
        Line line = lineRepository.findByName(line_name).orElseThrow(LineNotFoundException::new);

    }



    private List<StationResponseDto> getStation(){
        List<Station> stations = stationRepository.findAll();
        List<StationResponseDto> stationResponseDtos = new LinkedList<>();
        for(Station station: stations){
            stationResponseDtos.add(StationResponseDto.toDto(station));
        }
        return stationResponseDtos;
    }
}
