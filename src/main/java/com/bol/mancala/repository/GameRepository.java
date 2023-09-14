package com.bol.mancala.repository;

import com.bol.mancala.repository.entity.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GameRepository extends CrudRepository<Game, UUID> {
}
