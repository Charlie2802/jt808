package org.yzh.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class T1078DataReader {

    private static final Logger log = LoggerFactory.getLogger(T1078DataReader.class);

    @Value("${jt-server.t1078.enable:false}")
    private boolean t1078Enabled;

    @Value("${jt-server.t1078.storage.path}")
    private String storagePath;

    public T1078DataReader() {
        log.info("T1078 Data Reader initialized");
    }

    /**
     * Process incoming T1078 data packet
     */
    public void processT1078Packet(String deviceId, int messageId, byte[] data) {
        if (!t1078Enabled) {
            return;
        }

        try {
            // Log the packet
            log.info("T1078 Packet received - Device: {}, MessageID: 0x{}, DataLength: {} bytes", 
                deviceId, Integer.toHexString(messageId).toUpperCase(), data.length);

            // Save packet data to file
            savePacketData(deviceId, messageId, data);
            
            // Process based on message type
            processMessageByType(deviceId, messageId, data);
            
        } catch (Exception e) {
            log.error("Error processing T1078 packet for device: {} messageId: 0x{}", 
                deviceId, Integer.toHexString(messageId).toUpperCase(), e);
        }
    }

    /**
     * Save packet data to storage
     */
    private void savePacketData(String deviceId, int messageId, byte[] data) throws IOException {
        Path devicePath = Paths.get(storagePath, deviceId);
        Files.createDirectories(devicePath);

        String fileName = String.format("t1078_%s_0x%s_%s.bin", 
            deviceId,
            Integer.toHexString(messageId).toUpperCase(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        Path packetFile = devicePath.resolve(fileName);

        Files.write(packetFile, data);
        log.debug("Saved T1078 packet data to: {}", packetFile);
    }

    /**
     * Process message based on T1078 message type
     */
    private void processMessageByType(String deviceId, int messageId, byte[] data) {
        switch (messageId) {
            case 0x1003: // 终端上传音视频属性
                log.info("T1078: Device {} uploaded audio/video properties", deviceId);
                break;
                
            case 0x1005: // 终端上传乘客流量
                log.info("T1078: Device {} uploaded passenger flow data", deviceId);
                break;
                
            case 0x1205: // 终端上传音视频资源列表
                log.info("T1078: Device {} uploaded audio/video resource list", deviceId);
                break;
                
            case 0x1206: // 文件上传完成通知
                log.info("T1078: Device {} completed file upload", deviceId);
                break;
                
            case 0x9105: // 实时音视频传输状态通知
                log.info("T1078: Device {} real-time audio/video transmission status", deviceId);
                break;
                
            case 0x30316364: // 实时音视频流及透传数据传输
                log.info("T1078: Device {} real-time audio/video stream data, length: {} bytes", deviceId, data.length);
                break;
                
            default:
                log.info("T1078: Device {} unknown message type: 0x{}", deviceId, Integer.toHexString(messageId).toUpperCase());
                break;
        }
    }

    /**
     * Get T1078 service status
     */
    public T1078Status getStatus() {
        T1078Status status = new T1078Status();
        status.setEnabled(t1078Enabled);
        status.setStoragePath(storagePath);
        
        if (t1078Enabled) {
            try {
                File storageDir = new File(storagePath);
                status.setStorageExists(storageDir.exists());
                status.setStorageWritable(storageDir.canWrite());
            } catch (Exception e) {
                status.setError(e.getMessage());
            }
        }
        
        return status;
    }

    /**
     * T1078 service status
     */
    public static class T1078Status {
        private boolean enabled;
        private String storagePath;
        private boolean storageExists;
        private boolean storageWritable;
        private String error;

        // Getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public String getStoragePath() { return storagePath; }
        public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
        
        public boolean isStorageExists() { return storageExists; }
        public void setStorageExists(boolean storageExists) { this.storageExists = storageExists; }
        
        public boolean isStorageWritable() { return storageWritable; }
        public void setStorageWritable(boolean storageWritable) { this.storageWritable = storageWritable; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
