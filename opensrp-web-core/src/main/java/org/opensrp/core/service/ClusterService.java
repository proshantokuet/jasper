package org.opensrp.core.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.ParaCenter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClusterService {

    Cluster save(Cluster cluster) throws Exception;

    Cluster findById(Integer id);

    boolean isExist(Cluster cluster);

    List<Cluster> findAllByUnionId(List<Integer> ids);

    List<Cluster> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection);

    Long countTotal(String name);

    JSONObject getClusterDataOfDataTable(Integer draw, Long totalCluster, List<Cluster> clusters) throws JSONException;
}