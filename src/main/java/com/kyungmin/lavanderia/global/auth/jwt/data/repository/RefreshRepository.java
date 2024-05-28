package com.kyungmin.lavanderia.global.auth.jwt.data.repository;

import com.kyungmin.lavanderia.global.auth.jwt.data.entity.RefreshEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends CrudRepository<RefreshEntity,String> {

    Boolean existsByRefresh(String refresh);

    RefreshEntity findByRefresh(String refresh);
}
