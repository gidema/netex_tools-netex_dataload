package nl.gertjanidema.netex.dataload;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;

@Component
public class FileProcessorFactory {

    @Inject
    private ObjectFactory<NetexFileProcessor> objectFactory;

    public NetexFileProcessor getInstance() {
        return objectFactory.getObject();
    }
}
