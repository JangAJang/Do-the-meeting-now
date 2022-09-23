package com.swprogramming.dothemeetingnow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @JoinColumn(name = "FROM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station from;

    @JoinColumn(name = "TO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station to;

    @Column(name = "TRASFER_TIME")
    private Long time;

    @JoinColumn(name = "TRANSFER_REVERSE")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @OneToOne(fetch = FetchType.LAZY)
    private Transfer reverse;
}
