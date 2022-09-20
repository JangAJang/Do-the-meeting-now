package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.station.AddStationRequestDto;
import com.swprogramming.dothemeetingnow.dto.station.StationResponseDto;
import com.swprogramming.dothemeetingnow.entity.Authority;
import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Member;
import com.swprogramming.dothemeetingnow.entity.Station;
import com.swprogramming.dothemeetingnow.exception.LineNotFoundException;
import com.swprogramming.dothemeetingnow.exception.MemberNotAuthorized;
import com.swprogramming.dothemeetingnow.exception.MemberNotFoundException;
import com.swprogramming.dothemeetingnow.exception.StationNotFoundException;
import com.swprogramming.dothemeetingnow.repository.LineRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    //역 추가하기
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

    //모든 역 조회하기
    @Transactional(readOnly = true)
    public List<StationResponseDto> getAllStations(){
        return getStation();
    }

    //이름으로 역 찾기
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

    //호선으로 역 찾기
    @Transactional(readOnly = true)
    public List<StationResponseDto> searchStationByLine(String line_name) {
        Line line = lineRepository.findByName(line_name).orElseThrow(LineNotFoundException::new);
        List<Station> stations = stationRepository.findAllByLine(line);
        if(stations.isEmpty()){
            throw new StationNotFoundException();
        }
        List<StationResponseDto> stationResponseDtos = new LinkedList<>();
        for(Station station: stations){
            stationResponseDtos.add(StationResponseDto.toDto(station));
        }
        return stationResponseDtos;
    }

    @Transactional
    public StationResponseDto updateStation(AddStationRequestDto addStationRequestDto, Long id){
        Station station = stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
        Line line = lineRepository.findByName(addStationRequestDto.getLine_name()).orElseThrow(LineNotFoundException::new);
        validate();
        station.setLine(line);
        station.setName(addStationRequestDto.getName());
        stationRepository.save(station);
        return StationResponseDto.toDto(station);
    }

    @Transactional
    public String deleteStation(Long id){
        Station station = stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
        validate();
        stationRepository.delete(station);
        return "삭제 성공";
    }

    //모든 역 조회하기
    private List<StationResponseDto> getStation(){
        List<Station> stations = stationRepository.findAll();
        if(stations.isEmpty()) throw new StationNotFoundException();
        List<StationResponseDto> stationResponseDtos = new LinkedList<>();
        for(Station station: stations){
            stationResponseDtos.add(StationResponseDto.toDto(station));
        }
        return stationResponseDtos;
    }

    //회원 권한 확인 : 관리자나 매니저가 아니면 삭제한다.
    private void validate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }
}
