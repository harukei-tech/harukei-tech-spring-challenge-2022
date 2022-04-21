import java.util.*;
import java.io.*;
import java.math.*;
import java.util.ArrayList;
import java.util.List;

class Wait implements Action {
    @Override
    public String toString() {
        return "WAIT";
    }

}
class Move implements Action {
    int x, y;
    Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("MOVE");
        return s.append(" ").append(this.x).append(" ").append(this.y).toString();
    }
}
interface Action {
    String toString();
}

class Hero extends Charactor {
    Hero(
        int id,
        int x,
        int y,
        int shieldLife,
        int isControlled
    ){
        this.id = id;
        this.x = x;
        this.y = y;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
    }
}

class Monster extends Charactor implements Comparable<Monster>{
    int health;
    int vx, vy;
    int nearBase, threatFor;
    Monster(
        int id,
        int x,
        int y,
        int shieldLife,
        int isControlled,
        int health,
        int vx,
        int vy,
        int nearBase,
        int threatFor
    ){
        this.id = id;
        this.x = x;
        this.y = y;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
        this.health = health;
        this.vx = vx;
        this.vy = vy;
        this.nearBase = nearBase;
        this.threatFor = threatFor;
    }


    @Override
    public int compareTo(Monster m) {
        double distanceA = Math.sqrt((Player.baseX - this.x) * (Player.baseX - this.x) + (Player.baseY - this.y) * (Player.baseY - this.y));
        double distanceB = Math.sqrt((Player.baseX - m.x) * (Player.baseX - m.x) + (Player.baseY - m.y) * (Player.baseY - m.y));
        if (distanceA < distanceB) {
            return -1;
        }
        if (distanceA > distanceB) {
            return 1;
        }
        return 0;
    }
}

class Charactor {
    int id;
    int x, y;
    int shieldLife, isControlled;
}


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    static int baseX;
    static int baseY;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        baseX = in.nextInt(); // The corner of the map representing your base
        baseY = in.nextInt();
        int heroesPerPlayer = in.nextInt(); // Always 3

        // game loop
        while (true) {
            for (int i = 0; i < 2; i++) {
                int health = in.nextInt(); // Your base health
                int mana = in.nextInt(); // Ignore in the first league; Spend ten mana to cast a spell
            }
            int entityCount = in.nextInt(); // Amount of heros and monsters you can see
            List<Hero> me = new ArrayList<>();
            List<Hero> op = new ArrayList<>();
            List<Monster> monsters = new ArrayList<>();

            for (int i = 0; i < entityCount; i++) {
                int id = in.nextInt(); // Unique identifier
                int type = in.nextInt(); // 0=monster, 1=your hero, 2=opponent hero
                int x = in.nextInt(); // Position of this entity
                int y = in.nextInt();
                int shieldLife = in.nextInt(); // Ignore for this league; Count down until shield spell fades
                int isControlled = in.nextInt(); // Ignore for this league; Equals 1 when this entity is under a control spell
                int health = in.nextInt(); // Remaining health of this monster
                int vx = in.nextInt(); // Trajectory of this monster
                int vy = in.nextInt();
                int nearBase = in.nextInt(); // 0=monster with no target yet, 1=monster targeting a base
                int threatFor = in.nextInt(); // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither

                if(type == 0) {
                    monsters.add(new Monster(id,x,y,shieldLife,isControlled,health,vx,vy,nearBase,threatFor));
                } else if (type == 1) {
                    me.add(new Hero(id,x,y,shieldLife,isControlled));
                } else {
                    op.add(new Hero(id,x,y,shieldLife,isControlled));
                }
            }

            Monster targetMonster;
            if (monsters.size() == 0) {
                targetMonster = null;
            }else {
                targetMonster = getThreatfullMonster(baseX, baseY, monsters);
            }
            // Monster target = monsters.stream().filter(m -> m.id == monsterId).findFirst().orElseThrow();

            for (int i = 0; i < heroesPerPlayer; i++) {
                Action action;
                if(targetMonster == null) {
                    action = new Wait();
                } else {
                    action = new Move(targetMonster.x, targetMonster.y);
                }
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");


                // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
                System.out.println(action.toString());
            }
        }
    }

    public static Monster getThreatfullMonster(int baseX, int baseY, List<Monster> monsters) {
        Collections.sort(monsters);
        Monster mostThreatMonster = monsters.get(0);

        return mostThreatMonster;
    }
}