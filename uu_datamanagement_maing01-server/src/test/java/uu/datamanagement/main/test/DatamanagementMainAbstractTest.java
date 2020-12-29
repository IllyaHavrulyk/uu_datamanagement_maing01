package uu.datamanagement.main.test;

import javax.inject.Inject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uu.app.authorization.Authorization;
import uu.app.workspace.AppWorkspace;
import uu.datamanagement.main.SubAppRunner;
import uu.datamanagement.main.abl.DatamanagementMainAbl;

@RunWith(SpringRunner.class)
@PropertySource("classpath:test.properties")
@SpringBootTest(classes = {SubAppRunner.class, DatamanagementMainAbl.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DatamanagementMainAbstractTest {

  @MockBean
  protected Authorization authorization;

  @Inject
  protected AppWorkspace workspaceModel;

  @Inject
  protected DatamanagementMainAbl datamanagementMainAbl;

}
