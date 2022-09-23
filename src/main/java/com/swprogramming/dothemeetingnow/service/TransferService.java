package com.swprogramming.dothemeetingnow.service;

import com.swprogramming.dothemeetingnow.dto.transfer.AddTransferRequestDto;
import com.swprogramming.dothemeetingnow.dto.transfer.AddTransferResponseDto;
import com.swprogramming.dothemeetingnow.entity.*;
import com.swprogramming.dothemeetingnow.exception.*;
import com.swprogramming.dothemeetingnow.repository.LineRepository;
import com.swprogramming.dothemeetingnow.repository.MemberRepository;
import com.swprogramming.dothemeetingnow.repository.StationRepository;
import com.swprogramming.dothemeetingnow.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransferService {

    private final MemberRepository memberRepository;
    private final TransferRepository transferRepository;
    private final StationRepository  stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public AddTransferResponseDto addTransfer(AddTransferRequestDto addTransferRequestDto){
        validateMember();
        Transfer transfer = new Transfer();
        Transfer reverse = new Transfer();
        setTransfer(addTransferRequestDto, transfer, reverse);
        transferRepository.save(reverse);
        transferRepository.save(transfer);
        return AddTransferResponseDto.toDto(transfer);
    }

    @Transactional
    public AddTransferResponseDto updateTransfer(AddTransferRequestDto addTransferRequestDto, Long id){
        validateMember();
        Transfer transfer = transferRepository.findById(id).orElseThrow(TransferNotFoundException::new);
        Transfer reverse = transfer.getReverse();
        setTransfer(addTransferRequestDto, transfer, reverse);
        transferRepository.save(reverse);
        transferRepository.save(transfer);
        return AddTransferResponseDto.toDto(transfer);
    }

    @Transactional
    public String deleteTransfer(Long id){
        validateMember();
        Transfer transfer = transferRepository.findById(id).orElseThrow(TransferNotFoundException::new);
        Transfer reverse = transfer.getReverse();
        transferRepository.delete(transfer);
        transferRepository.delete(reverse);
        return "삭제완료";
    }

    private void setTransfer(AddTransferRequestDto addTransferRequestDto, Transfer transfer, Transfer reverse){
        Line from_line = lineRepository.findByName(addTransferRequestDto.getFrom_line()).orElseThrow(LineNotFoundException::new);
        Line to_line = lineRepository.findByName(addTransferRequestDto.getTo_line()).orElseThrow(LineNotFoundException::new);
        if(from_line.equals(to_line)) throw new LineSameException();
        Station from = stationRepository.findByNameAndLine(addTransferRequestDto.getStation_name(), from_line).orElseThrow(StationNotFoundException::new);
        Station to = stationRepository.findByNameAndLine(addTransferRequestDto.getStation_name(), to_line).orElseThrow(StationNotFoundException::new);
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setTime(addTransferRequestDto.getMin() * 60 + addTransferRequestDto.getSec());
        reverse.setFrom(to);
        reverse.setTo(from);
        reverse.setTime(transfer.getTime());
        transfer.setReverse(reverse);
        reverse.setReverse(transfer);
    }

    private void validateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);
        if(member.getAuthority().equals(Authority.ROLE_USER)) throw new MemberNotAuthorized();
    }
}
