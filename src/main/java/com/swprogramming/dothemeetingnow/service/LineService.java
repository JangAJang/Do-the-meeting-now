package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.line.LineDto;
import com.swprogramming.dothemeetingnow.entity.Authority;
import com.swprogramming.dothemeetingnow.entity.Line;
import com.swprogramming.dothemeetingnow.entity.Member;
import com.swprogramming.dothemeetingnow.exception.LineNotFoundException;
import com.swprogramming.dothemeetingnow.exception.MemberNotAuthorized;
import com.swprogramming.dothemeetingnow.exception.MemberNotFoundException;
import com.swprogramming.dothemeetingnow.repository.LineRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public LineDto createLine(LineDto lineDto){
        Line line = new Line();
        line.setName(lineDto.getLine_name());
        lineRepository.save(line);
        return LineDto.toDto(line);
    }

    @Transactional(readOnly = true)
    public List<LineDto> searchAllLineDto(){
        return changeEntityToDto(getAllLineEntity());
    }

    @Transactional(readOnly = true)
    public List<LineDto> searchAllLineByName(String name){
        List<Line> lines = getAllLineEntity();
        filterByName(lines, name);
        return changeEntityToDto(lines);
    }

    @Transactional
    public LineDto updateLine(LineDto lineDto, Long id){
        validateMember();
        Line line = getLine(id);
        line.setName(lineDto.getLine_name());
        lineRepository.save(line);
        return LineDto.toDto(line);
    }

    @Transactional
    public String deleteLine(Long id){
        validateMember();
        Line line = getLine(id);
        lineRepository.delete(line);
        return "삭제완료";
    }

    private List<Line> getAllLineEntity(){
        List<Line> lines = lineRepository.findAll();
        if(lines.isEmpty()) throw new LineNotFoundException();
        return lines;
    }

    private List<LineDto> changeEntityToDto(List<Line> lines){
        List<LineDto> lineDtos = new ArrayList<>();
        for(Line line : lines){
            lineDtos.add(LineDto.toDto(line));
        }
        return lineDtos;
    }

    private Line getLine(Long id){
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        return line;
    }

    private void validateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }

    private void filterByName(List<Line> lines, String name){
        for(Line line : lines){
            if(!line.getName().equals(name)) lines.remove(line);
        }
    }
}
