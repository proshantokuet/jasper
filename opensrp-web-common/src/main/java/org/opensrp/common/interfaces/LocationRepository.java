package org.opensrp.common.interfaces;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository {

    <T> List<T> getLocationsHierarchy(String name, Integer tagId, Integer length, Integer start, String orderColumn, String orderDirection);

    <T> T countTotal(String name, Integer tagId);

    <T> List<T> findAllByParents(List<Integer> ids);
}
