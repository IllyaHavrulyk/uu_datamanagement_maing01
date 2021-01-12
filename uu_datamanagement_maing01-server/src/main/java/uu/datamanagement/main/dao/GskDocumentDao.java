package uu.datamanagement.main.dao;

import com.mongodb.WriteResult;
import uu.app.objectstore.dao.UuObjectDao;
import uu.datamanagement.main.abl.entity.GskDocument;

public interface GskDocumentDao extends UuObjectDao<GskDocument> {

  WriteResult deleteMany(String awid);

}
