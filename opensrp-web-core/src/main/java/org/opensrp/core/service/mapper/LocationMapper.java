package org.opensrp.core.service.mapper;

import org.opensrp.common.dto.LocationDTO;
import org.opensrp.core.dto.LocationHierarchyDTO;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.LocationTag;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.LocationTagService;
import org.opensrp.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class LocationMapper {

    @Autowired private LocationService locationService;
    @Autowired private LocationTagService locationTagService;
    @Autowired private UserService userService;

    public Location map(LocationDTO dto) {
        Location location = new Location();

        if (dto.getId() != null) location.setId(dto.getId());
        location.setName(dto.getName().toUpperCase());
        location.setCode(dto.getCode());

        location.setDescription(dto.getDescription());

        location.setParentLocation(locationService.findById(dto.getParentLocationId(), "id", Location.class));
        if (dto.getDivisionId() != null) location.setDivision(locationService.findById(dto.getDivisionId(), "id", Location.class));
        if (dto.getDistrictId() != null) location.setDistrict(locationService.findById(dto.getDistrictId(), "id", Location.class));
        if (dto.getPourasabhaId() != null) location.setPourasabha(locationService.findById(dto.getPourasabhaId(), "id", Location.class));
        if (dto.getUpazilaId() != null) location.setUpazila(locationService.findById(dto.getUpazilaId(), "id", Location.class));
        if (dto.getUnionId() != null) location.setUnion(locationService.findById(dto.getUnionId(), "id", Location.class));
        location.setLocationTag(locationTagService.findById(dto.getLocationTagId(), "id", LocationTag.class));

        location.setCreated(dto.getCreated());
        location.setUpdated(dto.getUpdated());
        location.setCreator(userService.getLoggedInUser()); // logged in user is the creator of this location

        location.setLoginLocation(dto.isLoginLocation());
        location.setVisitLocation(dto.isVisitLocation());

        return location;
    }

    public LocationDTO map(Location location) {
        LocationDTO dto = new LocationDTO();

        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setCode(location.getCode());

        dto.setDescription(location.getDescription());
        dto.setParentLocationId(location.getParentLocation().getId());
        dto.setLocationTagId(location.getLocationTag().getId());

        dto.setCreated(location.getCreated());
        dto.setUpdated(location.getUpdated());

        dto.setLoginLocation(location.isLoginLocation());
        dto.setVisitLocation(location.isVisitLocation());

        if (location.getUnion() != null) dto.setUnionId(location.getUnion().getId());
        if (location.getPourasabha() != null) dto.setPourasabhaId(location.getPourasabha().getId());
        if (location.getUpazila() != null) dto.setUpazilaId(location.getUpazila().getId());
        if (location.getDistrict() != null) dto.setDistrictId(location.getDistrict().getId());
        if (location.getDivision() != null)  dto.setDivisionId(location.getDivision().getId());

        return dto;
    }
}
