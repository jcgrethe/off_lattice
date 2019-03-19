package ar.edu.itba.ss;
import javafx.util.Pair;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NeighborDetection {

    /**
     * Returns a map with the neighbors for each particle using the "Cell Index Method".
     *
     * @param grid                  The current grid with all the particles loaded.
     * @param usedCells             A set of pairs of coordenates of the used cells (for optimization)
     * @param interactionRadio      The max distance between two particles to be neighbors.
     * @param contornCondition      True if the contorn condition is on.
     * @return  A Map with a {@link List} of {@link Particle}s for each Particle.
     */
    protected static Map<Particle, List<Particle>> getNeighbors(Grid grid, HashSet<Pair<Integer, Integer>> usedCells, Double interactionRadio, Boolean contornCondition){
        Map<Particle, List<Particle>> result = new HashMap<>();
        // Foreach cell with particles
        usedCells.forEach(pair -> {
            int i = pair.getKey(), j = pair.getValue();
            for (Particle current : grid.getCell(i, j).getParticles()){
                List<Particle> currentNeighbors = new ArrayList<>();
                List<Particle> sameCell = new ArrayList<>();

                //get the neighboor added before or a new linked list
                final List<Particle> neighbors = result.getOrDefault(current, new LinkedList<>());

                if (!contornCondition) {
                    //Check the four neighbors taking advantage of the simetry.
                    if (i != 0)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i - 1, j), interactionRadio, contornCondition, grid.getSideLength()));
                    if (i != grid.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i + 1, j), interactionRadio, contornCondition, grid.getSideLength()));

                    if (j != grid.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i, j + 1), interactionRadio, contornCondition, grid.getSideLength()));
                    if (j != 0)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i, j - 1), interactionRadio, contornCondition, grid.getSideLength()));

                    if (i != 0 && j != grid.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i - 1, j + 1), interactionRadio, contornCondition, grid.getSideLength()));
                    if (j != grid.getSideCellsQuantity() - 1 && i != grid.getSideCellsQuantity() - 1)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i + 1, j + 1), interactionRadio, contornCondition, grid.getSideLength()));
                    if (i != grid.getSideCellsQuantity() - 1 && j != 0)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i + 1, j - 1), interactionRadio, contornCondition, grid.getSideLength()));
                    if (j != 0 && i != 0)
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getCell(i - 1, j - 1), interactionRadio, contornCondition, grid.getSideLength()));
                }else {
                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getSideCell((i - 1)  , j), interactionRadio, contornCondition, grid.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getSideCell(i - 1, j + 1), interactionRadio, contornCondition, grid.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getSideCell(i, j + 1), interactionRadio, contornCondition, grid.getSideLength()));

                        currentNeighbors.addAll(getNeighborParticles(current,
                                grid.getSideCell(i + 1, j + 1), interactionRadio, contornCondition, grid.getSideLength()));
                }

                //check same cell
                sameCell.addAll(getNeighborParticles(current,
                        grid.getCell(i, j), interactionRadio, contornCondition, grid.getSideLength()));

                //add all to the neighbors
                neighbors.addAll(currentNeighbors);
                neighbors.addAll(sameCell);

                //for each neighbors add current to the relation
                for (Particle newRelation : currentNeighbors) {
                    final List<Particle> anotherNeighbors = result.getOrDefault(newRelation, new LinkedList<>());
                    anotherNeighbors.add(current);
                    result.put(newRelation, anotherNeighbors);
                }

                result.put(current, neighbors);
            }
        });
        return result;
    }


    /**
     * This function returns a list of the particles that are near than the interaction radio from the current Particle.
     *
     * @param current The particle looking for neighbors
     * @param cell    The cell under lookup.
     * @param interactionRadio The max length of the distance from the current particle.
     * @return
     */
    private static List<Particle> getNeighborParticles(Particle current, Cell cell, Double interactionRadio, boolean contorn, int gridSize){
        return cell.getParticles().stream()
                .parallel()
                .filter(another -> getDistance(current, another, contorn, gridSize) - current.getRadio() <= interactionRadio)
                .filter(another -> !current.equals(another))
                .collect(Collectors.toList());
    }

    private static Double getDistance(Particle p1, Particle p2, boolean contorn, int size){
        double y = Math.abs(p2.getLastState().getY() - p1.getLastState().getY());
        double x = Math.abs(p2.getLastState().getX() - p1.getLastState().getX());
        double h = Math.hypot(y, x); 
        if (contorn){
            double xc = Math.abs(p1.getLastState().getX() - p2.getLastState().getX());
            xc = Math.min(xc, size - xc);
            double yc = (double)size - Math.abs(p1.getLastState().getY() - p2.getLastState().getY());
            yc = Math.min(yc, size - yc);
            return Math.min(h, Math.hypot(xc, yc));
        }
        return h;
    }
}
