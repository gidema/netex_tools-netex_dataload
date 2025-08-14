package nl.gertjanidema.netex.dataload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.gertjanidema.netex.dataload" },
excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.gertjanidema.netex.dataload.jobs.*")})
@EnableJpaRepositories(basePackages = "nl.gertjanidema.netex.dataload.dto")
@EntityScan("nl.gertjanidema.netex.dataload.dto")
public class NetexDataloadCommand implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(NetexDataloadCommand.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        new SpringApplicationBuilder(NetexDataloadCommand.class)
            .web(WebApplicationType.NONE)
            .run(args);
        LOG.info("APPLICATION FINISHED");
    }
    
    @Override
    public void run(String... args) {
        dataload().run();
    }
    
    @SuppressWarnings("static-method")
    @Bean
    NetexDataload dataload() {
        return new NetexDataload();
    }

}