package test;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();

        // Sort units by efficiency: attack/cost and health/cost
        unitList.sort(Comparator.comparingDouble(unit -> -((double) unit.getBaseAttack() / unit.getCost() + (double) unit.getHealth() / unit.getCost())));

        int currentPoints = 0;
        for (Unit unit : unitList) {
            int maxUnitsForType = Math.min(11, maxPoints / unit.getCost());
            for (int i = 0; i < maxUnitsForType && currentPoints + unit.getCost() <= maxPoints; i++) {
                selectedUnits.add(new Unit(
                        unit.getName(),
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        unit.getxCoordinate(),
                        unit.getyCoordinate()
                ));
                currentPoints += unit.getCost();
            }
        }

        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);

        return computerArmy;
    }
}