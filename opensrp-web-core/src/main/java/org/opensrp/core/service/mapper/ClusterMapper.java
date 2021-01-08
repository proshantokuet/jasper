package org.opensrp.core.service.mapper;

import org.opensrp.common.dto.ClusterDTO;
import org.opensrp.common.util.DateUtil;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.ParaCenter;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.ParaCenterService;
import org.opensrp.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class ClusterMapper {

    @Autowired private UserService userService;
    @Autowired private LocationService locationService;
    @Autowired private ParaCenterService paraCenterService;

    /**
     * DTO to Entity
     *
     * @param dto
     * @return
     */
    public Cluster map(ClusterDTO dto) throws ParseException {
        Cluster cluster = new Cluster();
        if (dto.getId() != 0) cluster.setId(dto.getId());
        cluster.setCode(dto.getCode());
        cluster.setName(dto.getName());
        cluster.setCreator(userService.getLoggedInUser());
        if (dto.getCreated() != null)cluster.setCreated(DateUtil.parseDate(dto.getCreated()));
        cluster.setUpdated(new Date());
        if (dto.getDivisionId() !=null ) cluster.setDivision(locationService.findById(dto.getDivisionId(), "id", Location.class));
        if (dto.getDistrictId() !=null ) cluster.setDistrict(locationService.findById(dto.getDistrictId(), "id", Location.class));
        if (dto.getUpazilaId() !=null ) cluster.setUpazila(locationService.findById(dto.getUpazilaId(), "id", Location.class));
        if (dto.getUpazilaId() !=null ) cluster.setUpazila(locationService.findById(dto.getUpazilaId(), "id", Location.class));
        if (dto.getPourasabhaId() != null ) {
            List<Location> pourasabhas =  new ArrayList<>();
            pourasabhas.add((Location) locationService.findById(dto.getPourasabhaId(), "id", Location.class));
            cluster.setPourasabhas(new HashSet<>(pourasabhas));
        }
        if (dto.getUnionIds()!= null && dto.getUnionIds().size() > 0 ) {
            List<Location> unions = locationService.findAllById(new ArrayList<>(dto.getUnionIds()), "id", "Location");
            cluster.setUnions(new HashSet<>(unions));
        }
        if (dto.getParaCenterIds()!= null && dto.getParaCenterIds().size() > 0 ) {
            List<ParaCenter> paraCenters = paraCenterService.findAllById(new ArrayList<>(dto.getParaCenterIds()));
            cluster.setParaCenters(new HashSet<>(paraCenters));
        }
        return cluster;
    }

    public List<Cluster> map(List<ClusterDTO> dtos) {
        List<Cluster> entities = new ArrayList<>();
        dtos.forEach(dto-> {
            try {
                entities.add(this.map(dto));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return entities;
    }

    /**
     * Entity to DTO
     *
     * @param cluster
     * @return
     */
    public ClusterDTO map(Cluster cluster) {

        ClusterDTO dto = new ClusterDTO();
        dto.setId(cluster.getId());
        dto.setCode(cluster.getCode());
        dto.setName(cluster.getName());
        dto.setCreatorId(cluster.getCreator().getId());
        dto.setCreated(cluster.getCreated().toString());
        dto.setUpdated(cluster.getUpdated().toString());
        dto.setDivisionId(cluster.getDivision().getId());
        dto.setDistrictId(cluster.getDistrict().getId());
        dto.setUpazilaId(cluster.getUpazila().getId());
//        dto.setPourasabhaId(cluster.getPourasabha().getId());
//        dto.setUnionId(cluster.getUnion().getId());
//        dto.setParaId(cluster.getPara().getId());

        return dto;
    }

    public List<ClusterDTO> map(Iterable<Cluster> entities) {
        List<ClusterDTO> dtos = new ArrayList<>();
        entities.forEach(entity->dtos.add(this.map(entity)));
        return dtos;
    }
}
