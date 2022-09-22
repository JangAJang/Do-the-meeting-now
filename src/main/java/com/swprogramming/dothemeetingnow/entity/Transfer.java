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

    @JoinColumn(name = "STATION_START")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    @JoinColumn(name = "LINE_DESTINATION")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line destination;

    @Column(name = "TRANSFER_DISTANCE")
    private Long distance;

    @Column(name = "TRASFER_TIME")
    private Long time;
}
