The `PathFinder` program is a JavaFX-based application designed to create, manage, and visualize maps with places and connections between them. 
The program allows users to add new places, create connections, and find paths between locations on a graphical interface. 

This program was done as a assignment for a Java/JavaFX course.

To "run" the program you have to run the PathFinder.java-file.

Here’s a summary of its key features and functionality:

### Key Features:
1. **Map Creation and Management**  
   - Users can create new maps, open existing ones, and save maps to files.  
   - Maps can be saved as images (`png` format).  

2. **Place Management**  
   - Users can add new places by clicking on the map. Each place can be given a name, and the program visually represents the location.  
   - Places can be selected, and connections between them can be established.  

3. **Connection Management**  
   - Connections between places represent paths or routes. Users can add new connections or modify existing ones by selecting two places.  
   - Each connection has a name and a weight (likely representing distance or time).  

4. **Pathfinding**  
   - The program can calculate paths between selected places. If a path exists, the program displays the route and total weight (cost or distance).  
   - If no path is found, an error message is shown.  

5. **Graph Visualization**  
   - The application visually represents places and connections as nodes and edges on the map.  
   - The interface allows users to interact with the graph by selecting, modifying, or adding new nodes and edges.  

6. **User Interface**  
   - The program features a graphical interface built with JavaFX, including buttons for key operations (`Find Path`, `Show Connection`, `New Place`, `New Connection`, `Change Connection`).  
   - A menu bar provides options for file operations (`New Map`, `Open`, `Save`, `Save Image`, `Exit`).  

7. **Event Handling and Alerts**  
   - The program uses event handlers to manage user interactions, such as adding places or connections and saving files.  
   - It features alert dialogs for errors, confirmations, and warnings (e.g., unsaved changes, invalid inputs).  

### Technical Details:
- **Graph Structure**: The program uses a `ListGraph` class to represent the graph of places and connections. This structure stores nodes (`Place`) and edges (`Edge<Place>`).  
- **Persistence**: Maps are saved to a `.graph` file format that stores place coordinates and connections.  
- **Interactive Elements**: Places are draggable and selectable, and connections are visualized with lines on the map.  
- **Data Storage**: The program saves map data, including places, connections, and associated metadata, to files.  
- **Error Handling**: Users receive visual feedback and alerts when operations fail (e.g., no path between places, invalid names, missing selections).  

### Purpose:
The `PathFinder` program serves as an interactive tool for planning and managing routes or networks on a map. It can be useful for educational purposes, logistics planning, or visualization of transport networks. The intuitive interface and graphical representation make it accessible for users without programming knowledge.
