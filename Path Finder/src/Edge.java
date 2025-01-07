//PROG2 VT2023, Inlämningsuppgift, del1
//Grupp 022
//Palina Boczkowska pabo5408
//Anna Strömberg anst5816


public class Edge<T> {

    private final T destination;
    private final String name;

    private int weight;

    public Edge(T destination, String name, int weight){
        this.destination=destination;
        this.name = name;
        this.weight = weight;
    }

    public T getDestination(){
        return this.destination;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weightParam){
        if(weightParam<0){
            throw new IllegalArgumentException("Wrong! The weight can't be negativ");
        }
        this.weight = weightParam;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "till " + destination + " med " + name + " tar " + weight;
    }
}
