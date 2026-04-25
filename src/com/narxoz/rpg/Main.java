package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.CryptBattleFloor;
import com.narxoz.rpg.floor.SanctuaryFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.VenomTrapFloor;
import com.narxoz.rpg.floor.WardenBossFloor;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point for Homework 8 - The Haunted Tower: Ascending the Floors.
 *
 * Build your heroes, floors, tower runner, and execute the climb here.
 */
public class Main {

    public static void main(String[] args) {
        List<Hero> party = Arrays.asList(
                new Hero("Lyra", 34, 10, 3, new PoisonedState(2)),
                new Hero("Doran", 40, 12, 2, new BerserkState(2))
        );

        List<TowerFloor> floors = Arrays.asList(
                new CryptBattleFloor(),
                new VenomTrapFloor(),
                new SanctuaryFloor(),
                new WardenBossFloor()
        );

        TowerRunner runner = new TowerRunner(floors);

        System.out.println("Initial party:");
        for (Hero hero : party) {
            System.out.println("  - " + hero.describe());
        }

        TowerRunResult result = runner.run(party);

        System.out.println();
        System.out.println("=== Final Tower Summary ===");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());
        System.out.println("Final party:");
        for (Hero hero : party) {
            System.out.println("  - " + hero.describe());
        }
    }
}
