package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult run(List<Hero> party) {
        int floorsCleared = 0;

        System.out.println("=== The Haunted Tower Run Begins ===");
        for (TowerFloor floor : floors) {
            if (countLivingHeroes(party) == 0) {
                System.out.println("No heroes remain. The tower run ends immediately.");
                break;
            }

            FloorResult result = floor.explore(party);
            System.out.println("[result] " + result.getSummary() + " Damage taken: " + result.getDamageTaken());

            if (!result.isCleared()) {
                System.out.println("The party can climb no further.");
                break;
            }

            floorsCleared++;
        }

        boolean reachedTop = floorsCleared == floors.size() && countLivingHeroes(party) > 0;
        System.out.println("=== The Haunted Tower Run Ends ===");
        return new TowerRunResult(floorsCleared, countLivingHeroes(party), reachedTop);
    }

    private int countLivingHeroes(List<Hero> party) {
        int count = 0;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }
}
