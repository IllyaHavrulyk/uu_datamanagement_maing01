package uu.datamanagement.main;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import uu.app.auditlog.AuditLogLibraryConfiguration;
import uu.app.datastore.annotations.DataStoreConfiguration;
import uu.app.datastore.mongodb.AbstractPersistenceMongoDbContextConfiguration;
import uu.app.datastore.mongodb.DatastoreMongoDbContextConfiguration;
import uu.app.objectstore.annotations.ObjectStore;
import uu.app.workspace.store.WorkspaceStorageConfiguration;
import uu.datamanagement.main.utils.converters.ZonedDateTimeReadConverter;
import uu.datamanagement.main.utils.converters.ZonedDateTimeWriteConverter;

/**
 * Spring configuration of the application persistence.
 */
@DataStoreConfiguration
@Import({DatastoreMongoDbContextConfiguration.class})
public class SubAppPersistenceConfiguration extends AbstractPersistenceMongoDbContextConfiguration {

  private final List<Converter<?, ?>> converters = new ArrayList<>();

  @Value("${uuSubAppDataStore.primary}")
  private String objectStorePrimaryUri;

  @Bean({"primaryObjectStoreFactory"})
  public MongoDbFactory primaryOsMongoFactory() {
    return getMongoDbFactory(objectStorePrimaryUri);
  }

  @ObjectStore(name = {"primary", WorkspaceStorageConfiguration.WORKSPACE_OBJECT_STORE, AuditLogLibraryConfiguration.AUDIT_OBJECT_STORE}, primary = true)
  public MongoTemplate primaryMongo(@Qualifier("primaryObjectStoreFactory") MongoDbFactory mongoDbFactory) {
    return getMongoTemplate(mongoDbFactory);
  }

  @Override
  public CustomConversions customConversions() {
    converters.add(new ZonedDateTimeReadConverter());
    converters.add(new ZonedDateTimeWriteConverter());

    return new CustomConversions(converters);
  }
}
