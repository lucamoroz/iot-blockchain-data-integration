
## Build
- With docker:
`sudo docker build -t tuwien/iot .`
- With Maven:
`sudo ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=tuwien/iot`

## Run
`sudo docker run -p 8080:8080 tuwien/iot`

## Configuration
Configure the broker address, and the topic names at: dataFilter/src/main/resources/application.properties

## Notes
- Allow IDEA IDE to process lombok annotations by install plugin lombok.