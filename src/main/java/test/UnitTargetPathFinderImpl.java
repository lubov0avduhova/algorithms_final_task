package test;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        List<Edge> path = new ArrayList<>();

        // Игровое поле: размеры
        final int WIDTH = 27;
        final int HEIGHT = 21;

        // Создаем сет для проверки занятых клеток
        Set<Edge> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                occupiedCells.add(new Edge(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        // Начальная и конечная точки
        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Edge goal = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        // Карты для A* (gScore и fScore)
        Map<Edge, Integer> gScore = new HashMap<>();
        Map<Edge, Integer> fScore = new HashMap<>();
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, goal));

        // Очередь с приоритетом (по fScore)
        PriorityQueue<Edge> openSet = new PriorityQueue<>(Comparator.comparingInt(fScore::get));
        openSet.add(start);

        // Карта для восстановления пути
        Map<Edge, Edge> cameFrom = new HashMap<>();

        // Основной цикл A*
        while (!openSet.isEmpty()) {
            Edge current = openSet.poll();

            // Если цель достигнута, восстановить путь
            if (current.equals(goal)) {
                reconstructPath(cameFrom, current, path);
                return path;
            }

            for (Edge neighbor : getNeighbors(current, WIDTH, HEIGHT)) {
                if (occupiedCells.contains(neighbor)) continue;

                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return path; // Если путь не найден, вернуть пустой список
    }

    // Восстановление пути
    private void reconstructPath(Map<Edge, Edge> cameFrom, Edge current, List<Edge> path) {
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
    }

    // Метод оценки эвристики (манхэттенское расстояние)
    private int heuristic(Edge a, Edge b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    // Получение соседей
    private List<Edge> getNeighbors(Edge edge, int width, int height) {
        List<Edge> neighbors = new ArrayList<>();
        int x = edge.getX();
        int y = edge.getY();

        if (x > 0) neighbors.add(new Edge(x - 1, y));
        if (x < width - 1) neighbors.add(new Edge(x + 1, y));
        if (y > 0) neighbors.add(new Edge(x, y - 1));
        if (y < height - 1) neighbors.add(new Edge(x, y + 1));

        return neighbors;
    }
}
