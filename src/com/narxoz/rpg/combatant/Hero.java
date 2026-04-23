package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.*;;;

/**
 * Represents a player-controlled hero participating in the tower climb.
 *
 * Students: you may extend this class as needed for your implementation.
 * You will need to add a HeroState field and related methods.
 */
public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this(name, hp, attackPower, defense, new NormalState());
    }

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = initialState == null ? new NormalState() : initialState;
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public HeroState getState()    { return state; }
    public String getStateName()   { return state.getName(); }
    public boolean isAlive()       { return hp > 0; }

    /**
     * Reduces this hero's HP by the given amount, clamped to zero.
     *
     * @param amount the damage to apply; must be non-negative
     */
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    /**
     * Restores this hero's HP by the given amount, clamped to maxHp.
     *
     * @param amount the HP to restore; must be non-negative
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void transitionTo(HeroState nextState) {
        HeroState safeState = nextState == null ? new NormalState() : nextState;
        String previousName = state == null ? "None" : state.getName();
        state = safeState;
        if (!previousName.equals(safeState.getName())) {
            System.out.println(name + " shifts from " + previousName + " to " + safeState.getName() + ".");
        }
    }

    public void startTurn() {
        if (!isAlive()) {
            return;
        }
        System.out.println(name + " starts the turn in " + state.getName() + " (" + hp + "/" + maxHp + " HP).");
        state.onTurnStart(this);
    }

    public void endTurn() {
        if (!isAlive()) {
            return;
        }
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return isAlive() && state.canAct();
    }

    public int attack(Monster monster) {
        int damage = Math.max(1, state.modifyOutgoingDamage(attackPower));
        monster.takeDamage(damage);
        System.out.println(name + " attacks " + monster.getName() + " for " + damage
                + " damage while " + state.getName() + ".");
        return damage;
    }

    public int receiveAttack(String attackerName, int rawDamage) {
        int reducedByDefense = Math.max(1, rawDamage - defense);
        int finalDamage = Math.max(1, state.modifyIncomingDamage(reducedByDefense));
        takeDamage(finalDamage);
        System.out.println(attackerName + " hits " + name + " for " + finalDamage
                + " damage after defense/state adjustments.");
        announceIfFallen();
        return finalDamage;
    }

    public void sufferDirectDamage(String source, int amount) {
        int finalDamage = Math.max(0, amount);
        takeDamage(finalDamage);
        System.out.println(name + " suffers " + finalDamage + " damage from " + source + ".");
        announceIfFallen();
    }

    public void recover(String source, int amount) {
        if (!isAlive()) {
            return;
        }
        int before = hp;
        heal(Math.max(0, amount));
        int healed = hp - before;
        System.out.println(name + " recovers " + healed + " HP from " + source + ".");
    }

    public String describe() {
        return name + " - HP " + hp + "/" + maxHp + ", ATK " + attackPower
                + ", DEF " + defense + ", State " + state.getName();
    }

    private void announceIfFallen() {
        if (!isAlive()) {
            System.out.println(name + " has fallen.");
        }
    }
}
