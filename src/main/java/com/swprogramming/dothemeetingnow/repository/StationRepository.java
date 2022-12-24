package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findAllByLine(Line line);

    Optional<Station> findByNameAndLine(String name, Line line);




}
