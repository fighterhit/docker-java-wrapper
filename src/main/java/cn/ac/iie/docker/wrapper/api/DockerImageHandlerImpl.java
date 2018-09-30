package cn.ac.iie.docker.wrapper.api;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author Fighter Created on 2018/9/29.
 */
public class DockerImageHandlerImpl implements DockerImageHandler {
    private static Logger logger = LoggerFactory.getLogger(DockerImageHandlerImpl.class);

    private DockerClient dockerClient;

    public DockerImageHandlerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public String build(String dockerBaseDirPath, String dockerFilePath) {
        File baseDirectory = new File(dockerFilePath);
        File dockerFile = new File(baseDirectory, dockerFilePath);

        String res = dockerClient
                .buildImageCmd(baseDirectory)
                .withDockerfile(dockerFile)
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        logger.debug("Build an image in + " + dockerBaseDirPath + " with Dockerfile " + dockerFilePath + " successfully!");
        return res;
    }

    @Override
    public void pull(String repo, String tag) {
        dockerClient.pullImageCmd(repo).withTag(tag).exec(new MyPullImageResultCallback()).awaitSuccess();
        logger.debug("Pull an image from a repo: " + repo + "with tag " + tag + " successfully!");
    }


    @Override
    public void push(String imageNameAndTag) {
        //define your own auth config
        AuthConfig authConfig = new AuthConfig();
        dockerClient
                .pushImageCmd(imageNameAndTag)
//                .withAuthConfig(authConfig)
                .exec(new MyPushImageResultCallback())
                .awaitSuccess();
        logger.debug("Push an image " + imageNameAndTag + " successfully!");
    }

    @Override
    public void tag(String oldImageNameAndTag, String newImageName, String newTag) {
        dockerClient.tagImageCmd(oldImageNameAndTag, newImageName, newTag).exec();
        logger.debug("Tag image " + oldImageNameAndTag + " with new tag " + newTag + " successfully!");
    }

    @Override
    public List<Image> getImages() {
        List<Image> images = dockerClient.listImagesCmd().exec();
        logger.debug("List images successfully!");
        return images;
    }


    static class MyBuildImageResultCallback extends BuildImageResultCallback {
        @Override
        public void onNext(BuildResponseItem item) {
            logger.debug("id:" + item.getId() + " status: " + item.getStatus());
            super.onNext(item);
        }
        @Override
        public void onComplete() {
            logger.debug("completed!");
            super.onComplete();
        }
    }

    static class MyPullImageResultCallback extends PullImageResultCallback {
        @Override
        public void onNext(PullResponseItem item) {
            logger.debug("id:" + item.getId() + " status: " + item.getStatus());
            super.onNext(item);
        }
        @Override
        public void onComplete() {
            logger.debug("completed!");
            super.onComplete();
        }
    }

    static class MyPushImageResultCallback extends PushImageResultCallback {
        @Override
        public void onNext(PushResponseItem item) {
            logger.debug("id:" + item.getId() + " status: " + item.getStatus());
            super.onNext(item);
        }
    }
}
