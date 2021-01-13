package uu.datamanagement.main;

import freemarker.template.TemplateException;
import java.util.Locale;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uu.app.subapp.AbstractSubAppConfiguration;
import uu.app.subapp.OidcAuthenticationContextConfiguration;
import uu.app.subapp.WorkspaceContextConfiguration;

/**
 * Spring configuration of the application.
 */
@Configuration
@Import({WorkspaceContextConfiguration.class, OidcAuthenticationContextConfiguration.class})
public class SubAppConfiguration extends AbstractSubAppConfiguration {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @Bean
  public freemarker.template.Configuration freemarkerConfiguration() throws TemplateException {
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
    cfg.setLocale(Locale.US);
    cfg.setClassForTemplateLoading(this.getClass(), "/freemarker/");
    cfg.setSetting("number_format", "0.#########");
    return cfg;
  }

}
