package com.wrf.websocket;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class ChannelContainer {

    private static Map<String, Channel> map = new ConcurrentHashMap<>();

    /**
     * 保存设备ID和对应的channel
     * @param uuid
     * @param channel
     * @return
     */
    public static Channel put(String uuid,Channel channel) {
        return map.put(uuid, channel);
    }

    /**
     * 管理的channel数量
     * @return
     */
    public static int size() {
        return map.size();
    }

    public static Channel getChannel(String uuid) {
        if (map.containsKey(uuid)) {
            return map.get(uuid);
        }
        return null;
    }

    /**
     * 移除channel
     * @param channel
     */
    public static void remove(Channel channel) {
        Set<Map.Entry<String, Channel>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Channel>> it = entrySet.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Channel> e = it.next();
            if (e.getValue() == channel) {
                it.remove();
            }
        }
    }
}
