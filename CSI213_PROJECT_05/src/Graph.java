import java.util.List;

public interface Graph {
    public interface Edge {
        public int getDistance();
        public Vertex getDestination();
    }

    public interface Vertex {
        public String getName();
        public List<? extends Edge> getNeighbors();
    }
    public List<? extends Vertex> getVertices();
    public void addEdge(String name1, String name2, int distance);
}