package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Route;
import com.swprogramming.dothemeetingnow.entity.RouteStatus;
import com.swprogramming.dothemeetingnow.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAll();

    List<Route> findAllByLine(Line line);

    List<Route> findAllByRouteStatus(RouteStatus routeStatus);

    Optional<Route> findByStartAndEnd(Station start, Station end);

}
