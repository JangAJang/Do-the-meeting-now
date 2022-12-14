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
@Table(name = "ROUTE")
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTE_ID")
    private Long id;

    @Column(name = "ROUTE_TIME")
    private Long time;

    @JoinColumn(name = "ROUTE_START")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station start;

    @JoinColumn(name = "LINE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @JoinColumn(name = "ROUTE_END")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station end;

    @Column(name = "ROUTE_STATUS")
    private RouteStatus routeStatus;

}
