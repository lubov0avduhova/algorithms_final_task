package test;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            playerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            computerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            simulateRound(playerUnits, computerUnits);
        }
    }

    private void simulateRound(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        for (Iterator<Unit> it = playerUnits.iterator(); it.hasNext(); ) {
            Unit playerUnit = it.next();
            if (!playerUnit.isAlive()) {
                it.remove();
                continue;
            }
            Unit target = playerUnit.getProgram().attack();
            if (target != null) {
                printBattleLog.printBattleLog(playerUnit, target);
            }
        }

        for (Iterator<Unit> it = computerUnits.iterator(); it.hasNext(); ) {
            Unit computerUnit = it.next();
            if (!computerUnit.isAlive()) {
                it.remove();
                continue;
            }
            Unit target = computerUnit.getProgram().attack();
            if (target != null) {
                printBattleLog.printBattleLog(computerUnit, target);
            }
        }
    }
}