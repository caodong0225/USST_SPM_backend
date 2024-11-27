package usst.spm.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Service
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-consumer-group")
public class RocketMQConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        // 在此处理收到的消息逻辑
    }
}
