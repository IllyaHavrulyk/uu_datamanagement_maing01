package uu.datamanagement.main.rules;

import java.util.List;
import org.junit.rules.ExternalResource;
import uu.app.datastore.dao.UuDataEntityDao;
import uu.app.datastore.domain.PageInfo;
import uu.app.datastore.domain.UuDataEntity;

public class ClearDatabaseRule extends ExternalResource {

  protected String awid = "11111111111111111111111111111123";

  private final List<UuDataEntityDao> daos;

  public ClearDatabaseRule(List<UuDataEntityDao> daos) {
    this.daos = daos;
  }

  @Override
  protected void before() {
    cleanUp();
  }

  @Override
  protected void after() {
    cleanUp();
  }

  private void cleanUp() {
    for (UuDataEntityDao<?> dao : daos) {
      deleteTestData(dao);
    }
  }

  private <T extends UuDataEntity> void deleteTestData(UuDataEntityDao<T> dao) {
    for (T item : dao.list(awid, new PageInfo(0, Integer.MAX_VALUE)).getItemList()) {
      dao.delete(item);
    }
  }

  public String getAwid() {
    return awid;
  }
}
