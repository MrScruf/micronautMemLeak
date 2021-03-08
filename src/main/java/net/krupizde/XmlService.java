package net.krupizde;

import javax.inject.Singleton;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Singleton
public class XmlService {

    public InputStream createSumthing(byte[] xml) throws XMLStreamException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter writer = outputFactory.createXMLEventWriter(out, StandardCharsets.UTF_8.name());
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        writer.add(eventFactory.createStartElement("", "", "root"));
        for (int i = 0; i < 1000; i++) {
            XMLEventReader reader =
                    xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(xml), StandardCharsets.UTF_8.name());
            writer.add(reader);
        }
        writer.close();
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
