/* eslint-disable */

const datamanagementMainInitDtoInType = shape({
  authoritiesUri: uri().isRequired()
});

const dataManagementMainClearDtoInType = shape({

})

const metadataListDtoInType = shape({
  pageInfo: shape({
    pageIndex: integer(),
    pageSize: integer()
  })
})

const metadataUpdateDtoInType = shape({
  sender: string(),
  receiver: string(),
  domain: string()
})

const gskDocumentDtoInType = shape({
  name: string(100),
  text: string(500),
  document: binary().isRequired()
})
