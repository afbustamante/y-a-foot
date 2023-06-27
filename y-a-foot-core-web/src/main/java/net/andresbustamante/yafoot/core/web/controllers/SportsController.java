package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.services.SportSearchService;
import net.andresbustamante.yafoot.core.web.mappers.SportMapper;
import net.andresbustamante.yafoot.web.dto.Sport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class SportsController extends AbstractController implements SportsApi {

    private final SportSearchService sportSearchService;
    private final SportMapper sportMapper;

    public SportsController(SportSearchService sportSearchService, SportMapper sportMapper) {
        this.sportSearchService = sportSearchService;
        this.sportMapper = sportMapper;
    }

    @Override
    public ResponseEntity<List<Sport>> loadSports() {
        try {
            List<net.andresbustamante.yafoot.core.model.Sport> sports = sportSearchService.loadSports();
            return ResponseEntity.ok(sportMapper.map(sports));
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
