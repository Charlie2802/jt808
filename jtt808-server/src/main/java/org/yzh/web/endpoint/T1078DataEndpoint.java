package org.yzh.web.endpoint;

import io.github.yezhihao.netmc.core.annotation.Endpoint;
import io.github.yezhihao.netmc.core.annotation.Mapping;
import io.github.yezhihao.netmc.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yzh.protocol.jsatl12.DataPacket;
import org.yzh.protocol.t1078.T1003;
import org.yzh.protocol.t1078.T1005;
import org.yzh.protocol.t1078.T1205;
import org.yzh.protocol.t1078.T1206;
import org.yzh.protocol.t1078.T9105;
import org.yzh.web.service.T1078DataReader;

import static org.yzh.protocol.commons.JT1078.*;

@Endpoint
@Component
public class T1078DataEndpoint {

    private static final Logger log = LoggerFactory.getLogger(T1078DataEndpoint.class);

    @Autowired
    private T1078DataReader t1078DataReader;

    @Mapping(types = 终端上传音视频属性, desc = "终端上传音视频属性")
    public void T1003(T1003 message, Session session) {
        String deviceId = session.getClientId();
        logT1078Message(deviceId, 0x1003, "终端上传音视频属性", message);
        session.response(message);
    }

    @Mapping(types = 终端上传乘客流量, desc = "终端上传乘客流量")
    public void T1005(T1005 message, Session session) {
        String deviceId = session.getClientId();
        logT1078Message(deviceId, 0x1005, "终端上传乘客流量", message);
        session.response(message);
    }

    @Mapping(types = 终端上传音视频资源列表, desc = "终端上传音视频资源列表")
    public void T1205(T1205 message, Session session) {
        String deviceId = session.getClientId();
        logT1078Message(deviceId, 0x1205, "终端上传音视频资源列表", message);
        session.response(message);
    }

    @Mapping(types = 文件上传完成通知, desc = "文件上传完成通知")
    public void T1206(T1206 message, Session session) {
        String deviceId = session.getClientId();
        logT1078Message(deviceId, 0x1206, "文件上传完成通知", message);
        session.response(message);
    }

    @Mapping(types = 实时音视频传输状态通知, desc = "实时音视频传输状态通知")
    public void T9105(T9105 message, Session session) {
        String deviceId = session.getClientId();
        logT1078Message(deviceId, 0x9105, "实时音视频传输状态通知", message);
        session.response(message);
    }

    @Mapping(types = 实时音视频流及透传数据传输, desc = "实时音视频流及透传数据传输")
    public void T30316364(DataPacket dataPacket, Session session) {
        String deviceId = session.getClientId();
        log.info("T1078 Real-time video data received from device: {}, packet size: {} bytes", 
                deviceId, dataPacket.getPayload().readableBytes());
        
        // Convert DataPacket to byte array for processing
        byte[] data = convertDataPacketToBytes(dataPacket);
        logT1078Message(deviceId, 0x30316364, "实时音视频流及透传数据传输", data);
        // For real-time video data, we just log it, no response needed
    }

    private void logT1078Message(String deviceId, int messageId, String messageType, Object message) {
        try {
            // Convert message to byte array for storage
            byte[] data = convertMessageToBytes(message);
            t1078DataReader.processT1078Packet(deviceId, messageId, data);
        } catch (Exception e) {
            // Log error but don't fail
            log.error("Error processing T1078 message: {}", e.getMessage(), e);
        }
    }

    private byte[] convertMessageToBytes(Object message) {
        // Simple conversion - in a real implementation, you'd use proper serialization
        if (message instanceof byte[]) {
            return (byte[]) message;
        } else {
            // For now, just return a simple representation
            return message.toString().getBytes();
        }
    }

    private byte[] convertDataPacketToBytes(DataPacket dataPacket) {
        try {
            // Get the payload from DataPacket
            io.netty.buffer.ByteBuf payload = dataPacket.getPayload();
            byte[] data = new byte[payload.readableBytes()];
            payload.getBytes(payload.readerIndex(), data);
            return data;
        } catch (Exception e) {
            log.error("Error converting DataPacket to bytes: {}", e.getMessage(), e);
            return new byte[0];
        }
    }
}
