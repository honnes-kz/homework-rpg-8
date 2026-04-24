package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState(int turnsRemaining) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
    }

    @Override
    public String getName() {
        return "Stunned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, basePower / 2);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, rawDamage + 3);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " is stunned and cannot act this turn.");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            hero.transitionTo(new NormalState());
        } else {
            System.out.println(hero.getName() + " is still stunned for " + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return false;
    }
}
