import java.util.*;

public class Routing {

    /**
     * TODO
     * <p>
     * The findPaths function takes a board and a list of goals that contain
     * endpoints that need to be connected. The function returns a list of
     * Paths that connect the points.
     */
    public static ArrayList<Wire> findPaths(Board board, ArrayList<Endpoints> goals) {
        ArrayList<Wire> guide = new ArrayList<>();

        // Sort the goals from shortest distance (between each points) to longest
        ArrayList<Endpoints> sortedGoals = new ArrayList<>();
        Map<Endpoints, Double> distGoals = new HashMap<>();
        for (Endpoints x : goals){
            int ID = x.id;
            Coord copperStart = x.start;
            Coord copperEnd = x.end;

            double dist = Math.sqrt(Math.pow((copperEnd.row - copperStart.row), 2) + Math.pow((copperEnd.column - copperStart.column), 2));
            distGoals.put(x, dist);
        }

        ArrayList<Map.Entry<Endpoints, Double>> arr = sort(distGoals);
        for(Map.Entry<Endpoints, Double> e: arr) {
            sortedGoals.add(e.getKey());
        }

        //System.out.println(modGoals);
        ArrayList<Integer> toRemove = new ArrayList<>();
        ArrayList<Integer> toAddLater = new ArrayList<>();

        for (Endpoints wire : sortedGoals){
            int ID = wire.id;
            Coord copperStart = wire.start;
            Coord copperEnd = wire.end;

            ArrayList<Coord> stuck = new ArrayList<>();
            ArrayList<Coord> trace = bfs(board, copperStart, copperEnd, stuck);

            if (trace != null){
                Wire path = new Wire(ID, trace);
                board.placeWire(path);
                guide.add(path);
            } else {
                ArrayList<Coord> neighbors = board.adj(stuck.get(0));
                if (neighbors != null) {
                    for (Wire blocker : guide) {
                        ArrayList<Coord> coords = blocker.getPoints();
                        for (Coord x : neighbors) {
                            for (Coord y : coords) {
                                if (x.equals(y) && !(x.equals(coords.get(0)) || x.equals(coords.get(coords.size() - 1)))) {
                                    toRemove.add(blocker.id);
                                    toAddLater.add(ID);
                                    toAddLater.add(blocker.id);
                                }
                            }
                        }
                    }
                }
            }
            for (int x : toRemove){
                for (Wire w : guide){
                    if (w.id == x){
                        board.removeWire(w);
                    }
                }
                Iterator<Wire> itr = guide.iterator();
                while (itr.hasNext()) {
                    Wire w = itr.next();
                    if (w.id == x) {
                        itr.remove();
                    }
                }
            }

            for (int x : toAddLater){
                for (Endpoints e : goals){
                    if (x == e.id){
                        ArrayList<Coord> traceA = bfs(board, e.start, e.end, stuck);
                        if (traceA != null){
                            Wire path = new Wire(e.id, traceA);
                            board.placeWire(path);
                            guide.add(path);
                        }
                    }
                }

            }

        }
        System.out.println(toRemove);
        System.out.println(toAddLater);
        System.out.println(guide);
        return guide;
    }

    public static ArrayList<Coord> bfs(Board B, Coord start, Coord end, ArrayList<Coord> stuck) {
        // !occupied: 0
        // occupied: -1 or any number except 0
        Queue<Coord> Q = new LinkedList<Coord>();
        Q.add(start);

        Map<Coord, Coord> parent = new HashMap<Coord, Coord>();
        HashSet<Coord> visited = new HashSet<Coord>();
        ArrayList<Coord> stuckCoord = new ArrayList<>();
        visited.add(start);

        while (! Q.isEmpty()) {
            Coord curr = Q.remove();
            if (curr.equals(end)){
                return reconstructPath(start, end, parent);
            }

            for (Coord c : B.adj(curr)) {
                if (! visited.contains(c)){
                    if ((! B.isOccupied(c) && ! B.isObstacle(c)) || c.equals(end)) {
                        parent.put(c, curr);
                        Q.add(c);
                        stuckCoord.add(c);
                        visited.add(c);
                    }
                }
            }
        }
//        System.out.println(B.adj(start));
//        for (Map.Entry<Coord, Coord> entry : parent.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
//        }
        stuck.add(stuckCoord.get(stuckCoord.size() - 1));
        return null;
    }

    public static ArrayList<Coord> reconstructPath(Coord start, Coord end, Map<Coord, Coord> parent){
        ArrayList<Coord> path = new ArrayList<>();
        for (Coord point = end; point != null; point = parent.get(point)){
            path.add(point);
            if (point.equals(start)){
                break;
            }
        }
        Collections.reverse(path);
        return path;
    }

    public static ArrayList<Map.Entry<Endpoints, Double>> sort(Map<Endpoints, Double> map) {
        ArrayList<Map.Entry<Endpoints, Double>> arr = new ArrayList<>();

        for (Map.Entry<Endpoints, Double> e : map.entrySet()) {
            arr.add(e);
        }

        Comparator<Map.Entry<Endpoints, Double>> valueComparator = new Comparator<Map.Entry<Endpoints, Double>>() {
            @Override
            public int compare(Map.Entry<Endpoints, Double> e1, Map.Entry<Endpoints, Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        Collections.sort(arr, valueComparator);
        return arr;
    }
}