# CVITFusion
Connected Vehicle and Intelligent Transportation Fusion.  Consisting of 4 components:
- Client: JavaFX based GUI for configuration and monitoring of Engines
- Engine: Edge processing sevice for ITS and CV applications
- LicenseUtil: Turns the license ID of the engine into license keys with enabled features
- Common: library of common code

## Development tools
- Eclipse IDE and plugins (https://download.eclipse.org/efxclipse/updates-released/3.8.0/site/)
- Github Desktop
- SceneBuilder
- JavaFX
- JDK 18
- remarkable

## Development references
- https://controlsfx.github.io/
- https://openjfx.io/openjfx-docs/
- https://www.oracle.com/java/technologies/downloads/

## Running the client app
Include these into the "VM Arguments"
> --module-path "/usr/lib/jvm/javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml

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
> sudo apt install docker.io

> sudo docker pull eclipse-mosquitto

> sudo docker run -it -p 1883:1883 -p 9001:9001 eclipse-mosquitto

