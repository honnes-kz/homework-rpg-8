package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.ArrayList;
import java.util.List;

public class CryptBattleFloor extends TowerFloor {

    private final List<Monster> monsters = new ArrayList<>();
    private int startingPartyHp;

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[setup] Coffins crack open and the crypt defenders rise.");
        startingPartyHp = FloorSupport.totalHp(party);
        monsters.clear();
        monsters.add(new Monster("Bone Archer", 18, 8));
        monsters.add(new Monster("Rust Knight", 26, 10));
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[challenge] The party enters a round-based fight against undead guards.");
        int round = 1;

        while (FloorSupport.anyHeroesAlive(party) && FloorSupport.monstersAlive(monsters) && round <= 5) {
            System.out.println("  Round " + round);

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }

                hero.startTurn();
                if (!hero.isAlive()) {
                    continue;
                }

                Monster target = FloorSupport.firstLivingMonster(monsters);
                if (target == null) {
                    hero.endTurn();
                    break;
                }

                if (hero.canAct()) {
                    hero.attack(target);
                    if (!target.isAlive()) {
                        System.out.println(target.getName() + " collapses into dust.");
                    }
                } else {
                    System.out.println(hero.getName() + " loses the action phase.");
                }

                hero.endTurn();
            }

            if (!FloorSupport.monstersAlive(monsters)) {
                break;
            }

            for (Monster monster : monsters) {
                if (!monster.isAlive()) {
                    continue;
                }

                Hero target = FloorSupport.weakestLivingHero(party);
                if (target == null) {
                    break;
                }

                System.out.println(monster.getName() + " lunges at " + target.getName() + ".");
                target.receiveAttack(monster.getName(), monster.getAttackPower());
            }

            round++;
        }

        boolean cleared = !FloorSupport.monstersAlive(monsters) && FloorSupport.anyHeroesAlive(party);
        int damageTaken = startingPartyHp - FloorSupport.totalHp(party);
        String summary = cleared
                ? "The crypt defenders are defeated."
                : "The party is overwhelmed in the crypt.";
        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[loot] Soul lantern shards restore the victors.");
        for (Hero hero : party) {
            hero.recover("crypt spoils", 4);
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        FloorSupport.printPartyStatus(party);
    }

    @Override
    protected String getFloorName() {
        return "Floor 1 - Skeleton Crypt";
    }
}
