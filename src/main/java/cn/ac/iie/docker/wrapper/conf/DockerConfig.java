package cn.ac.iie.docker.wrapper.conf;

import cn.ac.iie.docker.wrapper.App;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

/**
 * @author Fighter Created on 2018/9/29.
 */
public class DockerConfig {

    private static DockerCmdExecFactory dockerCmdExecFactory() {
        return new JerseyDockerCmdExecFactory()
                .withReadTimeout(App.conf.getInt(Constants.DOCKER_READ_TIMEOUT))
                .withConnectTimeout(App.conf.getInt(Constants.DOCKER_CONNECT_TIMEOUT))
                .withMaxTotalConnections(App.conf.getInt(Constants.DOCKER_MAX_TOTAL_CONNECTIONS))
                .withMaxPerRouteConnections(App.conf.getInt(Constants.DOCKER_MAX_PER_ROUTE_CONNNECTIONS));
    }

    /**
     * create a DockerClientConfig
     *
     * @return
     */
    private static DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(App.conf.getString(Constants.DOCKER_HOST))
                .withDockerTlsVerify(App.conf.getBoolean(Constants.DOCKER_TLS_VERIFY))
                .withRegistryUrl(App.conf.getString(Constants.REGISTRY_URL))
                .withRegistryUsername(App.conf.getString(Constants.REGISTRY_USERNAME))
                .withRegistryPassword(App.conf.getString(Constants.REGISTRY_PASSWORD))
                .build();
    }

    /**
     * create a DockerClient
     */
    public static DockerClient getDockerClient() {
        return DockerClientBuilder
                .getInstance(dockerClientConfig())
                .withDockerCmdExecFactory(dockerCmdExecFactory())
                .build();
    }

}
