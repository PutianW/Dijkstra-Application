import java.util.ArrayList;

public class GlobalNet
{
    //creates a global network 
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions) 
    {
	    //TODO

        // Add all the regions to the output graph T
        Graph T = new Graph(O.V());
        T.setCodes(O.getCodes());
        for (Graph G: regions) {
            ArrayList<Edge> list = G.edges();
            for (Edge e: list) {
                T.addEdge(e);
            }
        }

        // Run Dijkstra's Algo to find the edges that connect each region
        for (int i = 0; i < regions.length - 1; i++) {
            ArrayList<Integer> regionVertices = getVertices(O, regions, i);
            T = Dijkstra(O, regionVertices, T, i, regions);
        }

        T = T.connGraph();
        return T;
    }


    private static ArrayList<Integer> getVertices(Graph O, Graph[] regions, int i) {
        ArrayList<Integer> vertices = new ArrayList<>();
        String[] codes = regions[i].getCodes();
        for (String code: codes) {
            vertices.add(O.index(code));
        }
        return vertices;
    }


    private static Graph Dijkstra(Graph G, ArrayList<Integer> regionVertices, Graph T, int regionNum, Graph[] regions) {
        int[] dist = new int[G.V()];
        int[] prev = new int[G.V()];
        DistQueue Q = new DistQueue(G.V());

        dist[regionVertices.get(0)] = 0;
        for (int i = 0; i < G.V(); i++) {
            if (i != regionVertices.get(0)) {
                dist[i] = Integer.MAX_VALUE;
            }
            prev[i] = -1;
            Q.insert(i, dist[i]);
        }

        while (!Q.isEmpty()) {
            int u = Q.delMin();
            for (int w: G.adj(u)) {
                if (Q.inQueue(w)) {
                    if (regionVertices.contains(w)) {
                        dist[w] = 0;
                        prev[w] = -1;
                        Q.set(w, 0);
                    } else {
                        int d = dist[u] + G.getEdge(u, w).getW();
                        if (d < dist[w]) {
                            dist[w] = d;
                            prev[w] = u;
                            Q.set(w, d);
                        }
                    }
                }
            }
        }

        for (int i = regionNum + 1; i < regions.length; i++) {
            ArrayList<Integer> vertices = getVertices(G, regions, i);

            int min = Integer.MAX_VALUE;
            int v = -1;
            for (int vertex: vertices) {
                if (dist[vertex] < min) {
                    min = dist[vertex];
                    v = vertex;
                }
            }

            int u = prev[v];
            while (u != -1) {
                T.addEdge(G.getEdge(u, v));
                v = prev[v];
                u = prev[v];
            }
        }

        return T;
    }

}
    
    
    