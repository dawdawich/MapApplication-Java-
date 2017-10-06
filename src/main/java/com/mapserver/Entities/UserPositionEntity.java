package com.mapserver.Entities;


import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_position")
public class UserPositionEntity {

    @Id
    @Column(name = "user_pos_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "last_update",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_update;

    @MapsId
    @OneToOne(mappedBy = "user_position")
    @JoinColumn(name = "user_pos_id")
    private UserEntity userEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Date getLast_update() {
        return last_update;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }
}
