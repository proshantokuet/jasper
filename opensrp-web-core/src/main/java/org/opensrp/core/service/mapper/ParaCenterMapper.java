package org.opensrp.core.service.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opensrp.common.dto.ParaCenterDTO;
import org.opensrp.common.util.DateUtil;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.ParaCenter;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParaCenterMapper {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LocationService locationService;
	
	/**
	 * DTO to Entity
	 *
	 * @param dto
	 * @return
	 */
	public ParaCenter map(ParaCenterDTO dto) throws ParseException {
		ParaCenter paraCenter = new ParaCenter();
		if (dto.getId() != 0)
			paraCenter.setId(dto.getId());
		paraCenter.setName(dto.getName());
		paraCenter.setCreator(userService.getLoggedInUser());
		paraCenter.setCreated(DateUtil.parseDate(dto.getCreated()));
		paraCenter.setUpdated(new Date());
		paraCenter.setParaKormiName(dto.getParaKormiName());
		paraCenter.setEstablished(dto.getEstablished());
		paraCenter.setDhis2Id(dto.getDhis2Id());
		if (dto.getDivisionId() != null)
			paraCenter.setDivision(locationService.findById(dto.getDivisionId(), "id", Location.class));
		if (dto.getDistrictId() != null)
			paraCenter.setDistrict(locationService.findById(dto.getDistrictId(), "id", Location.class));
		if (dto.getUpazilaId() != null)
			paraCenter.setUpazila(locationService.findById(dto.getUpazilaId(), "id", Location.class));
		if (dto.getPourasabhaId() != null)
			paraCenter.setPourasabha(locationService.findById(dto.getPourasabhaId(), "id", Location.class));
		if (dto.getUnionId() != null)
			paraCenter.setUnion(locationService.findById(dto.getUnionId(), "id", Location.class));
		if (dto.getDistrictId() != null)
			paraCenter.setPara(locationService.findById(dto.getParaId(), "id", Location.class));
		if (dto.getCode() != null && paraCenter.getDistrict() != null && paraCenter.getUpazila() != null
		        && paraCenter.getPourasabha() != null && paraCenter.getUnion() != null && paraCenter.getPara() != null) {
			paraCenter.setCode(paraCenter.getDistrict().getCode() + paraCenter.getUpazila().getCode()
			        + paraCenter.getPourasabha().getCode() + paraCenter.getUnion().getCode()
			        + paraCenter.getPara().getCode() + dto.getCode());
		}
		return paraCenter;
	}
	
	public List<ParaCenter> map(List<ParaCenterDTO> dtos) {
		List<ParaCenter> entities = new ArrayList<>();
		dtos.forEach(dto -> {
			try {
				entities.add(this.map(dto));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return entities;
	}
	
	/**
	 * Entity to DTO
	 *
	 * @param paraCenter
	 * @return
	 */
	public ParaCenterDTO map(ParaCenter paraCenter) {
		
		ParaCenterDTO dto = new ParaCenterDTO();
		dto.setId(paraCenter.getId());
		dto.setCode(paraCenter.getCode());
		dto.setName(paraCenter.getName());
		dto.setCreatorId(userService.getLoggedInUser().getId());
		dto.setCreated(paraCenter.getCreated().toString());
		dto.setUpdated(paraCenter.getUpdated().toString());
		dto.setDivisionId(paraCenter.getDivision().getId());
		dto.setDistrictId(paraCenter.getDistrict().getId());
		dto.setUpazilaId(paraCenter.getUpazila().getId());
		dto.setPourasabhaId(paraCenter.getPourasabha().getId());
		dto.setUnionId(paraCenter.getUnion().getId());
		dto.setParaId(paraCenter.getPara().getId());
		dto.setDhis2Id(paraCenter.getDhis2Id());
		
		return dto;
	}
	
	public List<ParaCenterDTO> map(Iterable<ParaCenter> entities) {
		List<ParaCenterDTO> dtos = new ArrayList<>();
		entities.forEach(entity -> dtos.add(this.map(entity)));
		return dtos;
	}
}
