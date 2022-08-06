FROM java:8
MAINTAINER lain<kamille606@163.com>

WORKDIR /opt
VOLUME /tmp

ADD learn-docker-1.0-SNAPSHOT.jar /opt/user-docker.jar
RUN bash -c 'touch /user-docker.jar'
ENTRYPOINT ["java","-jar","user-docker.jar"]

EXPOSE 8080