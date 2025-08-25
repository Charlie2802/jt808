# JT808 Server with T1078 Video Streaming Support

A comprehensive JT808 protocol server implementation with enhanced T1078 video streaming capabilities, built with Spring Boot and Netty.

## 🚀 Features

### Core JT808 Protocol Support
- **Device Registration & Authentication**: Complete JT808 device registration and authentication flow
- **Location Reporting**: Real-time GPS location data processing
- **Heartbeat Management**: Automatic device heartbeat monitoring
- **Session Management**: Robust device session handling with automatic cleanup

### T1078 Video Streaming Support
- **Real-time Video Streaming**: T9101 command for live video streaming
- **Remote Playback**: T9201 command for video playback requests
- **Resource Query**: T9205 command for querying available video resources
- **File Upload**: T9206 command for requesting video file uploads
- **Data Processing**: T1078 video data parsing and storage

### Web API & Management
- **RESTful API**: Complete REST API for device management and command sending
- **Real-time Monitoring**: WebSocket support for real-time device status
- **Device Status**: Live device connection status and health monitoring
- **Command History**: Track and manage device commands

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Charlie2802/jt808-server.git
   cd jt808-server
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the server**
   ```bash
   cd jtt808-server
   mvn spring-boot:run
   ```

## ⚙️ Configuration

The server configuration is in `jtt808-server/src/main/resources/application.yml`:

```yaml
server:
  port: 8000

jt-server:
  jt808:
    enable: true
    port:
      udp: 7611
      tcp: 7611
    media-file:
      path: /home/aaditya/jt_data/media_file
    alarm-file:
      host: 127.0.0.1
      port: 7612

  t1078:
    enable: true
    port:
      tcp: 1078
      udp: 1078
    storage:
      path: /home/aaditya/jt_data/t1078_data
```

## 🌐 API Endpoints

### Device Management
- `GET /api/t1078/command/devices` - Get all connected devices
- `GET /device/all` - Get all device sessions
- `GET /device/option` - Get device options

### T1078 Commands
- `POST /api/t1078/command/9101` - Send real-time video streaming request
- `POST /api/t1078/command/9201` - Send remote playback request
- `POST /api/t1078/command/9205` - Query video resources
- `POST /api/t1078/command/9206` - Request file upload

### Example T9101 Command
```bash
curl -X POST "http://localhost:8000/api/t1078/command/9101" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "deviceId=522076851217&serverIp=35.194.18.237&tcpPort=8554&udpPort=8554&channelNo=1&mediaType=0&streamType=0"
```

## 📡 Protocol Support

### JT808 Protocol (Port 7611)
- **T0100**: Device Registration
- **T0102**: Device Authentication
- **T0200**: Location Information Report
- **T0002**: Heartbeat
- **T0003**: Device Logout

### T1078 Protocol (Port 1078)
- **T9101**: Real-time Video Streaming Request
- **T9201**: Remote Playback Request
- **T9205**: Resource List Query
- **T9206**: File Upload Request

## 🔧 Development

### Project Structure
```
jtt808-server/
├── src/main/java/org/yzh/web/
│   ├── controller/
│   │   ├── T1078CommandController.java
│   │   └── T1078StatusController.java
│   ├── service/
│   │   ├── T1078CommandService.java
│   │   └── T1078DataReader.java
│   └── endpoint/
│       └── T1078DataEndpoint.java
└── src/main/resources/
    └── application.yml
```

### Key Components
- **T1078CommandController**: REST API for T1078 commands
- **T1078CommandService**: Business logic for command processing
- **T1078DataReader**: Video data processing and storage
- **T1078DataEndpoint**: Real-time data streaming

## 📊 Monitoring

### Device Status
- Real-time device connection monitoring
- Automatic session cleanup for disconnected devices
- Heartbeat timeout detection

### Logs
- Comprehensive logging for all JT808 and T1078 activities
- Debug information for troubleshooting
- Performance metrics

## 🚀 Deployment

### Production Deployment
1. Build the JAR file:
   ```bash
   mvn clean package
   ```

2. Run the server:
   ```bash
   java -jar jtt808-server/target/jtt808-server-1.0.0-SNAPSHOT.jar
   ```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY jtt808-server/target/jtt808-server-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8000 7611 1078
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Charlie2802** - [aaditya.darakh@gmail.com](mailto:aaditya.darakh@gmail.com)

## 🙏 Acknowledgments

- Original JT808 protocol implementation by yezhihao
- Spring Boot framework
- Netty for high-performance networking
- All contributors and maintainers

---

**Note**: This server supports both JT808 location tracking and T1078 video streaming protocols, making it suitable for comprehensive IoT device management and video surveillance applications.

