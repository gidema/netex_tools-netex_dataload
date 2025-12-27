package nl.gertjanidema.netex.cli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import nl.gertjanidema.netex.dataload.NetexDataload;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.gertjanidema.netex.dataload", "nl.gertjanidema.netex.dataload.services"})
@EnableJpaRepositories(basePackages = {"nl.gertjanidema.netex.dataload.dto", "nl.gertjanidema.netex.dto"})
@EntityScan({"nl.gertjanidema.netex.dataload.dto", "nl.gertjanidema.netex.dto"})
public class CmdLine implements CommandLineRunner, ExitCodeGenerator {

    private static Logger LOG = LoggerFactory.getLogger(CmdLine.class);

    private final IFactory factory;
    private final NetexDataload dataload;
    private int exitCode;

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        System.exit(SpringApplication.exit(SpringApplication.run(CmdLine.class, args)));
    }

    public CmdLine(IFactory factory, NetexDataload dataload) {
        super();
        this.factory = factory;
        this.dataload = dataload;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(dataload, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}