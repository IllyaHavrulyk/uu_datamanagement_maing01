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
