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

import java.util.ArrayList;
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
        return StationResponseDto.toDto(setStation(addStationRequestDto, new Station()));
    }

    //모든 역 조회하기
    @Transactional(readOnly = true)
    public List<StationResponseDto> searchAllStation(){
        return getStationListToDto(getAllStation());
    }

    //이름으로 역 찾기
    @Transactional(readOnly = true)
    public List<StationResponseDto> searchStationByName(String name){
        List<Station> stations = getAllStation();
        stations.removeIf(station -> !station.getName().contains(name));
        return getStationListToDto(stations);
    }

    //호선으로 역 찾기
    @Transactional(readOnly = true)
    public List<StationResponseDto> searchStationByLine(String line_name) {
        Line line = lineRepository.findByName(line_name).orElseThrow(LineNotFoundException::new);
        List<Station> stations = getAllByLine(line);
        return getStationListToDto(stations);
    }

    private List<Station> getAllByLine(Line line){
        List<Station> stations =  stationRepository.findAllByLine(line);
        if(stations.isEmpty()){
            throw new StationNotFoundException();
        }
        return stations;
    }

    @Transactional
    public StationResponseDto updateStation(AddStationRequestDto addStationRequestDto, Long id){
        validateAuthority();
        Station station = getStationById(id);
        return StationResponseDto.toDto(setStation(addStationRequestDto, station));
    }

    @Transactional
    public String deleteStation(Long id){
        validateAuthority();
        stationRepository.delete(getStationById(id));
        return "삭제 성공";
    }

    private Station setStation(AddStationRequestDto addStationRequestDto, Station station){
        station.setLine(getLineByName(addStationRequestDto.getLine_name()));
        station.setName(addStationRequestDto.getName());
        stationRepository.save(station);
        return station;
    }

    private Line getLineByName(String name){
        return lineRepository.findByName(name).orElseThrow(LineNotFoundException::new);
    }

    private Station getStationById(Long id){
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }

    //모든 역 조회하기
    private List<Station> getAllStation(){
        List<Station> stations = stationRepository.findAll();
        if(stations.isEmpty()) throw new StationNotFoundException();
        return stations;
    }

    private List<StationResponseDto> getStationListToDto(List<Station> stations){
        List<StationResponseDto> list = new ArrayList<>();
        for(Station station : stations){
            list.add(StationResponseDto.toDto(station));
        }
        return list;
    }

    //회원 권한 확인 : 관리자나 매니저가 아니면 삭제한다.
    private void validateAuthority(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }
}
