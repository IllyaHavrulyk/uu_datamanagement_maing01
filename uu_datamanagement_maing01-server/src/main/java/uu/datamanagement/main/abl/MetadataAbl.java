package uu.datamanagement.main.abl;

import java.util.Collections;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PagedResult;
import uu.app.server.dto.PageInfo;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoIn;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoOut;
import uu.datamanagement.main.api.exceptions.MetadataRuntimeException;
import uu.datamanagement.main.api.exceptions.MetadataRuntimeException.Error;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.ValidationHelper;

@Component
public class MetadataAbl {

  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final ModelMapper modelMapper;

  @Inject
  public MetadataAbl(MetadataDao metadataDao, ValidationHelper validationHelper, ModelMapper modelMapper) {
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.modelMapper = modelMapper;
  }

  public MetadataListDtoOut list(String awid, MetadataListDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.LIST_INVALID_DTO_IN);

    PagedResult<Metadata> metadataPagedResult = metadataDao.list(awid, dtoIn.getPageInfo());
    MetadataListDtoOut dtoOut = new MetadataListDtoOut();
    dtoOut.setItemList(metadataPagedResult.getItemList());
    PageInfo pageInfo = modelMapper.map(metadataPagedResult.getPageInfo(), PageInfo.class);
    dtoOut.setPageInfo(pageInfo);

    return dtoOut;
  }

  public MetadataUpdateDtoOut update(String awid, MetadataUpdateDtoIn dtoIn) {
    // check moderation phase

    validationHelper.validateDtoIn(dtoIn, Error.UPDATE_INVALID_DTO_IN);

    Metadata metadata = metadataDao.getById(awid, dtoIn.getId())
      .orElseThrow(() -> new MetadataRuntimeException(Error.GET_METADATA_FAILED, Collections.singletonMap("id", dtoIn.getId())));

    Metadata updatedData = updateData(metadata, dtoIn);
    try {
      updatedData = metadataDao.update(updatedData);
    } catch (MetadataRuntimeException e) {
      throw new MetadataRuntimeException(Error.UPDATE_METADATA_FAILED, e);
    }
    return modelMapper.map(updatedData, MetadataUpdateDtoOut.class);
  }

  private Metadata updateData(Metadata metadata, MetadataUpdateDtoIn dtoIn) {
    if (dtoIn.getDomain() != null) {
      metadata.setDomain(dtoIn.getDomain());
    }
    if (dtoIn.getReceiver() != null) {
      metadata.setReceiver(dtoIn.getReceiver());
    }
    if (dtoIn.getSender() != null) {
      metadata.setSender(dtoIn.getSender());
    }

    return metadata;
  }

}
