package org.opensrp.core.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.entity.ParaCenter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParaCenterService {

    ParaCenter save(ParaCenter paraCenter) throws Exception;

    boolean isExist(ParaCenter paraCenter);

    List<ParaCenter> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection);

    List<ParaCenter> findAllById(List<Integer> ids);

    List<ParaCenter> findAllByUnionId(List<Integer> ids);

    Long countTotal(String name);

    JSONObject getParaCenterDataOfDataTable(Integer draw, Long totalParaCenter, List<ParaCenter> paraCenters) throws JSONException;
}
