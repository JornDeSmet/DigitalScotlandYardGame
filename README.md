# Scotland Yard (Real-Time Board Game)


![github-header-image (5)](https://github.com/user-attachments/assets/cf919e79-163b-48ad-8d59-957cb2b25bb1)



![Java](https://img.shields.io/badge/Java-23-orange?style=for-the-badge&logo=java&logoColor=white&labelColor=000000)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.0-brightgreen?style=for-the-badge&logo=spring&logoColor=white&labelColor=000000)
![WebSockets](https://img.shields.io/badge/WebSockets-Enabled-blueviolet?style=for-the-badge&logo=websocket&logoColor=white&labelColor=000000)
![STOMP](https://img.shields.io/badge/STOMP%20Protocol-Supported-blue?style=for-the-badge&logo=opensourceinitiative&logoColor=white&labelColor=000000)
![Git](https://img.shields.io/badge/Git-gray?style=for-the-badge&logo=git&logoColor=white&labelColor=000000)

---

## 🧩 Intro
This project is a real-time, multi-player version of the **Scotland Yard board game**, where players take on the roles of detectives or the suspect. Built with **Java**, **Spring Boot**, **WebSockets**, and **JavaScript**, the game features real-time updates and turn-based gameplay. Players can join a game, make moves, and see updates instantly on a dynamic web interface.

**Core Features**:
- Turn-based gameplay with real-time updates.
- Suspect's location is revealed periodically for strategic depth.
- Fully interactive game board rendered in the browser.

---
## 📂 File Structure

- **`src/main/java`**: Backend code, including models, controllers, and services.
- **`src/main/resources`**: Frontend files (HTML, CSS, JS).
- **`pom.xml`**: Maven build configuration and dependencies.

---

## 🚀 Installation
### Prerequisites
Ensure the following software is installed:
- **Java JDK 23**
- **Maven 4.0.0** for building the project.
- **GIT** to clone this project.

### Steps to Install and Run
1. Clone the repository:
   ```bash
   git clone https://github.com/JornDeSmet/DigitalScotlandYardGame.git
2. navigate to the project directory
   ```bash
   cd <DigitalScotlandYardGame>
4. Import the Project into Your IDE:
   - If you're using IntelliJ IDEA, open the project directory directly.
   - For other IDEs, ensure proper support for Maven projects.
5. Build and Run the Application:
    -Locate and execute the **`ScotLandYardGamesApplication.java`** file. You can do this directly in your IDE by running the main() method of this class.
6. To enter a game go to a webbrowser and type:
    ```bash
    http://localhost:8080/index
    
  Note: Since this is a local multiplayer game, you'll need to open three browser tabs to start a full game with enough players.
   
## 🛠️ Technology Stack

### Backend:
- **Spring Boot**: Rapid application setup and WebSocket integration.
- **WebSockets + STOMP**: Enables two-way real-time communication with structured message routing.
- **Java**: Handles the game logic, turn management, and state updates.

### Frontend:
- **JavaScript**: Interactive game board and communication with the backend.
- **HTML & CSS**: User-friendly UI for gameplay.
- **STOMP.js**: Manages WebSocket communication in the browser.

---

## 🎮 Gameplay

- **Turn-Based Logic**: Each player gets a turn to make a move, with real-time updates for all other players.
- **Revealing Suspect**: Suspect's location is revealed at specific intervals, creating suspense and strategy.
- **Winning Conditions**:
  - Detectives win if they catch Mr. X within 30 rounds.
  - Suspect wins if he evades the detectives for 30 rounds.

---

## 🌟 Features & Highlights

### Real-Time Communication:
- WebSockets enable instant updates, so all players stay in sync without page reloads.

### Dynamic Gameplay:
- Detectives and Suspect have different strategies.  
- Suspect's location is hidden except during specific rounds.

### Interactive Frontend:
- Dynamic game board that updates as moves are made.  
- Visual indicators for player roles and locations.

### Configurable Backend:
- The game can be easily extended with more features or different rules.

---

## 📜 Licence
This project is for educational purposes only. Feel free to explore, learn, and adapt it for your own projects!

## Contact
For support or questions Email: jorn.de.smet@student.ehb.be
