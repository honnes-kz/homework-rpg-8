package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    private int turnsRemaining;

    public BerserkState(int turnsRemaining) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
    }

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.ceil(basePower * 1.5));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, rawDamage + 2);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println(hero.getName() + " fights recklessly in a berserk rage.");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            hero.transitionTo(new NormalState());
        } else {
            System.out.println(hero.getName() + " stays berserk for " + turnsRemaining + " more turn(s).");
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
