package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    Optional<Line> findByName(String name);
}
