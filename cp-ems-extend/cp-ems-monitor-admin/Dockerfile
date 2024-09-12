FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER cpems

RUN mkdir -p /cp-portal/monitor/logs

WORKDIR /cp-portal/monitor

EXPOSE 9090

ADD ./target/cp-portal-monitor-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
