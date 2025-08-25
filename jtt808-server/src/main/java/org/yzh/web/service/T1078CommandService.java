package org.yzh.web.service;

import io.github.yezhihao.netmc.session.Session;
import io.github.yezhihao.netmc.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yzh.protocol.t1078.T9101;
import org.yzh.protocol.t1078.T9201;
import org.yzh.protocol.t1078.T9205;
import org.yzh.protocol.t1078.T9206;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class T1078CommandService {

    private static final Logger log = LoggerFactory.getLogger(T1078CommandService.class);

    @Autowired
    private SessionManager sessionManager;

    /**
     * Send T9101 real-time video streaming request to device
     */
    public boolean sendT9101Command(String deviceId, T9101 command) {
        try {
            Session session = sessionManager.get(deviceId);
            if (session == null) {
                log.warn("Device {} is not connected", deviceId);
                return false;
            }

            log.info("Sending T9101 command to device: {} - IP: {}, TCP: {}, UDP: {}, Channel: {}, MediaType: {}, StreamType: {}", 
                deviceId, command.getIp(), command.getTcpPort(), command.getUdpPort(), 
                command.getChannelNo(), command.getMediaType(), command.getStreamType());

            session.notify(command);
            return true;
        } catch (Exception e) {
            log.error("Error sending T9101 command to device: {}", deviceId, e);
            return false;
        }
    }

    /**
     * Send T9201 remote playback request (for single frame capture)
     */
    public boolean sendT9201Command(String deviceId, String serverIp, int tcpPort, int udpPort, 
                                   int channelNo, int mediaType, int streamType, int storageType, 
                                   int playbackMode, int playbackSpeed, String startTime, String endTime) {
        try {
            Session session = sessionManager.get(deviceId);
            if (session == null) {
                log.warn("Device {} is not connected", deviceId);
                return false;
            }

            T9201 command = new T9201();
            command.setIp(serverIp);
            command.setTcpPort(tcpPort);
            command.setUdpPort(udpPort);
            command.setChannelNo(channelNo);
            command.setMediaType(mediaType);
            command.setStreamType(streamType);
            command.setStorageType(storageType);
            command.setPlaybackMode(playbackMode);
            command.setPlaybackSpeed(playbackSpeed);
            command.setStartTime(startTime);
            command.setEndTime(endTime);

            log.info("Sending T9201 command to device: {} - IP: {}, Port: {}, Channel: {}, PlaybackMode: {}, StartTime: {}", 
                deviceId, serverIp, tcpPort, channelNo, playbackMode, startTime);

            session.notify(command);
            return true;
        } catch (Exception e) {
            log.error("Error sending T9201 command to device: {}", deviceId, e);
            return false;
        }
    }

    /**
     * Send T9205 query resource list
     */
    public boolean sendT9205Command(String deviceId, int channelNo, String startTime, String endTime,
                                   int warnBit1, int warnBit2, int mediaType, int streamType, int storageType) {
        try {
            Session session = sessionManager.get(deviceId);
            if (session == null) {
                log.warn("Device {} is not connected", deviceId);
                return false;
            }

            T9205 command = new T9205();
            command.setChannelNo(channelNo);
            command.setStartTime(startTime);
            command.setEndTime(endTime);
            command.setWarnBit1(warnBit1);
            command.setWarnBit2(warnBit2);
            command.setMediaType(mediaType);
            command.setStreamType(streamType);
            command.setStorageType(storageType);

            log.info("Sending T9205 command to device: {} - Channel: {}, MediaType: {}, StartTime: {}, EndTime: {}", 
                deviceId, channelNo, mediaType, startTime, endTime);

            session.notify(command);
            return true;
        } catch (Exception e) {
            log.error("Error sending T9205 command to device: {}", deviceId, e);
            return false;
        }
    }

    /**
     * Send T9206 file upload command
     */
    public boolean sendT9206Command(String deviceId, String serverIp, int port, String username, String password, String path,
                                   int channelNo, String startTime, String endTime, int warnBit1, int warnBit2,
                                   int mediaType, int streamType, int storageType, int condition) {
        try {
            Session session = sessionManager.get(deviceId);
            if (session == null) {
                log.warn("Device {} is not connected", deviceId);
                return false;
            }

            T9206 command = new T9206();
            command.setIp(serverIp);
            command.setPort(port);
            command.setUsername(username);
            command.setPassword(password);
            command.setPath(path);
            command.setChannelNo(channelNo);
            command.setStartTime(parseDateTime(startTime));
            command.setEndTime(parseDateTime(endTime));
            command.setWarnBit1(warnBit1);
            command.setWarnBit2(warnBit2);
            command.setMediaType(mediaType);
            command.setStreamType(streamType);
            command.setStorageType(storageType);
            command.setCondition(condition);

            log.info("Sending T9206 command to device: {} - IP: {}, Port: {}, Channel: {}, MediaType: {}, StartTime: {}", 
                deviceId, serverIp, port, channelNo, mediaType, startTime);

            session.notify(command);
            return true;
        } catch (Exception e) {
            log.error("Error sending T9206 command to device: {}", deviceId, e);
            return false;
        }
    }

    /**
     * Parse BCD datetime string to LocalDateTime
     */
    private LocalDateTime parseDateTime(String bcdTime) {
        if (bcdTime == null || bcdTime.length() != 12) {
            return LocalDateTime.now();
        }
        
        try {
            // Format: YYMMDDHHMMSS
            int year = 2000 + Integer.parseInt(bcdTime.substring(0, 2));
            int month = Integer.parseInt(bcdTime.substring(2, 4));
            int day = Integer.parseInt(bcdTime.substring(4, 6));
            int hour = Integer.parseInt(bcdTime.substring(6, 8));
            int minute = Integer.parseInt(bcdTime.substring(8, 10));
            int second = Integer.parseInt(bcdTime.substring(10, 12));
            
            return LocalDateTime.of(year, month, day, hour, minute, second);
        } catch (Exception e) {
            log.warn("Error parsing datetime: {}, using current time", bcdTime);
            return LocalDateTime.now();
        }
    }

    /**
     * Get all connected devices
     */
    public Map<String, Object> getConnectedDevices() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Get all active sessions
            Collection<Session> sessions = sessionManager.all();
            
            result.put("totalDevices", sessions.size());
            
            // Add session details
            Map<String, Object> deviceDetails = new HashMap<>();
            for (Session session : sessions) {
                String deviceId = session.getClientId();
                
                Map<String, Object> details = new HashMap<>();
                details.put("connected", true);
                details.put("clientId", deviceId);
                
                deviceDetails.put(deviceId, details);
            }
            result.put("deviceDetails", deviceDetails);
            result.put("devices", deviceDetails.keySet());
            
        } catch (Exception e) {
            log.error("Error getting connected devices", e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}
