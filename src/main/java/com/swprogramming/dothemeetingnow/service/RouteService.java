package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.Graph;
import com.swprogramming.dothemeetingnow.dto.route.AddRouteRequestDto;
import com.swprogramming.dothemeetingnow.dto.route.RouteResponseDto;
import com.swprogramming.dothemeetingnow.dto.route.SearchRouteRequestDto;
import com.swprogramming.dothemeetingnow.entity.*;
import com.swprogramming.dothemeetingnow.exception.*;
import com.swprogramming.dothemeetingnow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RouteResponseDto addRoute(AddRouteRequestDto addRouteRequestDto){
        validateMember();
        Route route = new Route();
        setRoute(addRouteRequestDto, route);
        return RouteResponseDto.toDto(route);
    }

    @Transactional
    public RouteResponseDto updateRoute(AddRouteRequestDto addRouteRequestDto, Long id){
        validateMember();
        Route route = getRoute(id);
        setRoute(addRouteRequestDto, route);
        return RouteResponseDto.toDto(route);
    }

    @Transactional
    public String deleteRoute(Long id){
        validateMember();
        Route route = getRoute(id);
        routeRepository.delete(route);
        return "삭제 완료";
    }



    @Transactional(readOnly = true)
    public RouteResponseDto getRouteInfo(Long id){
        Route route = getRoute(id);
        return RouteResponseDto.toDto(route);
    }

    @Transactional(readOnly = true)
    public RouteResponseDto getShortestRouteInSameLine(SearchRouteRequestDto searchRouteRequestDto){
        Line line = lineRepository.findByName(searchRouteRequestDto.getStart_line()).orElseThrow(LineNotFoundException::new);
        Station start = stationRepository.findByNameAndLine(searchRouteRequestDto.getStart_station(), line).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByNameAndLine(searchRouteRequestDto.getEnd_station(), line).orElseThrow(StationNotFoundException::new);
        if(start.equals(end)) throw new StationSameException();
        List<Route> routes = routeRepository.findAllByLine(line);
        if(routes.isEmpty()) throw new RouteEmptyException();
        List<Station> stations = stationRepository.findAllByLine(line);
        Graph graph = new Graph(stations.size());
        for(int i=0; i<routes.size(); i++){
            int a = stations.indexOf(routes.get(i).getStart());
            int b = stations.indexOf(routes.get(i).getEnd());
            Long weight = routes.get(i).getTime();
            graph.input(a, b, weight);
        }
        Long[] distance = graph.dijkstra(stations.indexOf(start));
        Long time = distance[stations.indexOf(end)];
        RouteResponseDto routeResponseDto = RouteResponseDto.builder()
                .line_start(line.getName())
                .line_end(line.getName())
                .start(start.getName())
                .end(end.getName())
                .min(time/60)
                .second(time%60)
                .build();
        return routeResponseDto;
    }

    //수정필요
    @Transactional(readOnly = true)
    public RouteResponseDto getShortestRouteInDifferentLine(SearchRouteRequestDto searchRouteRequestDto){
        Line from_line = lineRepository.findByName(searchRouteRequestDto.getStart_line()).orElseThrow(LineNotFoundException::new);
        Line to_line = lineRepository.findByName(searchRouteRequestDto.getEnd_line()).orElseThrow(LineNotFoundException::new);
        Station from = stationRepository.findByNameAndLine(searchRouteRequestDto.getStart_station(), from_line).orElseThrow(StationNotFoundException::new);
        Station to = stationRepository.findByNameAndLine(searchRouteRequestDto.getEnd_station(), to_line).orElseThrow(StationNotFoundException::new);
        List<Route> routes = routeRepository.findAll();
        if(routes.isEmpty()) throw new RouteEmptyException();
        List<Station> stations = stationRepository.findAll();
        Graph graph = new Graph(stations.size());
        for(int i=0; i<routes.size(); i++){
            int a = stations.indexOf(routes.get(i).getStart());
            int b = stations.indexOf(routes.get(i).getEnd());
            Long weight = routes.get(i).getTime();
            graph.input(a, b, weight);
        }
        Long[] distance = graph.dijkstra(stations.indexOf(from));
        Long time = distance[stations.indexOf(to)];
        RouteResponseDto routeResponseDto = RouteResponseDto.builder()
                .line_start(from.getLine().getName())
                .line_end(to.getLine().getName())
                .start(from.getName())
                .end(to.getName())
                .min(time/60)
                .second(time%60)
                .build();
        return routeResponseDto;
    }

    private Route getRoute(Long id){
        return routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
    }

    private void validateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }

    private void setRoute(AddRouteRequestDto addRouteRequestDto, Route route){
        Line from = lineRepository.findByName(addRouteRequestDto.getLine_start()).orElseThrow(LineNotFoundException::new);
        Line to = lineRepository.findByName(addRouteRequestDto.getLine_end()).orElseThrow(LineNotFoundException::new);
        Station start = stationRepository.findByNameAndLine(addRouteRequestDto.getStart(), from).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByNameAndLine(addRouteRequestDto.getEnd(), to).orElseThrow(StationNotFoundException::new);
        if(from.equals(to) && start.equals(end)){
            throw new StationSameException();
        }
        if(from.equals(to)){
            route.setLine(from);
            route.setRouteStatus(RouteStatus.DIRECT);
        }
        else{
            route.setLine(null);
            route.setRouteStatus(RouteStatus.TRANSFER);
        }
        if(addRouteRequestDto.getStart().equals("-1")) route.setStart(null);
        else route.setStart(start);
        if(addRouteRequestDto.getEnd().equals("-1")) route.setEnd(null);
        else route.setEnd(end);
        route.setTime(addRouteRequestDto.getMinute()*60 + addRouteRequestDto.getSecond());
        routeRepository.save(route);
    }
}
