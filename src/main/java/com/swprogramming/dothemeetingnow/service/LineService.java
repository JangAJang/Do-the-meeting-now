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
    public List<LineDto> getAllLines(){
        return getLine();
    }

    @Transactional(readOnly = true)
    public List<LineDto> searchAllLineByName(String name){
        List<LineDto> lineDtos = getLine();
        for(LineDto lineDto : lineDtos){
            if(!lineDto.getLine_name().equals(name)){
                lineDtos.remove(lineDto);
            }
        }
        if(lineDtos.isEmpty()) throw new LineNotFoundException();
        return lineDtos;
    }

    @Transactional
    public LineDto updateLine(LineDto lineDto, Long id){
        Line line = getLine(id);
        validateMember(line);
        line.setName(lineDto.getLine_name());
        lineRepository.save(line);
        return LineDto.toDto(line);
    }

    @Transactional
    public String deleteLine(Long id){
        Line line = getLine(id);
        validateMember(line);
        lineRepository.delete(line);
        return "삭제완료";
    }

    private List<LineDto> getLine(){
        List<Line> lines = lineRepository.findAll();
        List<LineDto> lineDtos = new LinkedList<LineDto>();
        for(Line line : lines){
            lineDtos.add(LineDto.toDto(line));
        }
        if(lineDtos.isEmpty()) throw new LineNotFoundException();
        return lineDtos;
    }

    private Line getLine(Long id){
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        return line;
    }

    private void validateMember(Line line){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }
}
