package org.opensrp.core.repository;

import org.opensrp.core.entity.Cluster;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRepository {

    Cluster save(Cluster cluster) throws Exception;

    List<Cluster> saveAll(List<Cluster> clusters) throws Exception;

    Cluster findById(Integer id);

    List<Cluster> findAllByUnionId(List<Integer> ids);

    Cluster update(Cluster cluster);

    boolean delete(Cluster cluster);

    boolean isExist(Cluster cluster);

    List<Cluster> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection);

    List<Cluster> findAllById(List<Integer> ids);

    Long countTotal(String name);
}
