package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Route;
import com.swprogramming.dothemeetingnow.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAll();

    List<Route> findAllByLine(Line line);

    List<Route> findAllByLineAndStart(Line line, Station start);



}
