package com.mapserver.Repositories;


import com.mapserver.Entities.UserPositionEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserPositionRepository extends CrudRepository<UserPositionEntity, Long> {

    UserPositionEntity findByUserEntityId(int id);

}
