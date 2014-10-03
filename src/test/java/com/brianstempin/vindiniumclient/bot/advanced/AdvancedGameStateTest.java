package com.brianstempin.vindiniumclient.bot.advanced;

import com.brianstempin.vindiniumclient.dto.GameState;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by bstempi on 10/3/14.
 */
public class AdvancedGameStateTest {
    private static final String KNOWN_GOOD_GAME_STATE = "/known-good-game-state.json";
    private static final Gson gson = new Gson();

    @Before
    public void setup() {

    }

    @Test
    public void createFromGameState() throws IOException {
        File jsonFile = new File(this.getClass().getResource(KNOWN_GOOD_GAME_STATE).getFile());
        GameState gameState = gson.fromJson(new FileReader(jsonFile), GameState.class);

        AdvancedGameState testObj = new AdvancedGameState(gameState);

        Assert.assertNotNull(testObj.getBoardGraph());
        Assert.assertNotNull(testObj.getHeroesById());
        Assert.assertNotNull(testObj.getHeroesByPosition());
        Assert.assertNotNull(testObj.getMe());
        Assert.assertNotNull(testObj.getMines());
        Assert.assertNotNull(testObj.getPubs());
        // Test a mine with an owner
        Assert.assertEquals(testObj.getMines().get(new GameState.Position(7, 3)).getOwner().getId(), 4);
        // Test a mine without an owner
        Assert.assertNull(testObj.getMines().get(new GameState.Position(7, 14)).getOwner());
    }
}