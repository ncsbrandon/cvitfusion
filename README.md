# CVITFusion
Connected Vehicle and Intelligent Transportation Fusion.  Consisting of 4 components:
- Client: JavaFX based GUI for configuration and monitoring of Engines
- Engine: Edge processing sevice for ITS and CV applications
- LicenseUtil: Turns the license ID of the engine into license keys with enabled features
- Common: library of common code

## Building the jars with dependencies
For each project:
> mvn clean compile assembly:single

## Running the client app
> java -cp target/CVITFusionClient-0.0.1-SNAPSHOT-jar-with-dependencies.jar --module-path "/usr/lib/jvm/javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml com.apextalos.cvitfusion.client.app.Main

## Running the engine app
> java -cp target/cvitfusionengine-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.apextalos.cvitfusionengine.app.App

## Running the licensing app
java -cp target/cvitfusionlicenseutil-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.apextalos.cvitfusionlicenseutil.app.App

## Development tools
- Eclipse IDE (https://download.eclipse.org/efxclipse/updates-released/3.8.0/site/)
  - Sonarlint
- Github Desktop
- SceneBuilder
- JavaFX (https://gluonhq.com/products/javafx/) deployed to /usr/lib/jvm
- JDK 11 or greater (sudo apt install default-jdk)
- remarkable

## Development references
- https://controlsfx.github.io/
- https://openjfx.io/openjfx-docs/
- https://www.oracle.com/java/technologies/downloads/


## topic schema
> apextalos/cvitfusion/{message}/{engine guid}

Message can be one of:
```
requestconfig
responseconfig
```

### Example:
Client request
> apextalos/cvitfusion/requestconfig/32143245325

Engine response
> apextalos/cvitfusion/responseconfig/32143245325

## message schema
- message
  - version
  - map<string, string> parameters
  - object data

## mosquitto docker
> apt install docker.io
> 
> docker pull eclipse-mosquitto
> 
> docker run -it -p 1883:1883 -p 9001:9001 -v /root/mosquitto/config/mosquitto.conf:/mosquitto/config/mosquitto.conf eclipse-mosquitto
> 
> mosquitto_pub -h localhost -p 1883 -t /test -m 'hello'
> 
> mosquitto_sub -h localhost -p 1883 -t /#

