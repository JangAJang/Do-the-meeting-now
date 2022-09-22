package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.route.AddRouteRequestDto;
import com.swprogramming.dothemeetingnow.dto.route.RouteResponseDto;
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
        Route route = new Route();
        setRoute(addRouteRequestDto, route);
        return RouteResponseDto.toDto(route);
    }

    @Transactional(readOnly = true)
    public List<RouteResponseDto> getAllRouteOfLine(Long line_id){
        Line line = lineRepository.findById(line_id).orElseThrow(LineNotFoundException::new);
        List<RouteResponseDto> routeResponseDtos = new LinkedList<>();
        List<Route> routes = routeRepository.findAllByLine(line);
        if(routes.isEmpty()) throw new RouteNotFoundException();
        routeResponseDtos.add(RouteResponseDto.toDto(routes.get(0)));
        routes.remove(routes.get(0));
        while(!routes.isEmpty()){
            for(Route route : routes){
                if(route.getStart().equals(routeResponseDtos.get(routeResponseDtos.size()-1))){
                    routeResponseDtos.add(RouteResponseDto.toDto(route));
                    routes.remove(route);
                }
            }
        }
        return routeResponseDtos;
    }

    @Transactional(readOnly = true)
    public RouteResponseDto getRouteInfo(Long id){
        Route route = routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        return RouteResponseDto.toDto(route);
    }

    @Transactional
    public RouteResponseDto updateRouteInfo(Long id, AddRouteRequestDto addRouteRequestDto){
        Route route = getRoute(id);
        validateMember();
        setRoute(addRouteRequestDto, route);
        return RouteResponseDto.toDto(route);
    }

    @Transactional
    public String deleteRouteInfo(Long id){
        Route route = getRoute(id);
        validateMember();
        routeRepository.delete(route);
        return "삭제 완료";
    }

    private Route getRoute(Long id){
        return routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
    }

    private void validateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }

    private Route setRoute(AddRouteRequestDto addRouteRequestDto, Route route){
        Station start = stationRepository.findByName(addRouteRequestDto.getStart()).orElseThrow(StationNotFoundException::new);
        Station end = stationRepository.findByName(addRouteRequestDto.getEnd()).orElseThrow(StationNotFoundException::new);
        Line line = lineRepository.findByName(addRouteRequestDto.getLine_name()).orElseThrow(LineNotFoundException::new);
        Long min = addRouteRequestDto.getMinute();
        Long sec = addRouteRequestDto.getSecond();
        route.setLine(line);
        route.setEnd(end);
        route.setStart(start);
        route.setTime(min*60 + sec);
        routeRepository.save(route);
        return route;
    }

}
