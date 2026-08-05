import java.util.*;

public class Flood {

    // Implement the flood function
    public static void flood(WaterColor color, LinkedList<Coord> flooded_list, Tile[][] tiles, Integer board_size) {
        Set<Coord> visited = new HashSet<>(); // set for visited, queue for flooded list for BFS
        Queue<Coord> queue = new LinkedList<>();

        // add flooded to queue and visited
        for (Coord coord : flooded_list) {
            queue.add(coord);
            visited.add(coord);
        }


        while (!queue.isEmpty()) { //loop through the queue
            Coord currentCoord = queue.poll(); // pull 1st coord from queue

            // directions (up, down, left, right)
            Coord[] directions = {currentCoord.up(), currentCoord.down(), currentCoord.left(), currentCoord.right()};

            // check neighbors
            for (Coord neighborCoord : directions) {

                if (neighborCoord.onBoard(board_size)) { // on board?
                    // not visited? and same color player has selected?
                    if (!visited.contains(neighborCoord) && tiles[neighborCoord.getY()][neighborCoord.getX()].getColor() == color) {
                        flooded_list.add(neighborCoord); // update flooded_list

                        // BFS continuation, add to visited/queue
                        visited.add(neighborCoord);
                        queue.add(neighborCoord);
                    }
                }
            }
        }
    }


    // An alternative implementation goes here (if you choose to implement it)
    public static void flood1(WaterColor color, LinkedList<Coord> flooded_list, Tile[][] tiles, Integer board_size) {
        Set<Coord> newFlooded = new HashSet<>(flooded_list); // create set from flooded_list
        boolean changesMade;

        do {
            changesMade = false;
            Set<Coord> tempFlooded = new HashSet<>(newFlooded);

            for (int y = 0; y < board_size; y++) {
                for (int x = 0; x < board_size; x++) { // Scan the entire board -- this loop wont go if we loop through the whole board and not find a tile to flood
                    Coord currentCoord = new Coord(x, y);

                    if (tiles[y][x].getColor() == color && !newFlooded.contains(currentCoord)) { // If a tile is of the color and is not in the flooded list **The main part**
                        Coord[] directions = {currentCoord.up(), currentCoord.down(), currentCoord.left(), currentCoord.right()};

                        for (Coord neighborCoord : directions) { // loop through each neighboring tile for the coordinate we are checking
                            // if NEIGHBOR** within board && is within the flooded list already
                            // This means that currentTILE (currentCord) is a tile that must be flooded because it's neighbor is flooded as explained up above
                            if (neighborCoord.onBoard(board_size) && newFlooded.contains(neighborCoord)) {
                                tempFlooded.add(currentCoord); // I make a temp list to add all the coords again that have been flooded and should be flooded
                                changesMade = true; // we found a tile that should be changed, so we need to (outer) loop again to check for more
                                break;
                            }
                        }
                    } // end if-statement
                }
            }

            newFlooded = new HashSet<>(tempFlooded); // update set with new flooded tile
        } while (changesMade);

        flooded_list.clear(); // clear and transfer the set we used into the official flooded_list for the game
        flooded_list.addAll(newFlooded);
    }

}