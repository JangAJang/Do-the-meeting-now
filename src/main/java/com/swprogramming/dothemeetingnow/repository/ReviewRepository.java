package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByContent(String content);

    List<Review> findAllByStation(Station station);
}
