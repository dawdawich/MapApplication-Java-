package com.mapserver.Entities;

import javax.persistence.*;

@Entity
@Table(name = "invite_tb")
public class InviteEntity {

    @Id
    @Column(name = "invite_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private UserEntity from;

    @ManyToOne
    private UserEntity to;

//    @Column(name = "status")
//    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getFrom() {
        return from;
    }

    public void setFrom(UserEntity from) {
        this.from = from;
    }

    public UserEntity getTo() {
        return to;
    }

    public void setTo(UserEntity to) {
        this.to = to;
    }

//    public Boolean getStatus() {
//        return status;
//    }
//
//    public void setStatus(Boolean status) {
//        this.status = status;
//    }
}
