package org.opensrp.core.repository;

import org.opensrp.core.entity.ParaCenter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParaCenterRepository {

    ParaCenter save(ParaCenter paraCenter) throws Exception;

    List<ParaCenter> saveAll(List<ParaCenter> paraCenters) throws Exception;

    ParaCenter update(ParaCenter paraCenter);

    boolean delete(ParaCenter paraCenter);

    boolean isExist(ParaCenter paraCenter);

    List<ParaCenter> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection);

    List<ParaCenter> findAllById(List<Integer> ids);

    List<ParaCenter> findAllByUnionId(List<Integer> ids);

    Long countTotal(String name);
}
