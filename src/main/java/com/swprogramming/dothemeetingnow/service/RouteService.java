package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.Graph;
import com.swprogramming.dothemeetingnow.dto.route.*;
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
    public RouteResponseDto getShortestRoute(SearchRouteRequestDto searchRouteRequestDto){
        Line from_line = lineRepository.findByName(searchRouteRequestDto.getStart_line()).orElseThrow(LineNotFoundException::new);
        Line to_line = lineRepository.findByName(searchRouteRequestDto.getEnd_line()).orElseThrow(LineNotFoundException::new);
        Station start = stationRepository.findByNameAndLine(searchRouteRequestDto.getStart_station(), from_line).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByNameAndLine(searchRouteRequestDto.getEnd_station(), to_line).orElseThrow(StationNotFoundException::new);
        Route route;
        if(from_line.equals(to_line)){
            route = routeRepository.findByStartAndEnd(start, end).orElse(getShortestRouteInSameLine(searchRouteRequestDto, start, end));
        }
        else {
            route = routeRepository.findByStartAndEnd(start, end).orElse(getShortestRouteInDifferentLine(searchRouteRequestDto, start, end));
        }
        return RouteResponseDto.toDto(route);
    }

    private Route getShortestRouteInSameLine(SearchRouteRequestDto searchRouteRequestDto, Station start, Station end){
        Line line = start.getLine();
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
        Route route = new Route();
        route.setLine(line);
        route.setStart(start);
        route.setEnd(end);
        route.setTime(time);
        route.setRouteStatus(RouteStatus.DIRECT);
        routeRepository.save(route);
        return route;
    }

    private Route getShortestRouteInDifferentLine(SearchRouteRequestDto searchRouteRequestDto, Station from, Station to){
        Line from_line = from.getLine();
        Line to_line = to.getLine();
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
        Route route = new Route();
        route.setStart(from);
        route.setEnd(to);
        route.setTime(time);
        route.setLine(null);
        route.setRouteStatus(RouteStatus.TRANSFER);
        routeRepository.save(route);
        return route;
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
