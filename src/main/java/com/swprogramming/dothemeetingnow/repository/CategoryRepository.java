package com.swprogramming.dothemeetingnow.repository;

import com.swprogramming.dothemeetingnow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}