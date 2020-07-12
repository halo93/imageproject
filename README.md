# Instruction to make Imageproject up and running

1. Use Imageproject docker image:

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

   - To stop the container, execute the command below:
     `docker-compose -f src/main/docker/app-rds-cloud.yml down`

   \*\* We can track the processing status by using these command:

   ```
       docker ps -a (Then copy the docker container id of imageproject)
       docker logs --details -f DOCKER_CONTAINER_ID_IMAGE_PROJECT
   ```

2. Build and run Imageproject directly:

   _Prequesites:_

   - Install docker on local machine by following the instruction in this URL:
     - Mac: https://docs.docker.com/docker-for-mac/install/
     - Linux: https://docs.docker.com/engine/install/
     - Windows: https://docs.docker.com/docker-for-windows/install/
   - Install Java 8: https://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_HowTo.html
   - Install Maven 3.6.0: https://maven.apache.org/install.html
   - Create AWS credentials file:
     - Create a folder named `.aws` in the user root folder (i.e. mkdir ~/.aws)
     - Create a file named `config` and paste this content to the file:
       ```
        [default]
        region = us-west-1
       ```
     - Create a file named `credentials` and paste the below content to it:
       ```
        [default]
        aws_access_key_id = AKIAT67LMDPSOGCMNB4C
        aws_secret_access_key = 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
       ```

   _Steps:_

   - From console, change directory to project root folder and execute the command below to build and run the application:
     ```
     ./mvnw
     ```
   - To stop the application, terminate the console window or interrupt the command by using `ctrl + c` key combination

\*\* There is an option to build the imageproject docker image on your local, but it requires java and maven already installed on the machine. Steps:

```
    -- Build docker image:
    ./mvnw package -Pdev,webpack verify jib:dockerBuild

    -- Instantiate docker container:
        -- Using local database & ES:
           docker-compose -f src/main/docker/app-local.yml up -d
        -- Using AWS RDS database & AWS ES:
           docker-compose -f src/main/docker/app-rds-cloud.yml up -d
    -- Stop and remove the container:
        -- Using local database & ES:
           docker-compose -f src/main/docker/app-local.yml down
        -- Using AWS RDS database & AWS ES:
           docker-compose -f src/main/docker/app-rds-cloud.yml down
```
