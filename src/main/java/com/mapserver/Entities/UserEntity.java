package com.mapserver.Entities;



import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_creation",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_of_creation;


    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserPositionEntity user_position;

    @ManyToMany(cascade={CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name="friends_relations",
            joinColumns={@JoinColumn(name="user_one", referencedColumnName = "user_id")},
            inverseJoinColumns={@JoinColumn(name="user_two", referencedColumnName = "user_id")})
    private Set<UserEntity> friendsPartOne = new HashSet<>();

    @ManyToMany(mappedBy = "friendsPartOne")
    private Set<UserEntity> friendsPartTwo = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "to")
    private Set<InviteEntity> incomingInvites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "from")
    private Set<InviteEntity> outcomingInvite = new HashSet<>();

    @Column(name = "get_update", columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isUpdate;

    @Column(name = "avatar_path")
    private String avatar_path;

    @Column(name = "avatar_name")
    private String avatar_name;

    @Transient
    private Set<UserEntity> friends = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(Date date_of_creation) {
        this.date_of_creation = date_of_creation;
    }

    public UserPositionEntity getUser_position() {
        return user_position;
    }

    public void setUser_position(UserPositionEntity user_position) {
        this.user_position = user_position;
    }

    public Set<InviteEntity> getIncomingInvites() {
        return incomingInvites;
    }

    public void setIncomingInvites(Set<InviteEntity> incomingInvites) {
        this.incomingInvites = incomingInvites;
    }

    public Set<InviteEntity> getOutcomingInvite() {
        return outcomingInvite;
    }

    public void setOutcomingInvite(Set<InviteEntity> outcomingInvite) {
        this.outcomingInvite = outcomingInvite;
    }

    public Boolean getUpdate() {
        return isUpdate;
    }

    public void setUpdate(Boolean getUpdate) {
        this.isUpdate = getUpdate;
    }

    public Set<UserEntity> getFriendsPartOne() {
        return friendsPartOne;
    }

    public void setFriendsPartOne(Set<UserEntity> friendsPartOne) {
        this.friendsPartOne = friendsPartOne;
    }

    public Set<UserEntity> getFriendsPartTwo() {
        return friendsPartTwo;
    }

    public void setFriendsPartTwo(Set<UserEntity> friendsPartTwo) {
        this.friendsPartTwo = friendsPartTwo;
    }

    public Set<UserEntity> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserEntity> friends) {
        this.friends = friends;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getAvatar_name() {
        return avatar_name;
    }

    public void setAvatar_name(String avatar_name) {
        this.avatar_name = avatar_name;
    }

    public void combineFriends()
    {
        friends.clear();
        friends.addAll(friendsPartOne);
        friends.addAll(friendsPartTwo);
    }
}
