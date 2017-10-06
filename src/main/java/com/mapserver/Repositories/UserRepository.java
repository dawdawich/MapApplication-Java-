package com.mapserver.Repositories;


import com.mapserver.Entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

    UserEntity findByNickname(String nickname);

    UserEntity findByEmail(String email);

    Set<UserEntity> findById (int[] id);

}
