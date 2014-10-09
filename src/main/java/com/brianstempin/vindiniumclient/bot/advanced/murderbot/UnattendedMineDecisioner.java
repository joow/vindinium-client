package com.brianstempin.vindiniumclient.bot.advanced.murderbot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.bot.advanced.Mine;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.Map;

import static com.brianstempin.vindiniumclient.bot.advanced.murderbot.AdvancedMurderBot.DijkstraResult;

/**
 * Decides to go after an unclaimed mine far, far away.
 *
 * This decisioner decides if any mines are "easy," despite being out of the way.
 *
 * According to Maslov's Hierarchy, this is boredom.
 */
public class UnattendedMineDecisioner implements Decision<AdvancedMurderBot.GameContext, BotMove> {

    private final Decision<AdvancedMurderBot.GameContext, BotMove> noGoodMineDecision;

    public UnattendedMineDecisioner(Decision<AdvancedMurderBot.GameContext, BotMove> noGoodMineDecision) {
        this.noGoodMineDecision = noGoodMineDecision;
    }

    @Override
    public BotMove makeDecision(AdvancedMurderBot.GameContext context) {

        Map<GameState.Position, DijkstraResult> dijkstraResultMap = context.getDijkstraResultMap();

        // A good target is the closest unattended mine
        Mine targetMine = null;

        for(Mine mine : context.getGameState().getMines().values()) {
            if(targetMine == null || mine.getOwner() == null)
                targetMine = mine;
            else if(dijkstraResultMap.get(targetMine.getPosition()).getDistance()
                    > dijkstraResultMap.get(mine.getPosition()).getDistance()) {
                targetMine = mine;
            }
        }

        if(targetMine != null) {
            GameState.Position currentPosition = targetMine.getPosition();
            DijkstraResult currentResult = dijkstraResultMap.get(currentPosition);
            while(currentResult.getDistance() > 1) {
                currentPosition = currentResult.getPrevious();
                currentResult = dijkstraResultMap.get(currentPosition);
            }

            return BotUtils.directionTowards(context.getGameState().getMe().getPos(),
                    currentPosition);
        } else {
            return noGoodMineDecision.makeDecision(context);
        }
    }
}