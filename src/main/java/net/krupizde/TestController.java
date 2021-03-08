package net.krupizde;

import io.micronaut.http.FullHttpRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.netty.NettyHttpRequest;
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

    /**
     * Řešení problémů s memory leaky, kdy Micronaut z nějakého důvodu nevolal release na ByteBuf, který získával od Netty
     * jakožto tělo requestu. 'Fix' vytvořen na základě
     * https://github.com/micronaut-projects/micronaut-core/issues/2665
     * a prostudování fixů, které se na toto issue pokoušeli vytvořit micronaut vývojáři.
     * <p>
     * <p>
     * !!POZOR!!
     * pokud budeme povyšovat micronaut na vyšší verze je možné, že tento 'fix' bude způsobovat
     * neočekávané chování.
     * !!POZOR!!
     *
     * @param request Request, jehož zdroje chceme uvolnit.
     */
    private void releaseRequestResources(HttpRequest<byte[]> request) {
        ((NettyHttpRequest<byte[]>) ((FullHttpRequest<byte[]>) request).getDelegate()).release();
    }

    @Post("/test-me-fixed")
    @Consumes("application/xml")
    @Produces(MediaType.APPLICATION_XML)
    public FileCustomizableResponseType extractXMLFixed(HttpRequest<byte[]> request) throws Exception {
        if (request.getBody().isEmpty()) {
            throw new Exception("No body specified");
        }
        InputStream inpuStream = xmlService.createSumthing(request.getBody().get());
        releaseRequestResources(request);
        return new StreamedFile(inpuStream, MediaType.APPLICATION_XML_TYPE).attach("data.xml");
    }

    @Post("/test-me")
    @Consumes("application/xml")
    @Produces(MediaType.APPLICATION_XML)
    public FileCustomizableResponseType extractXML(@Body byte[] xml) throws XMLStreamException, IOException {
        InputStream inpuStream = xmlService.createSumthing(xml);
        return new StreamedFile(inpuStream, MediaType.APPLICATION_XML_TYPE).attach("data.xml");
    }
}
