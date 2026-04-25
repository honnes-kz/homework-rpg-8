package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class WardenBossFloor extends TowerFloor {

    private Monster boss;
    private int startingPartyHp;
    private boolean openingRoarUsed;

    @Override
    protected void announce() {
        System.out.println("\n=== Floor 4 - Bell Warden's Apex ===");
        System.out.println("The tower bell tolls once, and the final guardian steps from the smoke.");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[setup] The Bell Warden tightens his grip on a grave-iron mace.");
        startingPartyHp = FloorSupport.totalHp(party);
        openingRoarUsed = false;
        boss = new Monster("Bell Warden", 42, 11);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[challenge] A boss fight begins at the summit.");
        int round = 1;

        while (boss.isAlive() && FloorSupport.anyHeroesAlive(party) && round <= 6) {
            System.out.println("  Boss Round " + round);

            if (!openingRoarUsed) {
                Hero stunnedHero = FloorSupport.firstLivingHero(party);
                if (stunnedHero != null) {
                    System.out.println("The Bell Warden unleashes a shockwave that stuns " + stunnedHero.getName() + ".");
                    stunnedHero.transitionTo(new StunnedState(1));
                }
                openingRoarUsed = true;
            }

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }

                hero.startTurn();
                if (!hero.isAlive()) {
                    continue;
                }

                if (hero.canAct()) {
                    hero.attack(boss);
                    if (!boss.isAlive()) {
                        System.out.println("The Bell Warden crashes to the floor.");
                        break;
                    }
                } else {
                    System.out.println(hero.getName() + " cannot answer the Warden this round.");
                }

                hero.endTurn();
            }

            if (!boss.isAlive()) {
                break;
            }

            Hero target = FloorSupport.weakestLivingHero(party);
            if (target == null) {
                break;
            }

            System.out.println("Bell Warden slams the mace toward " + target.getName() + ".");
            target.receiveAttack(boss.getName(), boss.getAttackPower());
            if (target.isAlive()
                    && "Normal".equals(target.getStateName())
                    && target.getHp() <= target.getMaxHp() / 2) {
                System.out.println(target.getName() + " answers the pain with desperate fury.");
                target.transitionTo(new BerserkState(2));
            }

            round++;
        }

        boolean cleared = !boss.isAlive() && FloorSupport.anyHeroesAlive(party);
        int damageTaken = startingPartyHp - FloorSupport.totalHp(party);
        String summary = cleared
                ? "The Bell Warden is defeated and the summit is secured."
                : "The Bell Warden holds the summit.";
        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[loot] The summit relic strengthens the survivors.");
        for (Hero hero : party) {
            hero.recover("summit relic", 6);
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        FloorSupport.printPartyStatus(party);
    }

    @Override
    protected String getFloorName() {
        return "Floor 4 - Bell Warden's Apex";
    }
}
