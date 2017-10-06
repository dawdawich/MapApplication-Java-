package com.mapserver.Repositories;


import com.mapserver.Entities.InviteEntity;
import org.springframework.data.repository.CrudRepository;

public interface InviteRepository extends CrudRepository<InviteEntity, Long>{

    InviteEntity findByFromNicknameAndToNickname(String from_nickname, String to_nickname);

}
