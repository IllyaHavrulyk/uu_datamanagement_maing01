package uu.datamanagement.main.abl;

import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PageInfo;
import uu.app.datastore.domain.PagedResult;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
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

}
