import java.util.*;

public class Dijkstras {

    public static class dijkstraNode {
        private dijkstraNode(Graph.Vertex v, boolean isSource) {
            distance = isSource ? 0 : Integer.MAX_VALUE;
            self = v;
            previous = this;
        }

        public int distance;

        public dijkstraNode previous;

        public Graph.Vertex self;
    }

    public static dijkstraNode getAndRemoveSmallest(LinkedList<dijkstraNode> list) {
        dijkstraNode n = list.get(0); // start with the first one
        for (var l : list) {
            if (l.distance < n.distance) // if this one is smaller, use it instead
                n = l;
        }
        list.remove(n);
        return n;
    }

    public static void dijkstras(Graph g, String source) {
    /* https://en.wikipedia.org/wiki/Dijkstra's_algorithm#Pseudocode
      function Dijkstra(Graph, source):
       for each vertex v in Graph.Vertices:
           dist[v] â† INFINITY
           prev[v] â† UNDEFINED
           add v to Q
       dist[source] â† 0
 */
        LinkedList<dijkstraNode> done = new LinkedList<>();
        // make a dijkstra's node for each graph vertex. Note if it is the source or not.
        LinkedList<dijkstraNode> q = new LinkedList<>(g.getVertices().stream().map(v-> new dijkstraNode(v,source.equals(v.getName()))).toList());

    /*
       while Q is not empty:
          u â† vertex in Q with min dist[u]
          remove u from Q
*/
        while (!q.isEmpty()) {
            var u = getAndRemoveSmallest(q);
            done.add(u);
// for each neighbor v of u still in Q:
            for (var e : u.self.getNeighbors()) {
                var v = e.getDestination();
                dijkstraNode dnForV = null;
                for (var findV : q) { // implement "still in q" by looping over q looking for a match
                    if (findV.self == v) {
                        dnForV = findV;
                        break;
                    }
                }
                if (dnForV != null) {
/*
              alt â† dist[u] + Graph.Edges(u, v)
              if alt < dist[v]:
                  dist[v] â† alt
                  prev[v] â† u
*/
                   int alt = e.getDistance() + u.distance;
                   if (alt < dnForV.distance) {
                        dnForV.distance = alt;
                        dnForV.previous = u;
                   }
                }
            }
        }
        /* return dist[], prev[] */
        for (var v : done) {
            System.out.println("The shortest way (" + v.distance + ") to get to " + v.self.getName() + " is through " + v.previous.self.getName());
        }
    }
}