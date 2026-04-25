package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;

public class VenomTrapFloor extends TowerFloor {

    private int startingPartyHp;

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[setup] Hidden darts and green vapors line the narrow corridor.");
        startingPartyHp = FloorSupport.totalHp(party);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[challenge] The party crosses a trapped hall one careful step at a time.");

        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }

            hero.startTurn();
            if (!hero.isAlive()) {
                continue;
            }

            hero.sufferDirectDamage("needle trap", 3);
            hero.endTurn();
        }

        Hero weakest = FloorSupport.weakestLivingHero(party);
        if (weakest != null) {
            System.out.println("A toxic dart finds " + weakest.getName() + " at the end of the corridor.");
            weakest.transitionTo(new PoisonedState(2));
        }

        boolean cleared = FloorSupport.anyHeroesAlive(party);
        int damageTaken = startingPartyHp - FloorSupport.totalHp(party);
        String summary = cleared
                ? "The trap hall is crossed, but the venom lingers."
                : "The party falls in the poisoned corridor.";
        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[loot] A forgotten satchel contains a few bandages.");
        for (Hero hero : party) {
            hero.recover("bandages", 2);
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        FloorSupport.printPartyStatus(party);
    }

    @Override
    protected String getFloorName() {
        return "Floor 2 - Venom Needle Hall";
    }
}
