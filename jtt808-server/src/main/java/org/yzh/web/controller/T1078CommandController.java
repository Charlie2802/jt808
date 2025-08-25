package org.yzh.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yzh.commons.model.APIResult;
import org.yzh.commons.model.APICodes;
import org.yzh.protocol.t1078.T9101;
import org.yzh.web.service.T1078CommandService;

@Tag(name = "T1078 Commands", description = "Send T1078 commands to devices")
@RestController
@RequestMapping("/api/t1078/command")
public class T1078CommandController {

    @Autowired
    private T1078CommandService t1078CommandService;

    @Operation(summary = "Send T9101 real-time video streaming request", description = "Request real-time video streaming from device")
    @PostMapping("/9101")
    public APIResult<String> sendT9101Command(
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "35.194.18.237") String serverIp,
            @RequestParam(defaultValue = "8554") int tcpPort,
            @RequestParam(defaultValue = "8554") int udpPort,
            @RequestParam(defaultValue = "1") int channelNo,
            @RequestParam(defaultValue = "0") int mediaType,
            @RequestParam(defaultValue = "0") int streamType) {
        
        try {
            T9101 command = new T9101();
            command.setIp(serverIp);
            command.setTcpPort(tcpPort);
            command.setUdpPort(udpPort);
            command.setChannelNo(channelNo);
            command.setMediaType(mediaType);
            command.setStreamType(streamType);
            
            boolean sent = t1078CommandService.sendT9101Command(deviceId, command);
            
            if (sent) {
                return APIResult.ok("T9101 command sent successfully to device: " + deviceId);
            } else {
                return new APIResult<>(APICodes.OperationFailed, "Failed to send T9101 command - device not connected");
            }
        } catch (Exception e) {
            return new APIResult<>(APICodes.OperationFailed, "Error sending T9101 command: " + e.getMessage());
        }
    }

    @Operation(summary = "Send T9201 remote playback request", description = "Request remote video playback from device")
    @PostMapping("/9201")
    public APIResult<String> sendT9201Command(
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "35.194.18.237") String serverIp,
            @RequestParam(defaultValue = "8554") int tcpPort,
            @RequestParam(defaultValue = "8554") int udpPort,
            @RequestParam(defaultValue = "1") int channelNo,
            @RequestParam(defaultValue = "2") int mediaType,
            @RequestParam(defaultValue = "0") int streamType,
            @RequestParam(defaultValue = "0") int storageType,
            @RequestParam(defaultValue = "4") int playbackMode,
            @RequestParam(defaultValue = "0") int playbackSpeed,
            @RequestParam String startTime,
            @RequestParam(defaultValue = "000000000000") String endTime) {
        
        try {
            boolean sent = t1078CommandService.sendT9201Command(deviceId, serverIp, tcpPort, udpPort, 
                channelNo, mediaType, streamType, storageType, playbackMode, playbackSpeed, startTime, endTime);
            
            if (sent) {
                return APIResult.ok("T9201 command sent successfully to device: " + deviceId);
            } else {
                return new APIResult<>(APICodes.OperationFailed, "Failed to send T9201 command - device not connected");
            }
        } catch (Exception e) {
            return new APIResult<>(APICodes.OperationFailed, "Error sending T9201 command: " + e.getMessage());
        }
    }

    @Operation(summary = "Send T9205 query resource list", description = "Query available video/image resources from device")
    @PostMapping("/9205")
    public APIResult<String> sendT9205Command(
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "1") int channelNo,
            @RequestParam(defaultValue = "000000000000") String startTime,
            @RequestParam(defaultValue = "000000000000") String endTime,
            @RequestParam(defaultValue = "0") int warnBit1,
            @RequestParam(defaultValue = "0") int warnBit2,
            @RequestParam(defaultValue = "2") int mediaType,
            @RequestParam(defaultValue = "0") int streamType,
            @RequestParam(defaultValue = "0") int storageType) {
        
        try {
            boolean sent = t1078CommandService.sendT9205Command(deviceId, channelNo, startTime, endTime, 
                warnBit1, warnBit2, mediaType, streamType, storageType);
            
            if (sent) {
                return APIResult.ok("T9205 command sent successfully to device: " + deviceId);
            } else {
                return new APIResult<>(APICodes.OperationFailed, "Failed to send T9205 command - device not connected");
            }
        } catch (Exception e) {
            return new APIResult<>(APICodes.OperationFailed, "Error sending T9205 command: " + e.getMessage());
        }
    }

    @Operation(summary = "Send T9206 file upload command", description = "Request file upload from device")
    @PostMapping("/9206")
    public APIResult<String> sendT9206Command(
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "35.194.18.237") String serverIp,
            @RequestParam(defaultValue = "8554") int port,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String password,
            @RequestParam(defaultValue = "/upload") String path,
            @RequestParam(defaultValue = "1") int channelNo,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(defaultValue = "0") int warnBit1,
            @RequestParam(defaultValue = "0") int warnBit2,
            @RequestParam(defaultValue = "2") int mediaType,
            @RequestParam(defaultValue = "0") int streamType,
            @RequestParam(defaultValue = "0") int storageType,
            @RequestParam(defaultValue = "7") int condition) {
        
        try {
            boolean sent = t1078CommandService.sendT9206Command(deviceId, serverIp, port, username, password, path,
                channelNo, startTime, endTime, warnBit1, warnBit2, mediaType, streamType, storageType, condition);
            
            if (sent) {
                return APIResult.ok("T9206 command sent successfully to device: " + deviceId);
            } else {
                return new APIResult<>(APICodes.OperationFailed, "Failed to send T9206 command - device not connected");
            }
        } catch (Exception e) {
            return new APIResult<>(APICodes.OperationFailed, "Error sending T9206 command: " + e.getMessage());
        }
    }

    @Operation(summary = "Get connected devices", description = "List all currently connected devices")
    @GetMapping("/devices")
    public APIResult<Object> getConnectedDevices() {
        try {
            return APIResult.ok(t1078CommandService.getConnectedDevices());
        } catch (Exception e) {
            return new APIResult<>(APICodes.OperationFailed, "Error getting connected devices: " + e.getMessage());
        }
    }
}
