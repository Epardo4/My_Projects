import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Graph g = new Graph();
        var lines = Files.readAllLines(Paths.get("likes.txt")); // read in the file
        for (var line : lines) { // for each line
            var entries = line.split(" "); // split the line into the two names
            g.addEdge(entries[0],entries[1]); // add the edge to the graph
        }

        for (var name : g.getNames()) { // for each unique name in the graph
            var v = g.getVertex(name); // get the vertex for the name
            System.out.print(name + " (") ; // print the name
            for (var a : v.getEdges()) { // for each edge of this vertex
                System.out.print(a.getName() + ", "); // print the name of the edge's other vertex
            }
            System.out.println(" )"); // close the parenthesis
        }
    }
}