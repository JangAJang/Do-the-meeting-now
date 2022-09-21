package com.swprogramming.dothemeetingnow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROUTE")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTE_ID")
    private Long id;

    @JoinColumn(name = "LINE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @Column(name = "ROUTE_TIME")
    private Long time;

    @JoinColumn(name = "ROUTE_START")
    @OneToOne
    private Station start;

    @JoinColumn(name = "ROUTE_END")
    @OneToOne
    private Station end;

}
