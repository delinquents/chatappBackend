/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.services;

import com.chatapp.dtos.UserStatus;
import com.chatapp.dtos.domains.UserDto;
import com.chatapp.enums.Status;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yousef
 */
@Service
public class AnnounceUserStatus {
    
    private SimpMessagingTemplate template;
    
    
    private UsersService userService;
    
    @Autowired
    public void setUserService(UsersService userService) {
        this.userService = userService;
    }
    
    public void isOnline(String userName){
        userService.getOnlineUsers().add(userName);
        List<UserDto> friends = userService.getUserFriendsByUserName(userName);
        UserStatus userStatus = new UserStatus(userName, Status.ONLINE);
        for(UserDto user:friends){
            this.template.convertAndSendToUser(user.getUserName(),"/topic/contactlist", userStatus);
                        
        }
        
    }
    
    public void isOffline(String userName){
        userService.getOnlineUsers().remove(userName);
        List<UserDto> friends = userService.getUserFriendsByUserName(userName);
        UserStatus userStatus = new UserStatus(userName, Status.OFFLINE);
        for(UserDto user:friends){
            this.template.convertAndSendToUser(user.getUserName(),"/topic/contactlist", userStatus);
        }
        
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }
    
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    
}
