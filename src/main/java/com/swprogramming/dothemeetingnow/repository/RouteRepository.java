package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAll();

    List<Route> findAllByLine(Line line);

    List<Route> findAllByRouteStatus(RouteStatus routeStatus);

    Optional<Route> findByStartAndEnd(Station start, Station end);

}
