FROM alpine:3.18
LABEL authors="jpuazaix"

ENV SPRING_DATASOURCE_URL="jdbc:mysql://database-1.c1do0euvzerj.ap-southeast-1.rds.amazonaws.com/poll_and_vote"
ENV SPRING_DATASOURCE_USERNAME="root"
ENV SPRING_DATASOURCE_PASSWORD="PJf0r3v3r!"

RUN apk add --no-cache openjdk17

WORKDIR /app

COPY target/poller-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 80

CMD ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=15s --retries=3 --start-period=10s --timeout=10s CMD wget --no-verbose --tries=1 --spider localhost:80/actuator/health || exit 1