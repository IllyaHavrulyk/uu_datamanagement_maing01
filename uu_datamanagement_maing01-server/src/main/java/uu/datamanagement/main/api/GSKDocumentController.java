package uu.datamanagement.main.api;

import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.GSKDocumentAbl;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;

@CommandController
public class GSKDocumentController {

  private final GSKDocumentAbl gskDocumentAbl;

  @Inject
  public GSKDocumentController(GSKDocumentAbl gskDocumentAbl) {
    this.gskDocumentAbl = gskDocumentAbl;
  }

  @Command(path = "gskdocument/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public GSKDocumentDtoOut create(CommandContext<GSKDocumentDtoIn> ctx) {
    return gskDocumentAbl.create(ctx.getUri().getAwid(), ctx.getDtoIn());
  }


}
