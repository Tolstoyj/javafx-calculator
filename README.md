# JavaFX Scientific Calculator

A modern, feature-rich scientific calculator built with JavaFX. This calculator provides a beautiful user interface with both light and dark themes, and includes advanced mathematical operations.

![Calculator Screenshot](docs/screenshot.png)

## Features

- Basic arithmetic operations (+, -, ×, ÷)
- Scientific functions (sin, cos, tan, log, ln, √, etc.)
- Memory operations (MC, MR, M+, M-, MS)
- History tracking
- Dark/Light theme toggle
- Copy to clipboard functionality
- Professional installers for Mac and Windows

## Requirements

- Java 23 or later
- Maven 3.8.0 or later

## Building from Source

1. Clone the repository:
```bash
git clone https://github.com/yourusername/javafx-calculator.git
cd javafx-calculator
```

2. Build the project:
```bash
mvn clean package
```

3. Run the application:
```bash
mvn javafx:run
```

## Creating Installers

### Mac Installer (DMG)
```bash
mvn clean javafx:jlink
jpackage --name "JavaFX Calculator" --app-version 1.0.0 --input target --main-jar javafx-calculator-1.0-SNAPSHOT.jar --main-class com.calculator.App --type dmg --dest target/installer --mac-package-identifier com.calculator --mac-package-name "JavaFX Calculator" --vendor "Calculator Inc." --copyright "Copyright 2024" --runtime-image target/calculator
```

### Windows Installer (MSI/EXE)
```bash
mvn clean javafx:jlink
jpackage --name "JavaFX Calculator" --app-version 1.0.0 --input target --main-jar javafx-calculator-1.0-SNAPSHOT.jar --main-class com.calculator.App --type exe --dest target/installer --win-menu --win-shortcut --win-dir-chooser --vendor "Calculator Inc." --copyright "Copyright 2024" --runtime-image target/calculator
```

## Running Tests

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- JavaFX community
- Maven build tools
- All contributors who help improve this calculator 