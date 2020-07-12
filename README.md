# Imageproject execution instruction

_Prequesites:_

- Install docker on local machine by following the instruction in this URL:
  - Mac: https://docs.docker.com/docker-for-mac/install/
  - Linux: https://docs.docker.com/engine/install/
  - Windows: https://docs.docker.com/docker-for-windows/install/

_Steps:_

- From console, execute the command below to pull the docker image for Imageproject:

  ```
  docker pull halo93/imageproject:latest
  ```

- After the step to pull the docker image has done, access to the imageproject root folder and execute the below command to instantiate a container:

  `docker-compose -f src/main/docker/app-rds-cloud.yml up -d`

\*\* We can track the processing status by using these command:

```
    docker ps -a (Then copy the docker container id of imageproject)
    docker logs --details -f DOCKER_CONTAINER_ID_IMAGE_PROJECT
```

\*\* There is an option to build the imageproject docker image on your local, but it requires java and maven already installed on the machine. Steps:

```
    -- Build docker image:
    ./mvnw package -Pdev verify jib:dockerBuild

    -- Instantiate docker container:
        -- Using local database:
           docker-compose -f src/main/docker/app-local.yml up -d
```
