import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("cities.txt"));
        var g = new AdjacencyListGraph();
        makeGraph(lines, g);
        System.out.println();
        var names = new HashSet<String>();
        lines.forEach(l-> {
            var parts = l.split(" ");
            names.add(parts[0]);
            names.add(parts[1]);
        });
        var g2 = new AdjacencyMatrix(names.size());
        makeGraph(lines, g2);
    }

    private static void makeGraph(List<String> lines, Graph g) throws Exception {
        for (var line : lines) {
            var parts = line.split(" ");
            if (parts.length!=3) throw new Exception("parts has " + parts.length + " parts in " + line);
            g.addEdge(parts[0],parts[1],Integer.parseInt(parts[2]));
        }
        Dijkstras.dijkstras(g,"Madrid");
    }

}