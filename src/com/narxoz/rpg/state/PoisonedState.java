package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;

    public PoisonedState(int turnsRemaining) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.ceil(basePower * 0.8));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, rawDamage + 2);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " coughs as poison burns for another turn.");
        hero.sufferDirectDamage("poison", 4);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            hero.transitionTo(new NormalState());
        } else {
            System.out.println(hero.getName() + " remains poisoned for " + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
