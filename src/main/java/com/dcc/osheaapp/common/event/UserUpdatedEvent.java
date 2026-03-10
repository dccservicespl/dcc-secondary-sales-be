package com.dcc.osheaapp.common.event;

import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.context.ApplicationEvent;

public class UserUpdatedEvent extends ApplicationEvent {
    private final UserDetailsVo user;
    public UserUpdatedEvent(Object source, UserDetailsVo user) {
        super(source);
        this.user = user;
    }

    public UserDetailsVo getUser() {
        return user;
    }
}
