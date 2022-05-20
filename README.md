# CVITFusion
Connected Vehicle and Intelligent Transportation Fusion.  Consisting of 3 components:  Client, Server, and Common

## tools
- Eclipse IDE
  - Plugins https://download.eclipse.org/efxclipse/updates-released/3.8.0/site/
- Github Desktop
- SceneBuilder
- JavaFX
- JDK 18

## references

https://controlsfx.github.io/

https://openjfx.io/openjfx-docs/

https://www.oracle.com/java/technologies/downloads/

## running the client app
Include these in the "VM Arguments"
> --module-path "/usr/lib/jvm/javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml

## topic schema
apextalos/cvitfusion/{message}/{engine guid}

Example:
 -> apextalos/cvitfusion/requestconfig/32143245325
 <- apextalos/cvitfusion/responseconfig/32143245325
  
## message schema
message
 -version
 -map<string, string> parameters
 -object data


