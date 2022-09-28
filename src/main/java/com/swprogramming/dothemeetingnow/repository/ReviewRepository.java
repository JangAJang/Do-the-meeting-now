package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Review;
import com.swprogramming.dothemeetingnow.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByContent(String content);

    List<Review> findAllByStation(Station station);
}
