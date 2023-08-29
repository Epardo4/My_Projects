/*
 * E.Pardo
 * April 29, 2023
 * Class for an adjacency matrix graph
 */
import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrix implements Graph{
	/*
	 * Class for the edges of the graph
	 */
	public class AMEdge implements Edge{
		private int distance; //distance between two vertices
		private AMVertex destination; //destination of current vertex
		/**
		 * Constructor for the ALGEdge class
		 * @param distance
		 * @param destination
		 */
		public AMEdge(int distance, AMVertex destination) {
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
	public class AMVertex implements Vertex{
		private String name; //name of this vertex
		private ArrayList<AMEdge> neighbors; //edges to all of the neighbors of this vertex
		/**
		 * Constructor for the ALGVertex class
		 * @param name
		 */
		public AMVertex(String name){
			this.name = name;
			neighbors = new ArrayList<AMEdge>();
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
        	//goes through each item in the graph and the matrix and adds an edge between the two if one doesn't already exist
        	for(int i = 0; i < vertices.length; i++) 
        		for(int j = 0; j < vertices.length; j++) 
        			if(matrix[i][j] > 0) {
        				vertices[i].addEdge(vertices[j], matrix[i][j]);
        				vertices[j].addEdge(vertices[i], matrix[i][j]);
        			}
        	return neighbors;
        }
        /**
         * Adds an edge to neighbors
         * @param vertex
         * @param distance
         */
        public void addEdge(AMVertex vertex, int distance) {
        	//check if the given vertex already has an edge in neighbors, adds one if not
        	for(AMEdge edge : neighbors)
        		if(edge.getDestination().getName().equals(vertex.getName())) return;
        	neighbors.add(new AMEdge(distance, vertex));
        }
	}
	private AMVertex[] vertices; //list of all the vertices in this graph
	private int[][] matrix; //the matrix for this graph
	/**
	 * Constructor for the AdjacencyListGraph class
	 */
	public AdjacencyMatrix(int size) {
		vertices = new AMVertex[size];
		matrix = new int[size][size];
	}
	/**
	 * Accesses the vertices of this graph
	 * @return vertices
	 */
	public List<? extends Vertex> getVertices() {
		ArrayList<AMVertex> list = new ArrayList<AMVertex>();
		//goes through the list of vertices and adds to the array list which will be returned
		for(AMVertex vertex : vertices)
			if(vertex != null)
				list.add(vertex);
		return list;
	}
	/**
	 * Inputs values to the matrix for this graph
	 * @param name1
	 * @param name2
	 * @param distance
	 */
    public void addEdge(String name1, String name2, int distance) {
    	int index1 = indexOf(name1), index2 = indexOf(name2);
    	//checks if each name needs to be added to the list of vertices, adds accordingly,
    	//and inputs the distance into the matrix as required
    	if(index1 < 0 && index2 < 0) {
    		index1 = addVertex(name1);
    		index2 = addVertex(name2);
    	}
    	else if(index1 < 0) index1 = addVertex(name1);
    	else if(index2 < 0) index2 = addVertex(name2);
    	matrix[index1][index2] = distance;
    }
    /**
     * Gets the vertex where vertices holds the name
     * @param name
     * @return the index of vertices that holds the name or -1 if not found 
     */
    public int indexOf(String name) {
    	//loops through vertices to check if and where the vertex with this name occurs
    	for(int i = 0; i < vertices.length; i++)
    		 if(vertices[i] != null && vertices[i].getName().equals(name)) return i;
    	return -1;
    }
    /**
     * Adds a vertex as necessary with the given name to vertices
     * @param name
     * @return the index of the newly added vertex
     */
    public int addVertex(String name) {
    	int index = indexOf(name);
    	//if a vertex with name isn't here, add at the first null index and return then otherwise, return the index it's at
    	if(index < 0)
    		for(int i = 0; i < vertices.length; i++)
    			if(vertices[i] == null) {
    				vertices[i] = new AMVertex(name);
    				return i;
    		}
    	return index;
    }
}