package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.Graph;
import com.swprogramming.dothemeetingnow.dto.route.AddRouteRequestDto;
import com.swprogramming.dothemeetingnow.dto.route.RouteResponseDto;
import com.swprogramming.dothemeetingnow.dto.route.SearchRouteRequestDto;
import com.swprogramming.dothemeetingnow.entity.*;
import com.swprogramming.dothemeetingnow.exception.*;
import com.swprogramming.dothemeetingnow.repository.LineRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.RouteRepository;
import com.swprogramming.dothemeetingnow.repository.StationRepository;
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
        Route reverse = new Route();
        Line line = lineRepository.findByName(addRouteRequestDto.getLine_name()).orElseThrow(LineNotFoundException::new);
        Station start = stationRepository.findByNameAndLine(addRouteRequestDto.getStart(), line).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByNameAndLine(addRouteRequestDto.getEnd(), line).orElseThrow(StationNotFoundException::new);
        route.setLine(line);
        reverse.setLine(line);
        if(addRouteRequestDto.getStart().equals("-1")) {
            route.setStart(null);
            reverse.setEnd(null);
        }
        else {
            route.setStart(start);
            reverse.setEnd(start);
        }
        if(addRouteRequestDto.getEnd().equals("-1")) {
            route.setEnd(null);
            reverse.setStart(null);
        }
        else {
            route.setEnd(end);
            reverse.setStart(end);
        }
        route.setTime(addRouteRequestDto.getMinute()*60 + addRouteRequestDto.getSecond());
        reverse.setTime(addRouteRequestDto.getMinute()*60 + addRouteRequestDto.getSecond());
        routeRepository.save(route);
        routeRepository.save(reverse);
        return RouteResponseDto.toDto(route);
    }



    @Transactional(readOnly = true)
    public RouteResponseDto getRouteInfo(Long id){
        Route route = routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        return RouteResponseDto.toDto(route);
    }

    @Transactional(readOnly = true)
    public RouteResponseDto getShortestRouteInSameLine(SearchRouteRequestDto searchRouteRequestDto){
        Line line = lineRepository.findByName(searchRouteRequestDto.getStart_line()).orElseThrow(LineNotFoundException::new);
        Station start = stationRepository.findByNameAndLine(searchRouteRequestDto.getStart_station(), line).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByNameAndLine(searchRouteRequestDto.getEnd_station(), line).orElseThrow(StationNotFoundException::new);
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
                .line_name(line.getName())
                .start(start.getName())
                .end(end.getName())
                .min(time/60)
                .second(time%60)
                .build();
        return routeResponseDto;
    }

    @Transactional(readOnly = true)
    public List<RouteResponseDto> getShortestRouteInDifferentLine(SearchRouteRequestDto searchRouteRequestDto){
        
    }

    private Route getRoute(Long id){
        return routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
    }

    private void validateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }
}
