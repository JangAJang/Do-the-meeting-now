package com.swprogramming.dothemeetingnow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRANSFER")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSFER_ID")
    private Long id;

    @JoinColumn(name = "LINE_START")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line depart;

    @JoinColumn(name = "LINE_DEST")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line dest;

    @JoinColumn(name = "STATION_START")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    @Column(name = "TRANSFER_DISTANCE")
    private Long distance;

    @Column(name = "TRASFER_TIME")
    private Long time;
}
