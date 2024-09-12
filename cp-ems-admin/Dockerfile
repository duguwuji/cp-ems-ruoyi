FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER cp_energy

RUN mkdir -p /cp-ems/server/logs \
    /cp-ems/server/temp \
    /cp-ems/skywalking/agent

WORKDIR /cp-ems/server

ENV SERVER_PORT=8093

EXPOSE ${SERVER_PORT}

ADD ./target/cp-ems-admin.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
            # 应用名称 如果想区分集群节点监控 改成不同的名称即可
#            "-Dskywalking.agent.service_name=cpems-server", \
#            "-javaagent:/cpems/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
