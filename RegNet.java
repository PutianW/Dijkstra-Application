import java.util.ArrayList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) 
    {
	    //TODO

        // Step 1
        Graph T = new Graph(G.V());
        T.setCodes(G.getCodes());
        UnionFind UF = new UnionFind(G.V());
        ArrayList<Edge> edges = G.sortedEdges();

        int condition = T.V() - 1;
        while (T.E() < condition) {
            Edge e = edges.get(0);
            edges.remove(0);
            edges = G.sortedEdges();
            int u = e.ui();
            int v = e.vi();
            if (!UF.connected(u, v)) {
                T.addEdge(e);
                UF.union(u, v);
            }
        }

        // Step 2
        while (T.totalWeight() > max) {
            edges = T.sortedEdges();
            int i = edges.size() - 1;
            boolean run = true;
            while(run) {
                if (T.deg(edges.get(i).u) == 1 || T.deg(edges.get(i).v) == 1) {
                    T.removeEdge(edges.get(i));
                    run = false;
                }
                i--;
            }
        }

        // Step 3
        Graph stopsGraph = new Graph(T.V());
        stopsGraph.setCodes(T.getCodes());

        // Use BFS to create a graph that represents the #stops between each vertex
        int T_size = T.V();
        for (int i = 0; i < T_size; i++) {
            if (T.adj(i) != null) {
                stopsGraph = BFS(T, i, stopsGraph);
            }
        }

        // Sort edges then add edges to T
        ArrayList<Edge> stopsList = stopsGraph.sortedEdges();
        int i = stopsList.size() - 1;
        while (i >= 0 && stopsList.get(i).getW() > 0 && T.totalWeight() <= max) {
            Graph temp = new Graph(T.V());
            temp.setCodes(G.getCodes());
            int currentStops = stopsList.get(i).getW();
            temp.addEdge(G.getEdge(stopsList.get(i).ui(), stopsList.get(i).vi()));
            i--;
            while (i >= 0) {
                if (stopsList.get(i).getW() == currentStops) {
                    temp.addEdge(G.getEdge(stopsList.get(i).ui(), stopsList.get(i).vi()));
                    i--;
                } else {
                    break;
                }
            }
            ArrayList<Edge> sortedList = temp.sortedEdges();
            for (Edge e: sortedList) {
                if (T.totalWeight() + e.getW() <= max) {
                    T.addEdge(e);
                } else {
                    break;
                }
            }
        }

        T = T.connGraph();
        return T;
    }


    private static Graph BFS(Graph T, int s, Graph stopGraph) {
        Boolean[] visits = new Boolean[T.V()];
        for (int i = 0; i < visits.length; i++) {
            visits[i] = false;
        }
        int[] dists = new int[T.V()];
        BetterQueue<Integer> Q = new BetterQueue<>();
        Q.add(s);
        visits[s] = true;
        dists[s] = -1;

        while (!Q.isEmpty()) {
            int u = Q.remove();
            int d = dists[u];
            for (int w: T.adj(u)) {
                if (!visits[w]) {
                    Q.add(w);
                    dists[w] = d + 1;
                    visits[w] = true;
                    if ((stopGraph.getEdge(s, w) == null) && (stopGraph.getEdge(w, s) == null)) {
                        stopGraph.addEdge(s, w, dists[w]);
                    }
                }
            }
        }
        return stopGraph;
    }

}