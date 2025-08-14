package nl.gertjanidema.netex.dataload;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.NetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.StNetexDeliveryRepository;
import nl.gertjanidema.netex.dataload.ndov.NdovService;

@Component
public class NetexDataload {

    private static Logger LOG = LoggerFactory.getLogger(NetexDataload.class);

    @Inject NdovService ndovService;
    @Inject StNetexDeliveryRepository deliveryRepository;
    @Inject FileProcessorFactory fileProcessorFactory;
    
    public void run() {
        try {
            var newNetexFiles = ndovService.checkForNewNetexFiles();
            // Cache the requested netex files
            var files = ndovService.downloadNetexFiles(newNetexFiles);
            files.forEach(file -> {
                LOG.info("Processing file {}.", file.getFileName());
                processFile(file);
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Transactional
    private void processFile(NetexFileInfo fileInfo) {
        var fileProcessor = fileProcessorFactory.getInstance();
        var newDelivery = fileProcessor.processHeader(fileInfo);
        var existingDelivery = deliveryRepository.findById(newDelivery.getFileSetId());
        if(existingDelivery.isEmpty() || 
            newDelivery.getFilename().compareTo(existingDelivery.get().getFilename()) > 0) {
            fileProcessor.processData();
            deliveryRepository.save(newDelivery);
        }
    }
}