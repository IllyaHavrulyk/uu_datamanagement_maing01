package uu.datamanagement.main.dao.mongo;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.Index;
import uu.app.objectstore.annotations.ObjectStoreDao;
import uu.app.objectstore.mongodb.dao.UuObjectMongoDao;
import uu.datamanagement.main.dao.DatamanagementMainDao;
import uu.datamanagement.main.abl.entity.DatamanagementMain;

@ObjectStoreDao(entity = DatamanagementMain.class, store = "primary")
public class DatamanagementMainMongoDao extends UuObjectMongoDao<DatamanagementMain> implements DatamanagementMainDao {

 public void createSchema() {
   super.createSchema();
   createIndex(new Index().on(ATTR_AWID, Direction.ASC).unique());
 }

}
