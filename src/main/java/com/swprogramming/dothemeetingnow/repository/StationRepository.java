package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findAllByLine(Line line);

    Optional<Station> findByNameAndLine(String name, Line line);

    List<Station> findAllByName(String name);




}
