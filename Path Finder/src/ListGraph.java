//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816

import java.util.*;

public class ListGraph<T> implements Graph<T>{

    private Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node){

        nodes.putIfAbsent(node, new HashSet<>());
    }


    @Override
    public void connect(T node1, T node2, String name, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Wrong! Some of the nodes are missing.");
        } else if(weight < 0) {
            throw new IllegalArgumentException("Wrong! The weight can't be negative.");
        } else if(getEdgeBetween(node1, node2) != null) {
            throw new IllegalStateException("Wrong! A edge already exists.");
        } else {
            Set<Edge<T>> node1Edges = nodes.get(node1);
            Set<Edge<T>> node2Edges = nodes.get(node2);
            node1Edges.add(new Edge<T>(node2, name, weight));
            node2Edges.add(new Edge<T>(node1, name, weight));
        }
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException("Wrong! Some of the nodes are missing.");
        }
        else if(weight<0){
            throw new IllegalArgumentException("Wrong! The weight can't be negative.");
        }
        else{
            Edge <T> edge = getEdgeBetween(node1, node2);
            edge.setWeight(weight);
            Edge <T> edge2 = getEdgeBetween(node2, node1);
            edge2.setWeight(weight);
        }
    }

    @Override
    public Set <T> getNodes() {
        return nodes.keySet();
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException("Wrong! The node is missing from the graph.");
        }
        else {
            return nodes.get(node);
        }
    }

    @Override
    public Edge <T> getEdgeBetween(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Wrong! Some of the nodes are missing.");
        }
            for (Edge<T> edge : nodes.get(node1)) {
                if (edge.getDestination().equals(node2)) {
                    return edge;
                }
            }
            return null;
    }

    @Override
    public void disconnect(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Wrong! Some of the nodes are missing.");
        } else if (getEdgeBetween(node1, node2) == null) {
            throw new IllegalStateException("There is no edge between these two nodes.");
        } else {
            Edge <T> edge = getEdgeBetween(node1, node2);
            Edge <T> edge2 = getEdgeBetween(node2, node1);
            nodes.get(node1).remove(edge);
            nodes.get(node2).remove(edge2);
        }
    }

    @Override
    public void remove(T node) {
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException("Wrong! The node is missing.");
        }
        Collection<Edge<T>> edges = getEdgesFrom(node);
        for(T edge: nodes.keySet()){
            nodes.get(edge).removeIf(e -> e.getDestination().equals(node));
        }
        nodes.remove(node);
    }

    @Override
    public boolean pathExists(T from, T to) {
        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return false;
        }
        Set<T> visited = new HashSet<>();
        dfs(from,to,visited);
        return visited.contains(to);
    }


    public List<Edge<T>> getShortestPath(T from, T to) {
        Map<T, T> connections = new HashMap<>();
        LinkedList<T> queue = new LinkedList<>();
        connections.put(from, null);
        queue.add(from);
        while(!queue.isEmpty()){
            T obj = queue.pollFirst();
            for(Edge<T> edge: nodes.get(obj)){
                T destination = edge.getDestination();
                if(!connections.containsKey(destination)){
                    connections.put(destination,obj);
                    queue.add(destination);
                }
            }
        }
        if(!connections.containsKey(to)){
            return null;
        }
        return gatherPath(from, to, connections);
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connections){
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;
        while(!current.equals(from)){
            T next = connections.get(current);
            Edge<T> edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return path;
    }

    public List<Edge <T>> getPath(T from, T to){
        Set<T> visited = new HashSet<>();
        Stack<T> path = new Stack<>();
        path.push(from);
        Stack<T> temp = depthFirstSearch(from, to, visited, path);
        List<Edge<T>> list = new ArrayList<>();
        T node1 = null;
        if(!temp.isEmpty()) {
            node1 = temp.pop();
        }
        while(!temp.isEmpty()){
            T node2 = temp.pop();
            if(node2 != null){
                Edge<T> edge = getEdgeBetween(node1, node2);
                list.add(edge);
            }
            node1=node2;
        }
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    private Stack<T> depthFirstSearch(T current, T searched, Set<T> visited, Stack<T> pathSoFar) {
        visited.add(current);
        if (current.equals(searched)) {
            return pathSoFar;
        }
        for (Edge <T> edge : nodes.get(current)) {
            T n = edge.getDestination();
            if(!visited.contains(n)) {
                pathSoFar.push(n);
                Stack<T> p = depthFirstSearch(n, searched, visited, pathSoFar);
                if (!p.isEmpty()) {
                    return p;
                } else {
                    pathSoFar.pop();
                }
            }
        }
        return new Stack<T>();
    }

    private void dfs(T node1, T node2, Set<T> visited){
        visited.add(node1);
        for (Edge <T>edge: nodes.get(node1)){
            if(!visited.contains(edge.getDestination())){
                dfs(edge.getDestination(), node2, visited);
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (T obj : nodes.keySet()) {
            sb.append(obj).append(":").append(nodes.get(obj)).append("\n");
        }
        return sb.toString();
    }
}
