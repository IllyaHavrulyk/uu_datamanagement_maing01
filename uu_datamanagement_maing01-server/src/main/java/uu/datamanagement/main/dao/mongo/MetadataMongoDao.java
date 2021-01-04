package uu.datamanagement.main.dao.mongo;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import uu.app.objectstore.annotations.ObjectStoreDao;
import uu.app.objectstore.mongodb.dao.UuObjectMongoDao;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.dao.MetadataDao;

@ObjectStoreDao(entity = Metadata.class, store = "primary")
public class MetadataMongoDao extends UuObjectMongoDao<Metadata> implements MetadataDao {

  @Override
  public WriteResult deleteMany(String awid) {
    Query query = new Query().addCriteria(Criteria.where(ATTR_AWID).is(awid));

    return this.deleteMany(query);
  }
}
