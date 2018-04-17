FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD tao-api-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
RUN sh -c 'mkdir /config'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "-Dspring.config.location=file:/config/"]