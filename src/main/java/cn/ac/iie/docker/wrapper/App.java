package cn.ac.iie.docker.wrapper;

import cn.ac.iie.docker.wrapper.api.DockerImageHandlerImpl;
import com.github.dockerjava.api.model.Image;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static cn.ac.iie.docker.wrapper.conf.DockerConfig.getDockerClient;

/**
 * @author Fighter Created on 2018/9/29.
 */
@SpringBootApplication
public class App {
    private static final String PROPERTIES_PATH;
    public static FileBasedConfiguration conf;

    static {
        PROPERTIES_PATH = ClassLoader
                .getSystemClassLoader()
                .getResource("docker.properties")
                .getFile();
        if (PROPERTIES_PATH == null || PROPERTIES_PATH.isEmpty()) {
            throw new RuntimeException("properties path is empty!");
        } else {
            System.err.printf("PROPERTIES_PATH = {}", PROPERTIES_PATH);
        }

        try {
            conf = initConfiguration();
        } catch (Exception e) {
            System.err.printf("init conf error: {}", ExceptionUtils.getFullStackTrace(e));
            System.exit(1);
        }
    }

    private static FileBasedConfiguration initConfiguration() throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> confBuilder
                = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                        .setFileName(PROPERTIES_PATH)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                        .setThrowExceptionOnMissing(true));
        return confBuilder.getConfiguration();
    }

    public static void main(String[] args) {
        DockerImageHandlerImpl dockerImageController = new DockerImageHandlerImpl(getDockerClient());
        for (Image image : dockerImageController.getImages()) {
            System.out.println("Image name " + Arrays.toString(image.getRepoTags()) + " Image id : " + image.getId() + " ; size: " + image.getSize() + " B");
        }
    }
}
