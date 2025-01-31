package uu.datamanagement.main.dao.mongo;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import uu.app.objectstore.annotations.ObjectStoreDao;
import uu.app.objectstore.mongodb.dao.UuObjectMongoDao;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.dao.GskDocumentDao;

@ObjectStoreDao(entity = GskDocument.class, store = "primary")
public class GskDocumentMongoDao extends UuObjectMongoDao<GskDocument> implements GskDocumentDao {

  private final static String ATTR_METADATA_ID = "metadataId";

  @Override
  public WriteResult deleteMany(String awid) {
    Query query = new Query().addCriteria(Criteria.where(ATTR_AWID).is(awid));

    return this.deleteMany(query);
  }

  @Override
  public GskDocument getByMetadataId(String awid, String metadataId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(ATTR_AWID).is(awid));
    query.addCriteria(Criteria.where(ATTR_METADATA_ID).is(metadataId));
    return this.findOne(query);
  }
}
