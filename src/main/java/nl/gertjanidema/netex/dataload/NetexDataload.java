package nl.gertjanidema.netex.dataload;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
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
    @Inject JobRegistry jobRegistry;
    @Inject JobLauncher jobLauncher;
    
    public void run(boolean refreshFiles) {
        if (refreshFiles) {
            try {
                var newNetexFiles = ndovService.checkForNewNetexFiles();
                // Cache the requested netex files
                var files = ndovService.downloadNetexFiles(newNetexFiles);
                files.forEach(file -> {
                    if (file.getFileSetId().toLowerCase().contains("vehicles")) {
                        LOG.info("Ignoring file {}.", file.getFileName());
                    }
                    else {
                        LOG.info("Processing file {}.", file.getFileName());
                       processFile(file);
                    }
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            var parameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
            var job = jobRegistry.getJob("netexEtlUpdateJob");
            jobLauncher.run(job, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
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