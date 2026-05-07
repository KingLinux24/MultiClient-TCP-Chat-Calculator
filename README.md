# Multi-Client TCP Chat Application with Calculator

A Java Socket Programming project implementing a multi-client TCP-based chat application with integrated calculator functionality.

## Features

✅ **Multi-Client Support**: Server handles multiple clients simultaneously using threads
✅ **Message Broadcasting**: Server broadcasts messages to all connected clients
✅ **Active Client Management**: Maintains a list of active clients
✅ **Graceful Disconnection**: Handles client disconnection elegantly
✅ **Integrated Calculator**: Clients can perform calculations on the server
✅ **Thread Synchronization**: Uses synchronized collections for thread safety
✅ **Exception Handling**: Comprehensive error handling and input validation
✅ **System Messages**: Notifies all clients when someone joins/leaves

## Architecture

### Components

1. **Server.java**
   - Listens for incoming client connections on port 5000
   - Maintains a synchronized set of all connected clients
   - Broadcasts messages to all connected clients
   - Manages client count

2. **ClientHandler.java**
   - Implements Runnable to handle each client in a separate thread
   - Reads messages from the client
   - Parses calculator requests and regular chat messages
   - Broadcasts messages to other clients
   - Implements graceful disconnection

3. **Client.java**
   - Connects to the server
   - Sends chat messages and calculator requests
   - Listens for incoming messages from the server
   - Provides user-friendly interface

4. **Calculator.java**
   - Performs arithmetic operations: +, -, *, /, %
   - Validates input and handles edge cases (division by zero)
   - Provides clear exception messages

## Compilation

```bash
# Navigate to the project directory
cd c:\Users\Linux\Downloads\AJP

# Compile all Java files
javac Server.java ClientHandler.java Calculator.java Client.java
```

## Execution

### Terminal 1 - Start the Server

```bash
java Server
```

Output:
```
Server started on port 5000
Waiting for client connections...
```

### Terminal 2 - Start First Client

```bash
java Client
```

Input your name when prompted:
```
Enter your name: Alice
Connected to server!
Commands:
  - Type messages to chat
  - Use 'CALC:num1:num2:operation' for calculator
    Example: CALC:10:5:+ or CALC:20:3:/ or CALC:25:4:%
  - Type 'exit' to disconnect

Alice > 
```

### Terminal 3 - Start Second Client

```bash
java Client
```

Input your name:
```
Enter your name: Bob
Connected to server!
...
Bob > 
```

## Usage Examples

### Chat Messages

```
Alice > Hello everyone!
(Bob sees: [Alice]: Hello everyone!)

Bob > Hi Alice!
(Alice sees: [Bob]: Hi Alice!)
```

### Calculator Usage

Format: `CALC:num1:num2:operation`

Operations supported: `+`, `-`, `*`, `/`, `%`

```
Alice > CALC:10:5:+
(Alice receives: [CALCULATOR] 10 + 5 = 15.0)

Bob > CALC:20:3:/
(Bob receives: [CALCULATOR] 20 / 3 = 6.666666666666667)

Charlie > CALC:25:4:%
(Charlie receives: [CALCULATOR] 25 % 4 = 1.0)
```

### System Messages

When a client joins or leaves:
```
[SYSTEM]: Alice joined the chat
[SYSTEM]: Bob left the chat
```

## Key Features Implementation

### Thread Synchronization
- Uses `Collections.synchronizedSet()` for thread-safe client management
- Multiple clients can connect simultaneously
- Each client is handled in its own thread

### Exception Handling
- `IOException` for network errors
- `NumberFormatException` for invalid calculator input
- `IllegalArgumentException` for invalid operations

### Input Validation
- Client names cannot be empty (defaults to "Anonymous")
- Calculator operations validated before execution
- Division by zero prevented with error message
- Empty messages are ignored

### Graceful Disconnection
- Removes client from active list
- Notifies other clients
- Closes socket and streams properly

## Technical Details

- **Protocol**: TCP/IP over sockets
- **Port**: 5000
- **Threading Model**: One thread per client connection
- **Synchronization**: Thread-safe collections and synchronized blocks
- **Message Format**: Plain text with protocol prefixes (NAME:, CALC:, SYSTEM:)

## Files Generated

- `Server.java` - Main server application
- `ClientHandler.java` - Handles individual client connections
- `Client.java` - Client application
- `Calculator.java` - Calculator utility with operations
- `README.md` - This documentation

## Tested Scenarios

✅ Single client connection
✅ Multiple simultaneous client connections
✅ Message broadcasting
✅ Calculator operations (+, -, *, /, %)
✅ Client disconnection
✅ Division by zero handling
✅ System notifications on join/leave
✅ Thread synchronization with multiple clients

## Notes

- All clients connect to localhost:5000
- Modify the `HOST` and `PORT` constants in Client.java to connect to a different server
- Server maintains connections until clients explicitly disconnect
- All timestamps and calculations are processed on the server side
