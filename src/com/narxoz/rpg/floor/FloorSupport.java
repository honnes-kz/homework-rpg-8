package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.Comparator;
import java.util.List;

final class FloorSupport {

    private FloorSupport() {
    }

    static int totalHp(List<Hero> party) {
        int total = 0;
        for (Hero hero : party) {
            total += hero.getHp();
        }
        return total;
    }

    static boolean anyHeroesAlive(List<Hero> party) {
        return firstLivingHero(party) != null;
    }

    static Hero firstLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }

    static Hero weakestLivingHero(List<Hero> party) {
        return party.stream()
                .filter(Hero::isAlive)
                .min(Comparator.comparingInt(Hero::getHp))
                .orElse(null);
    }

    static Monster firstLivingMonster(List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return monster;
            }
        }
        return null;
    }

    static boolean monstersAlive(List<Monster> monsters) {
        return firstLivingMonster(monsters) != null;
    }

    static void printPartyStatus(List<Hero> party) {
        System.out.println("[cleanup] Party status:");
        for (Hero hero : party) {
            System.out.println("  - " + hero.describe());
        }
    }
}
