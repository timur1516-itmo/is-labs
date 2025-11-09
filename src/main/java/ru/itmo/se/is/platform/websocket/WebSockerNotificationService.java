package ru.itmo.se.is.platform.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import ru.itmo.se.is.shared.notification.NotificationMessageType;
import ru.itmo.se.is.shared.notification.NotificationService;

@ApplicationScoped
public class WebSockerNotificationService implements NotificationService {

    @Inject
    private WebSocketService webSocketService;

    @Override
    public void notifyAll(NotificationMessageType type) {
        String message = Json.createObjectBuilder()
                .add("type", type.name())
                .build().toString();

        webSocketService.broadcast(message);
    }
}
