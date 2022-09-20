package com.swprogramming.dothemeetingnow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "STATION")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @JoinColumn(name = "LINE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @Column(name = "STATION_NAME", nullable = false)
    private String name;
}
