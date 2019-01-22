FROM maven:3.3-jdk-8 as builder
COPY . backend
RUN cd backend && mvn package -DskipTests

# Get tomcat image with java 8
FROM java:8
# Copy target jar file into the image
COPY --from=builder ./backend/target/Meetgit singAppBackend-1.0-SNAPSHOT-jar-with-dependencies.jar  ./app.jar
# Run jar
COPY src/resources .

ENV TZ=Europe/Amsterdam
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

CMD java -jar app.jar
