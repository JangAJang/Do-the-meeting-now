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
        Line from_line = getLineByName(searchRouteRequestDto.getStart_line());
        Line to_line = getLineByName(searchRouteRequestDto.getEnd_line());
        Station start = getStationByNameAndLine(searchRouteRequestDto.getStart_station(), from_line);
        Station end = getStationByNameAndLine(searchRouteRequestDto.getEnd_station(), to_line);
        if(from_line.equals(to_line)){
             return RouteResponseDto.toDto(routeRepository.findByStartAndEnd(start, end).orElse(getShortestRouteInSameLine(start, end)));
        }
        return RouteResponseDto.toDto(routeRepository.findByStartAndEnd(start, end).orElse(getShortestRouteInDifferentLine(start, end)));
    }

    private Line getLineByName(String start){
        return lineRepository.findByName(start).orElseThrow(LineNotFoundException::new);
    }

    private Station getStationByNameAndLine(String name, Line line){
        return stationRepository.findByNameAndLine(name, line).orElseThrow(StationNotFoundException::new);
    }

    private void validateRoutes(List<Route> routes){
        if(routes.isEmpty()) throw new RouteEmptyException();
    }

    private void validateStations(List<Station>stations){
        if(stations.isEmpty()) throw new StationNotFoundException();
    }

    private Route getShortestRouteInSameLine(Station start, Station end){
        Line line = start.getLine();
        List<Route> routes = routeRepository.findAllByLine(line);
        validateRoutes(routes);
        List<Station> stations = stationRepository.findAllByLine(line);
        validateStations(stations);
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

    private Route getShortestRouteInDifferentLine(Station from, Station to){
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

    public List<RouteResponseDto> searchMiddleRoute(SearchMiddleRequestListDto searchMiddleRequestListDto){
        List<SearchMiddleRequestDto> list = searchMiddleRequestListDto.getSearchMiddleRequestDtoList();
        List<Station> starts = new LinkedList<>();
        for(SearchMiddleRequestDto searchMiddleRequestDto: list){
            Line line = lineRepository.findByName(searchMiddleRequestDto.getLine_name()).orElseThrow(LineNotFoundException::new);
            starts.add(stationRepository.findByNameAndLine(searchMiddleRequestDto.getStation_name(), line).orElseThrow(StationNotFoundException::new));
        }
        List<Route> routes = routeRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        Graph graph = new Graph(stations.size());
        for(int i=0; i<routes.size(); i++){
            int a = stations.indexOf(routes.get(i).getStart());
            int b = stations.indexOf(routes.get(i).getEnd());
            Long weight = routes.get(i).getTime();
            graph.input(a, b, weight);
        }
        Long[][] distances = new Long[starts.size()][stations.size()];
        for(int i=0; i<starts.size(); i++){
            distances[i] = graph.dijkstra(i);
        }
        int index = 0;
        Long min = Long.MAX_VALUE;
        for(int i=0; i<stations.size(); i++){
            Long average = 0L;
            for(int j=0; j<starts.size(); j++){
                average = distances[j][i];
            }
            average = average / starts.size();
            Long[] minus = new Long[starts.size()];
            Long variance = 0L;
            for(int j=0; j<starts.size(); j++){
                minus[j] = (distances[j][i] - average) * (distances[j][i] - average);
                variance += minus[j];
            }
            variance = variance / starts.size();
            if(min > variance){
                index = i;
                min = variance;
            }
        }
        List<RouteResponseDto> result = new LinkedList<>();
        for(int i=0; i<starts.size(); i++){
            result.add(RouteResponseDto.toDto(getShortestRouteInDifferentLine(starts.get(i), stations.get(index))));
        }
        return result;
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
