package nl.gertjanidema.netex.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlRootElement;

public class XmlFileReader<T> {

    public List<T> readFile(File netexFile, String containerElement, Class<T> targetClass) {
        var xmlRootAnnotation = targetClass.getAnnotation(XmlRootElement.class);
        var elementName = xmlRootAnnotation.name();
        XMLInputFactory xif = XMLInputFactory.newFactory();
        try (
            var is = new FileInputStream(netexFile);
            var xis = netexFile.getName().endsWith(".gz") ? new GZIPInputStream(is) : is;
        ) {
            var results = new LinkedList<T>();
            XMLStreamReader xsr = xif.createXMLStreamReader(xis);
            var context = JAXBContext.newInstance(targetClass);
            var unmarshaller = context.createUnmarshaller();
            xsr.nextTag();
            while(!xsr.getLocalName().equals(containerElement)) {
                xsr.nextTag();
            }
            xsr.nextTag();
            while(xsr.getLocalName().equals(elementName)) {
                var object = targetClass.cast(unmarshaller.unmarshal(xsr));
                results.add(object);
                xsr.nextTag();
            }
            xsr.close();
            return results;
        } catch (JAXBException | XMLStreamException | IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

}
