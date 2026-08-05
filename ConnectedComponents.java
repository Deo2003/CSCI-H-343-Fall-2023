import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectedComponents {

    /*
     * TODO
     */
    public static <V> void
    connected_components(Graph<V> G, Map<V, V> representative) {
        Set<V> visited = new HashSet<>();
        for (V vertex : G.vertices()) {
            if (!visited.contains(vertex)) {
                helper(G, vertex, vertex, visited, representative);
            }
        }
    }

    private static <V> void
    helper(Graph<V> G, V vertex, V representative, Set<V> visited,
        Map<V, V> representativeMap) {
        visited.add(vertex);
        representativeMap.put(vertex, representative);
        for (V adjacentVertex : G.adjacent(vertex)) {
            if (!visited.contains(adjacentVertex)) {
                helper(G, adjacentVertex, representative, visited, representativeMap);
            }
        }
    }


}
