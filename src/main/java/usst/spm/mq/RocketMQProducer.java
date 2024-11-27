package usst.spm.mq;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Service
public class RocketMQProducer {

    @Resource
    private RocketMQTemplate rocketTemplate;

    // 发送普通消息
    public void sendMessage(String topic, String message) {
        rocketTemplate.convertAndSend(topic, message);
        System.out.println("Message sent: " + message);
    }

    // 发送延时消息（示例：延迟等级 3，10 秒）
    public void sendDelayedMessage(String topic, String message) {
        rocketTemplate.syncSend(topic, message);
        System.out.println("Delayed message sent: " + message);
    }
}
