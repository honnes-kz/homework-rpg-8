package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class SanctuaryFloor extends TowerFloor {

    private int startingPartyHp;

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[setup] Blue candles ignite as the party enters a silent sanctuary.");
        startingPartyHp = FloorSupport.totalHp(party);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[challenge] The floor tests patience rather than strength.");

        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }

            hero.startTurn();
            if (!hero.isAlive()) {
                continue;
            }

            hero.recover("sanctuary waters", 8);
            if (!"Normal".equals(hero.getStateName())) {
                System.out.println("The sanctuary purifies " + hero.getName() + ".");
                hero.transitionTo(new NormalState());
            }
            hero.endTurn();
        }

        return new FloorResult(
                FloorSupport.anyHeroesAlive(party),
                Math.max(0, startingPartyHp - FloorSupport.totalHp(party)),
                "The party regains composure in the sanctuary."
        );
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("[loot] Hook override: this floor offers rest, not treasure.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[loot] No loot is granted here.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        FloorSupport.printPartyStatus(party);
    }

    @Override
    protected String getFloorName() {
        return "Floor 3 - Sanctuary of Echoes";
    }
}
