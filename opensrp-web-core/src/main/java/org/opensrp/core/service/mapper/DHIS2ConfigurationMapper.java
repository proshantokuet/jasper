package org.opensrp.core.service.mapper;

import org.opensrp.core.dto.DHIS2ConfigurationDTO;
import org.opensrp.core.entity.DhisConfiguration;
import org.springframework.stereotype.Service;

@Service
public class DHIS2ConfigurationMapper {
	
	public DhisConfiguration map(DHIS2ConfigurationDTO dto, DhisConfiguration dhisConfiguration) throws Exception {
		dhisConfiguration.setFormName(dto.getFormName());
		dhisConfiguration.setFieldName(dto.getFieldName());
		dhisConfiguration.setDhisId(dto.getDhisId());
		dhisConfiguration.setValue(dto.getValue());
		dhisConfiguration.setStatus(dto.isStatus());
		dhisConfiguration.setDynamic(dto.getDynamic());
		return dhisConfiguration;
	}
}
