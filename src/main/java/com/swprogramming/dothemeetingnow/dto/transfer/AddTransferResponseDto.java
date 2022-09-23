package com.swprogramming.dothemeetingnow.dto.transfer;


import com.swprogramming.dothemeetingnow.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddTransferResponseDto {

    private String from_line;

    private String to_line;

    private String station_name;

    private Long time;

    public static AddTransferResponseDto toDto(Transfer transfer){
        return AddTransferResponseDto.builder()
                .from_line(transfer.getFrom().getLine().getName())
                .to_line(transfer.getTo().getLine().getName())
                .station_name(transfer.getFrom().getName())
                .time(transfer.getTime())
                .build();
    }
}
