package com.hehe.user.event;

import com.google.common.eventbus.Subscribe;
import com.hehe.common.event.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户登录等操作信息写入日志
 * @author xieqinghe .
 * @date 2017/11/15 下午5:17
 * @email xieqinghe@terminus.io
 */
@Component
@Slf4j
public class UserEventLogs implements EventListener {

    @Subscribe
    public void onEveryLogin(UserEventLogsDto userEventLogsDto) {
        log.info("登录日志：token:"+userEventLogsDto.getGrant().toString());
    }

}
