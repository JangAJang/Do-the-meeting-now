package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
