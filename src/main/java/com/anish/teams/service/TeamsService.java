package com.anish.teams.service;

import com.anish.teams.dto.NotificationRequestDto;

public interface TeamsService {

    void sendChannelNotification(NotificationRequestDto request);

}
