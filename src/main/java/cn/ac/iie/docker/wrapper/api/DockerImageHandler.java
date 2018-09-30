package cn.ac.iie.docker.wrapper.api;

import com.github.dockerjava.api.model.Image;

import java.util.List;

/**
 * @author Fighter Created on 2018/9/29.
 */
public interface DockerImageHandler {
    String build(String dockerBaseDirPath, String dockerFilePath);
    void pull(String repo, String tag);
    void tag(String image, String repo, String tag);
    void push(String imageNameAndTag);
    List<Image> getImages();

}
