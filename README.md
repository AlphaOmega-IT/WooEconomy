# WooEconomy

WooEconomy is a plugin that provides an API for interacting with economy systems in Minecraft plugins. This repository contains the source code and documentation for the WooEconomy API.

## Installation

To use the WooEconomy API in your project, follow these steps:

1. **Fork the Project**: Fork the WooEconomy repository to your own GitHub account.

2. **Install as Maven Project**:
   - Add the following dependency to your Maven `pom.xml` file:
     ```xml
     <dependency>
         <groupId>de.alphaomega-it.wooeconomy</groupId>
         <artifactId>WooEconomy</artifactId>
         <version>1.0</version>
         <scope>provided</scope>
     </dependency>
     ```

3. **Declare Variable in Main Class**:
   - Declare a variable in your main class:
     ```java
     private EconomyProvider economyProvider;
     ```

4. **Initialize in `onEnable` Method**:
   - Initialize the `EconomyProvider` in your `onEnable` method:
     ```java
     this.economyProvider = new EconomyProvider();
     ```

5. **Accessing the Economy Provider**:
   - Use a getter method to access the `EconomyProvider` instance.

6. **Create Utility Class `EconomyProvider`**:
   - Create a utility class named `EconomyProvider` with the provided code.

7. **Using `EconomyProvider`**:
   - Use the `EconomyProvider` class to interact with the economy system. An example usage is provided below:
     ```java
     if (this.javaplugin.getEconomyProvider().hasEconomyProvider()) {
         this.javaplugin.getEconomyProvider().get().createPlayerAccount(player, 500.00);
     }
     ```

8. **Put WooEconomy JAR into Your Plugin Folder**:
   - After building the project successfully, put the WooEconomy JAR file into your plugin folder.

## Contributing

Contributions are welcome! If you encounter any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

If you have any more questions in the future or need further assistance, I'll be glad to help. Feel free to join the Discord server at https://dsc.gg/atwoo. See you there!
