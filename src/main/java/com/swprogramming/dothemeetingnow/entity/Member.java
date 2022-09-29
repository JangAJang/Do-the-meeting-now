package com.swprogramming.dothemeetingnow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "MEMBER_USERNAME", nullable = false)
    private String username;

    @Column(name = "MEMBER_NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "MEMBER_PASSWORD", nullable = false)
    private String password;

    @Column(name = "MEMBER_PHONE", nullable = false)
    private String phone;

    @Column(name = "MEMBER_EMAIL", nullable = false)
    private String email;

    @Column(name = "MEMBER_ADDRESS", nullable = false)
    private String city;

    @Column(name = "MEMBER_STREET", nullable = false)
    private String street;

    @Enumerated(EnumType.STRING)
    private Authority authority;


}
