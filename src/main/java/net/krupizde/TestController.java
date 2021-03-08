package net.krupizde;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.types.files.FileCustomizableResponseType;
import io.micronaut.http.server.types.files.StreamedFile;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class TestController {
    @Inject
    XmlService xmlService;

    @Post("/test-me")
    @Consumes("application/xml")
    @Produces(MediaType.APPLICATION_XML)
    public FileCustomizableResponseType extractXML(@Body byte[] xml) throws XMLStreamException, IOException {
        InputStream inpuStream = xmlService.createSumthing(xml);
        return new StreamedFile(inpuStream, MediaType.APPLICATION_XML_TYPE).attach("data.xml");
    }
}
