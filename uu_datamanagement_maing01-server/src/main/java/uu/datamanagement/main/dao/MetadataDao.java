package uu.datamanagement.main.dao;

import com.mongodb.WriteResult;
import uu.app.objectstore.dao.UuObjectDao;
import uu.datamanagement.main.abl.entity.Metadata;

public interface MetadataDao extends UuObjectDao<Metadata> {

  WriteResult deleteMany(String awid);

}
