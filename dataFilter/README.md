
## Build
- With docker:
`mvn -f pom.xml package && sudo docker build -t tuwien/iot .`
- With Maven:
`sudo ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=tuwien/iot`

## Run
`sudo docker run -p 8080:8080 tuwien/iot`

## Notes
- Allow IDEA IDE to process lombok annotations by install plugin lombok.