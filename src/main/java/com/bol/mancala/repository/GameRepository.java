package com.bol.mancala.repository;

import com.bol.mancala.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
}
