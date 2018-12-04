# Get tomcat image with java 10
FROM tomcat:9.0.13-jre10
# Set WORKDIR to root
RUN mkdir /app/isaak/
WORKDIR /app/isaak/
# Copy target jar file into the image
COPY ./target/*.jar ./app.jar
# Run jar
CMD java -jar app.jar
