package com.bol.mancala.repository;

import com.bol.mancala.dto.BoardDto;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import org.springframework.data.relational.core.mapping.event.AfterSaveCallback;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SaveGameEntityCallback implements AfterSaveCallback<Game> {

    public static final String TOPIC_GAME = "/topic/game/";

    private final SimpMessagingTemplate messagingTemplate;

    public SaveGameEntityCallback(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Game onAfterSave(Game aggregate) {
        Board board = aggregate.getBoard();
        if (board != null) {
            BoardDto boardDto = BoardDto.fromBoard(board);
            messagingTemplate.convertAndSend(TOPIC_GAME + aggregate.getId(), boardDto);
        }
        return aggregate;
    }
}
