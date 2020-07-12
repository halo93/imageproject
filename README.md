# Imageproject execution instruction

_Prequesites:_

- Install docker on local machine by following the instruction in this URL:
  - Mac: https://docs.docker.com/docker-for-mac/install/
  - Linux: https://docs.docker.com/engine/install/
  - Windows: https://docs.docker.com/docker-for-windows/install/

_Steps:_

- From console, execute the command below to build a docker image for Imageproject:

  ```
  ./mvnw package -Pdev verify jib:dockerBuild
  ```

- After the step to create a docker image has done, there are two options to deploy a container for it:

  - Deploy using local db (to support development phase)
    `docker-compose -f src/main/docker/app-local.yml up -d`
  - Deploy using aws rds db (to support release phase)
    `docker-compose -f src/main/docker/app-rds-cloud.yml up -d`

\*\* We can track the processing status by using these command:

```
    docker ps -a (Then copy the docker container id of imageproject)
    docker logs --details -f DOCKER_CONTAINER_ID_IMAGE_PROJECT
```
