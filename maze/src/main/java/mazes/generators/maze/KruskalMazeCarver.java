package mazes.generators.maze;

import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.
        ISet<Room> rooms = maze.getRooms();
        ISet<Wall> walls = maze.getWalls();
        Random r = new Random();
        for (Wall w : walls) {
            w.setDistance(r.nextInt(walls.size()));
        }
        Graph<Room, Wall> g = new Graph<Room, Wall>(rooms, walls);
        ISet<Wall> result = g.findMinimumSpanningTree();
        for (Wall w : walls) {
            w.resetDistanceToOriginal();
        }
        return result;
    }
}
