package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LineService {

    private final LineRepository lineRepository;
}
