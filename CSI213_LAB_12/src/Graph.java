import java.util.ArrayList;
public class Graph {
	public class Vertex{
		private String name;
		public ArrayList<Edge> edges;
		public int numOfEdges = 0;
		public Vertex(String name) {
			this.name = name;
			edges = new ArrayList<Edge>();
		}
		public Edge[] getEdges() {
			Edge[] e = new Edge[edges.size()];
			for(int i = 0; i < edges.size(); i++)
				e[i] = edges.get(i);
			return e;
		}
		public String getName() {
			return name;
		}
	}
	public class Edge{
		public Vertex vertex1;
		public Vertex vertex2;
		public Edge(Vertex v1, Vertex v2) {
			vertex1 = v1;
			vertex2 = v2;
			vertex1.numOfEdges++;
			vertex2.numOfEdges++;
		}
		public String getName() {
			return vertex2.getName();
		}
	}
	private ArrayList<Edge> edges;
	public Graph() {
		edges = new ArrayList<Edge>();
	}
	public void addEdge(String first, String last) {
		Vertex vertex1 = new Vertex(first), vertex2 = new Vertex(last);
		Edge e = new Edge(vertex1, vertex2);
		vertex1.edges.add(e);
		edges.add(e);
	}
	public String[] getNames() {
		String[] firstNames = new String[edges.size()];
		for(int i = 0; i < edges.size(); i++)
			firstNames[i] = edges.get(i).vertex1.getName();
		return firstNames;
	}
	public Vertex getVertex(String name) {
		for(Edge e : edges) {
			if(e.vertex1.getName().equals(name))
				return e.vertex1;
		}
		return null;
	}
	
}
