FROM java:8
MAINTAINER Lionel Burgbacher <lionel.burgbacher@heig-vd.ch>

COPY TcpServer-1.0-SNAPSHOT-standalone.jar /opt/app/server.jar

EXPOSE 2424

CMD ["java", "-jar", "/opt/app/server.jar"]
