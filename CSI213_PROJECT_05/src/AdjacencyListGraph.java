/*
 * E.Pardo
 * April 29, 2023
 * Class for an adjacency list graph
 */
import java.util.ArrayList;
import java.util.List;

public class AdjacencyListGraph implements Graph{
	/*
	 * Class for the edges of the graph
	 */
	public class ALGEdge implements Edge{
		private int distance; //distance between two vertices
		private ALGVertex destination; //destination of current vertex
		/**
		 * Constructor for the ALGEdge class
		 * @param distance
		 * @param destination
		 */
		public ALGEdge(int distance, ALGVertex destination) {
			this.distance = distance;
			this.destination = destination;
		}
		/**
		 * Accesses the distance of the two vertices
		 * @return destination
		 */
		public int getDistance() {
			return distance;
		}
		/**
		 * Accesses the destination of the current vertex
		 * @return destination
		 */
        public Vertex getDestination() {
        	return destination;
        }
	}
	/*
	 *Class for the vertices of the graph 
	 */
	public class ALGVertex implements Vertex{
		private String name; //name of this vertex
		private ArrayList<ALGEdge> neighbors; //edges to all of the neighbors of this vertex
		/**
		 * Constructor for the ALGVertex class
		 * @param name
		 */
		public ALGVertex(String name){
			this.name = name;
			neighbors = new ArrayList<ALGEdge>();
		}
		/**
		 * Accesses the name of this vertex
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * Accesses the list of edges to the neighbors of this vertex
		 * @return neighbors
		 */
        public List<? extends Edge> getNeighbors(){
        	return neighbors;
        }
        /**
         * Adds an edge to neighbors
         * @param vertex
         * @param distance
         */
        public void addEdge(ALGVertex vertex, int distance) {
        	//check if the given vertex already has an edge in neighbors, adds one if not
        	for(ALGEdge edge : neighbors)
        		if(edge.getDestination().getName().equals(vertex.getName())) return;
        	neighbors.add(new ALGEdge(distance, vertex));
        }
	}
	private ArrayList<ALGVertex> vertices; //list of all the vertices in this graph
	/**
	 * Constructor for the AdjacencyListGraph class
	 */
	public AdjacencyListGraph() {
		vertices = new ArrayList<ALGVertex>();
	}
	/**
	 * Accesses the vertices of this graph
	 * @return vertices
	 */
	public List<? extends Vertex> getVertices(){
		return vertices;
	}
	/**
	 * Adds an edge between the two given places in the graph
	 * @param name1
	 * @param name2
	 * @param distance
	 */
    public void addEdge(String name1, String name2, int distance) {
    	//checks if name1 and name2 are in the list of vertices for this graph
    	//if not, it adds either or both and then makes an edge with the given distance between the two
    	if(!contains(name1) && !contains(name2)) {
    		ALGVertex first = new ALGVertex(name1);
    		ALGVertex second = new ALGVertex(name2);
    		first.addEdge(second, distance);
    		second.addEdge(first, distance);
    		vertices.add(second);
    		vertices.add(first);
    	}
    	else if(!contains(name1)) {
    		ALGVertex first = new ALGVertex(name1);
    		for(ALGVertex vertex : vertices) {
    			if(vertex.getName().equals(name2)) {
    				first.addEdge(vertex, distance);
    				vertex.addEdge(first, distance);
    			}
    		}
    		vertices.add(first);
    	}
    	else if(!contains(name2)) {
    		ALGVertex second = new ALGVertex(name2);
    		for(ALGVertex vertex : vertices) {
    			if(vertex.getName().equals(name1)) {
    				second.addEdge(vertex, distance);
    				vertex.addEdge(second, distance);
    			}
    		}
    		vertices.add(second);
    	}
    	else {
    		int index1 = 0;
    		int index2 = 0;
    		for(int i = 0; i < vertices.size(); i++) {
    			if(vertices.get(i).getName().equals(name1)) index1 = i;
    			if(vertices.get(i).getName().equals(name2)) index2 = i;
    		}
    		vertices.get(index1).addEdge(vertices.get(index2), distance);
    		vertices.get(index2).addEdge(vertices.get(index1), distance);
    	}
    }
    /**
     * Checks if vertices contains the given name
     * @param name
     * @return if name is in one of the vertices
     */
    public boolean contains(String name) {
    	for(ALGVertex vertex : vertices)
    		if(vertex.getName().equals(name)) return true;
    	return false;
    }
}