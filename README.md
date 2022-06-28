# languagetool-service
This package is a port of the [schreiben/node-languagetool](https://github.com/schreiben/node-languagetool) to the rust language.


This package is a rust binding to the [LanguageTool](https://languagetool.org) spellchecker. It is based on a local instance of LanguageTool. As it is a Java based framework, this binding will use [nullishamy/kate](https://github.com/nullishamy/kate), which provides an embedded local Java Runtime Engine.

## Project stage
Java part of the project is ready, rust part needs a functionality to ready and run the .jar file. Its functionality will be provided by kate in the [future](https://github.com/nullishamy/kate/issues/13). Therefore, current library development is frozen.

## License
Project is under LGPL 3 license
