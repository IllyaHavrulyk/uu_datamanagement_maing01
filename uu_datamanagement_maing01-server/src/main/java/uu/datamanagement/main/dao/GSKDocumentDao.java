package uu.datamanagement.main.dao;

import com.mongodb.WriteResult;
import uu.app.objectstore.dao.UuObjectDao;
import uu.datamanagement.main.abl.entity.GSKDocument;

public interface GSKDocumentDao extends UuObjectDao<GSKDocument> {

  WriteResult deleteMany(String awid);

}
