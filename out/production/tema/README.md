
# Homework 0 - GwenStone

## Popa Filip-Andrei ~ 323CA

---


## Project Overview
**GwenStone** is a Java-based project that simulates a turn-based card game experience inspired by the genre of collectible card games. This project leverages object-oriented programming principles to create a simple yet robust game environment where two players face off using unique decks and hero abilities.

The main goal of this project is to simulate a sequence of game events between two players. The game runs through a structured series of actions, with detailed tracking and logging of game statistics to enhance readability and enable potential future analysis.

## Core Features
- **Two-Player Simulation**: The game engine is built to handle a two-player environment, initializing players and setting up their decks and hero configurations based on input. This makes the game setup both dynamic and flexible.
- **Turn-Based Action Execution**: Once initialized, the game follows a turn-based system. Each player executes actions defined in the game flow, interacting with their cards and hero abilities.
- **Detailed Statistics and Output**: A dedicated `Statistics` class manages game metrics and captures each action in a structured log format. For every command executed, an `ObjectNode` is created to store and organize information, contributing to the output log for comprehensive tracking of each game action.

## Technical Implementation

The project centers around the following primary classes:

### Game Class
The `Game` class is the main driver of the HearthStone simulation. It initializes two `Player` objects, configures their decks, and sets up their hero attributes based on inputs provided.
The `Game` class also contains the `runGame()` function, which serves as the core loop of the game, handling the execution of player actions in a turn-based sequence.
This function also calls on the `Statistics` class to log game events.

### Player Class
Each player in the game is represented by an instance of the `Player` class. This class includes properties such as the player's deck and hero attributes, and methods for performing game actions. By utilizing separate `Player` instances, the game engine maintains a clean and encapsulated representation of each player’s state and actions.

### Statistics Class
The `Statistics` class is designed to track and log each command executed during gameplay. For every action taken by either player, this class creates an `ObjectNode` that captures details of the command. This logging mechanism is especially helpful for debugging, testing, and analyzing the game flow, providing structured output that can be used to review game events after completion.

## Project Structure
The following directory structure organizes the code for easy maintenance and readability:

```
src/
├── main/
│   ├── Main.java            # Given skel
│   ├── Test.java            # Given skel
│   ├── Game.java            # Main game logic
│   ├── Statistics.java      # Logging and statistics tracking
│   └── 
├── utils/
│   ├── Player.java          # Player representation and functionality
│   ├── Card.java            # Card description and actions
│   ├── Hero.java            # Extends Card - has an useAbility() specific to the 'Hero' cards
│   ├── Constants.java       # Constant error messages and codes
│   └── 
└──
```


---

