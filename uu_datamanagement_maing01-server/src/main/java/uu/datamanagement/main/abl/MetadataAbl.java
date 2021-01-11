package uu.datamanagement.main.abl;

import java.util.Collections;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PagedResult;
import uu.app.exception.AppErrorMap;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.MetadataDtoIn;
import uu.datamanagement.main.api.dto.MetadataDtoOut;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoIn;
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
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    PagedResult<Metadata> metadataPagedResult = metadataDao.list(awid, dtoIn.getPageInfo());
    MetadataListDtoOut dtoOut = new MetadataListDtoOut();
    dtoOut.setItemList(metadataPagedResult.getItemList());
    dtoOut.setPageInfo(modelMapper.map(metadataPagedResult.getPageInfo(), uu.app.server.dto.PageInfo.class));

    return dtoOut;
  }

  public MetadataDtoOut update(String awid, MetadataUpdateDtoIn dtoIn) {
    // check moderation phase

    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    Metadata metadata = metadataDao.getById(awid, dtoIn.getId())
      .orElseThrow(() -> new MetadataRuntimeException(Error.GET_METADATA_FAILED, Collections.singletonMap("id", dtoIn.getId())));

    Metadata source = updateData(metadata, dtoIn);
    return modelMapper.map(source, MetadataDtoOut.class);
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

  public MetadataDtoOut create(String awid, MetadataDtoIn dtoIn) {
    Metadata metadata = modelMapper.map(dtoIn, Metadata.class);
    metadata.setAwid(awid);
    return modelMapper.map(metadataDao.create(metadata), MetadataDtoOut.class);
  }
}
