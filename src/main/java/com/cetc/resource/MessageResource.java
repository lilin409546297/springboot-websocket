package com.cetc.resource;

import com.cetc.result.RequestMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 消息接收处理
 */
@RestController
public class MessageResource {

    //spring提供的推送方式
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 广播模式
     * @param requestMsg
     * @return
     */
    @MessageMapping("/broadcast")
    @SendTo("/topic/broadcast")
    public String broadcast(RequestMsg requestMsg) {
        //这里是有return，如果不写@SendTo默认和/topic/broadcast一样
        return "server:" + requestMsg.getBody().toString();
    }

    /**
     * 订阅模式，只是在订阅的时候触发，可以理解为：访问——>返回数据
     * @param id
     * @return
     */
    @SubscribeMapping("/subscribe/{id}")
    public String subscribe(@DestinationVariable Long id) {
        return "success";
    }

    /**
     * 用户模式
     * @param requestMsg
     * @param principal
     */
    @MessageMapping("/one")
    //@SendToUser("/queue/one") 如果存在return,可以使用这种方式
    public void one(RequestMsg requestMsg, Principal principal) {
        //这里使用的是spring的security的认证体系，所以直接使用Principal获取用户信息即可。
        //注意为什么使用queue，主要目的是为了区分广播和队列的方式。实际采用topic，也没有关系。但是为了好理解
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/one", requestMsg.getBody());
    }
}
