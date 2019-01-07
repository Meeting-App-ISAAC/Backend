FROM maven:3.3-jdk-8 as builder
COPY . backend
RUN cd backend && mvn package

# Get tomcat image with java 8
FROM java:8
# Copy target jar file into the image
COPY --from=builder ./backend/target/MeetingAppBackend-1.0-SNAPSHOT-jar-with-dependencies.jar  ./app.jar
# Run jar
CMD java -jar app.jar
