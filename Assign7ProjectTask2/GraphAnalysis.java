import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

/**
 * GraphAnalysis analyzes and produces insights upon a dataset consisting of the Wikipedia pages 
 * of famous people and the hyperlinks between them. 
 *
 * @author Shirley Lei
 * @version April 25th, 2019
 */
public class GraphAnalysis {
    private Reader graphReader;
    private AdjListsGraph<Person> graph;
    private static final int SUBSET_SIZE = 1000;
    /**
     * First constructor for GraphAnalysis, which reads in and gets a graph of a FULL dataset of Wikipedia pages upon which analysis is performed.
     * @throws IOException  if an exception occurred when reading in dataset
     */
    public GraphAnalysis() throws IOException {
        this.graphReader =  new Reader("datasets/pantheon_nodes_all.csv", "datasets/pantheon_edges_all.csv");
        this.graph = graphReader.getGraph();
    }
    
    /**
     * Second constructor for GraphAnalysis, which reads in and gets a graph of a SUBSET dataset of Wikipedia pages (1000 entries) upon which analysis is performed.
     * @param numRows the number of rows in the subdata set (for this assignment, this is fixed at SUBSET_SIZE (which is 1000))
     * @throws IOException  if an exception occurred when reading in dataset
     */
    public GraphAnalysis(int numRows) throws IOException {
        this.graphReader =  new Reader("datasets/pantheon_nodes_1000.csv", "datasets/pantheon_edges_1000.csv");
        this.graph = graphReader.getGraph();
    }
    
    /**
     * Helper method to find which person or persons has/have the highest out-degree.
     * @return people who have the highest out-degree. 
     */
    private LinkedList<String> getMaxOutPersons() {
        LinkedList<Person> maxPersons = new LinkedList<Person>();

        int maxNumArcs = 0; 
        int numEqualMaxPersons = 0;
        Person maxPerson = graph.getVertices().iterator().next();
        maxPersons.add(maxPerson);

        for (Person p: graph.getVertices()) {
            if (graph.getSuccessors(p).size()>graph.getSuccessors(maxPersons.getFirst()).size()) {
                maxPersons.clear();
                maxPersons.add(p);
                maxNumArcs = graph.getSuccessors(p).size();
            } else if (graph.getSuccessors(p).size()==graph.getSuccessors(maxPersons.getFirst()).size()) {
                maxPersons.add(numEqualMaxPersons+1, maxPerson); 
            }   
        }
        //format maxPersons to output only their names, rather than entire Person objects
        LinkedList<String> maxPersonNames = new LinkedList<String>();
        for (Person p: maxPersons) {
            maxPersonNames.add(p.getData("name"));
        }
        return maxPersonNames;
    }
    
    /**
     * Helper method to determine how many nodes have out-degree of a given number
     * @param degree the number of degrees to determine how many nodes there are of that number of degrees
     * @return the number of nodes with the given degrees
     */
    private int getNumNodes(int degree) { 
        //initalize number of nodes with given degree to 0
        int counter = 0;
        for (Person p: graph.getVertices()) {
            //increase counter if the size of the current person's successors is same as given degrees
            if (graph.getSuccessors(p).size() == degree) {
                counter+=1;
            }
        }
        return counter;
    }

     /**
     * Helper method to find the person with max number of in-degrees 
     * 
     * @return the name of the person with max number of in-degrees
     */
    private String getMaxInPerson() { 
        //initialize hashtable to store max in-degree person
        Hashtable<Person, Integer> maxPersonHash = new Hashtable<Person, Integer>();        
        //initialize max in-degrees number to zero and a random person, and put that person-degrees into the hashtable
        int maxNumInArcs = 0;  
        Person maxInPerson = graph.getVertices().iterator().next();
        maxPersonHash.put(maxInPerson, maxNumInArcs); 
        for (Person p: graph.getVertices()) {
            if (graph.getPredecessors(p).size()>maxPersonHash.get(maxInPerson)) {
                //set new person-degrees into max person hash if their in-degrees is higher than current
                maxInPerson = p;
                maxNumInArcs = graph.getPredecessors(p).size();
                maxPersonHash.clear();
                maxPersonHash.put(maxInPerson, maxNumInArcs);
            }
        }
        //Get the name of the max in-degrees person from the hashtable
        String maxName = maxPersonHash.keys().nextElement().getData("name");
        return maxName;
    }
    
     /**
     * Helper method to find the woman with largest number of in-degrees 
     * 
     * @return the Person with max number of in-degrees
     */
    private Person getMaxWoman() {
        //Create a vector of all women in the dataset
        Vector<Person> allWomen = new Vector<Person>();
        for (Person p: graph.getVertices()) {
            if (p.getData("gender").equals("Female")){
                allWomen.add(p); //add person to allWomen if their gender is Female
            }
        }
        
        Person maxWoman = allWomen.firstElement(); //set random female person as maxWoman
        int maxDegrees = 0; //initialize max degrees
        for (Person p: allWomen) {
            if (graph.getPredecessors(p).size()>maxDegrees) {
                maxWoman = p;
                maxDegrees = graph.getPredecessors(p).size();
            }
        }
        return maxWoman; 
        //Note design decision: I return the entire Person object rather than just her name, so that I can get other details
        //of this person in the driver method (such as the number of in-degrees she has)
    }
    
    /**
     * Helper method to find number of men with larger number of in-degrees than that of the max in-degrees woman
     * @param maxWomanInDegrees the number of in-degrees of the woman with the biggest in-degrees
     * @return number of men with larger number of in-degrees than that of the max in-degrees woman
     */
    private int getNumMenGreaterThan(int maxWomanInDegrees) {
        //intialize number of men
        int numMenGreater = 0;
        for (Person p: graph.getVertices()) {
            if (p.getData("gender").equals("Male")) { 
                if (graph.getPredecessors(p).size()>maxWomanInDegrees) {
                    //increment numMenGreater if the current man has greater in-degrees than maximum woman
                    numMenGreater+=1; 
                }
            }
        }
        return numMenGreater;
    }
    
    /**
     * Helper method to find the shortest path between two given people names
     * @param p1 p2 two people's names for whom to find shortest path between 
     * @return the people that make up the shortest path between the two people
     */
    private Vector<String> getShortestPath(String p1, String p2) throws IOException {
        //Create the Person objects from the string names
        Person person1 = graphReader.getNameMap().get(p1);
        Person person2 = graphReader.getNameMap().get(p2);

        //Initialize shortestPathLength to large number
        int shortestPathLength = Integer.MAX_VALUE;
        //Initialize shortestPath to random path(in this case, path to the person herself)
        LinkedList<Person> shortestPath = graph.breadthFirstSearch(person1).getFirst();

        LinkedList<LinkedList<Person>> allPaths = graph.breadthFirstSearch(person1);
        for (LinkedList<Person> path: allPaths) {
            //System.out.println("The path looks like this: " + path);
            if (path.contains(person2)) {
                if (path.size()<shortestPathLength) {
                    shortestPathLength = path.size();
                    shortestPath = path;
                }
            }
        }
        //format this path to names only, rather than person objects
        Vector<String> pathNames = new Vector<String>();
        for (Person p: shortestPath) {
            pathNames.add(p.getData("name"));
        }
        return pathNames;
    }

    /**
     * Helper method to find the total number of connections within each country (my personal question)
     * @return all countries in the dataset and the number of connections within each of them
     */
    private Hashtable<String, Integer> getNumConnectionsWithinEachCountry() {
        //Hashtable contains entries of each country name and their corresponding number of connections 
        Hashtable<String, Integer> countriesStats = new Hashtable<String, Integer>();
        int initialConnectionCount = 1;
        for (Person p: graph.getVertices()) {
            String personCountry = p.getData("countryName"); //record the country that the current person is from
            if (countriesStats.containsKey((personCountry))) {//if the country is already existing in our hashtable:
                int currentConnectionCount = (countriesStats.get(personCountry)); 
                //System.out.println("currentConnectionCount is " + currentConnectionCount);
                countriesStats.replace(personCountry, currentConnectionCount+1);
            } else { //if it's a country unseen yet, add that country to our hashtable and initialize its count to 1:
                countriesStats.put(p.getData("countryName"), initialConnectionCount);
            }
        }
        return countriesStats;
    }
     
    /**
     * Helper method to find the farthest people from a given person
     * @param originPerson the person from whom we are trying to find the farthest people
     * @return the people that are farthest people from the given person
     */
    private LinkedList<String> getFarthestPeople(String originPerson) throws IOException {
        //Create the Person objects from the string names
        Person startPerson = graphReader.getNameMap().get(originPerson);
        //Initialize information for the longest path people, to be filled in after
        int longestPathLength = 0;
        LinkedList<String> longestPathPeople = new LinkedList<String>();
        //Iterate through all paths from given origin person, and compare the length of each path with the longest path length so far 
        LinkedList<LinkedList<Person>> allPaths = graph.breadthFirstSearch(startPerson);
        for (LinkedList<Person> path: allPaths) {
            //update or maintain the longest path length so far
            if (path.size()>=longestPathLength) {
                longestPathLength = path.size();
            }
        }
        //Find people who are exactly the longest path length's distance away from the origin person,
        //and add the names of those people to the output longestPathPeople
        for (LinkedList<Person> path: allPaths) {
            if (path.size() == longestPathLength) {
                longestPathPeople.add(path.getLast().getData("name"));
            }
        }
        return longestPathPeople;
    }
    
    /**
     * Helper method to find the expected in-group link fraction for given group (fraction of all nodes which are part of a given group)
     * @param dataField type the field in the data set that we are examining, and the specified type within that field (e.g. "gender", "Female)
     * @return the fraction of all nodes which are part of a given group
     */
    private double getFraction(String dataField, String type) {
        //Find total number of people in the graph
        int total = graph.getNumVertices();
        //System.out.println("The total is " + total);
        //Create a vector to filter for the specificed type only (e.g. "Female" or "ARTS")
        Vector<Person> allWithinType = new Vector<Person>();
        for (Person p: graph.getVertices()) {
            if (p.getData(dataField).equals(type)){
                allWithinType.add(p);
            }
        }
        //Get amount of people in that type
        int numOfType = allWithinType.size();
        //System.out.println("The number of this type is " + numOfType);
        //Divide amount by total to get the final fraction
        double outFraction = (double) numOfType / (double) total;
        return outFraction;
    }

     /**
     * Helper method to find the average in-group link fraction across the whole given group 
     * (meaning, the fraction of links on that page which go to other pages in the group)
     * @param dataField type the field in the data set that we are examining, and the specified type within that field (e.g. "gender", "Female)
     * @return the averaged in-group fraction
     */
    private double getAverageInGroupLinkFractions(String dataField, String type) {
        Hashtable<Person, Double> hashes = new Hashtable<Person, Double>(); //make a hashtable later used to map people to their fractions
        for (Person p: graph.getVertices()) {
            int totalConnections = graph.getSuccessors(p).size();
            int count = 0; //initialize the number of successors who are within the same group as the current person to 0
            double inGroupLinkFraction;
            if (p.getData(dataField).equals(type)){
                for (Person successor: graph.getSuccessors(p)) {
                    if (successor.getData(dataField).equals(type)) {
                        count += 1; //increment count if the successor is within the same group as the current person
                    }
                }
                inGroupLinkFraction = (double) count / (double) totalConnections;
                //System.out.println("The in group link fraction for current person is: " + inGroupLinkFraction);
                hashes.put(p, inGroupLinkFraction); //store the person and their corresponding fraction 
            }
        }
        double totalofFractions = 0;
        //System.out.println("the fractions are: " + hashes.values());
        for (double fraction: hashes.values()) {
            if (!(Double.isNaN(fraction))) {
                //System.out.println("current fraction: " + fraction);
                totalofFractions = totalofFractions + fraction;
            }
        }
        //System.out.println("total of fractions: " + totalofFractions);
        double avgInGroupLinkFraction = totalofFractions/hashes.size();
        //System.out.println("THE AVERAGE IS : " + avgInGroupLinkFraction);
        return avgInGroupLinkFraction;
    }

    /**
     * Driver method to perform numerous analysis on the dataset. 
     *
     * @param  args command line arguments (unused in this case)
     */
    public static void main(String[] args) throws IOException {   
        //Graph analysis on SUBSET of 1000 rows
        GraphAnalysis g = new GraphAnalysis(SUBSET_SIZE);
        
        //Graph analysis on the ENTIRE dataset: (UNCOMMENT LINE BELOW TO RUN)
        //GraphAnalysis g = new GraphAnalysis();
        
        System.out.println("\n----------------------------------------------------------STEP  1: Getting to know your data-----------------------------------------------------------");

        //1. Which node or nodes in the graph has the highest out-degree? 
        System.out.println("\n(1) The highest out-degree person(s) is/are:" + g.getMaxOutPersons());

        //2. How many nodes have out-degree 0? How many have out-degree 1?
        System.out.println("\n(2a) The number of people that have out-degree 0 is: " + g.getNumNodes(0));
        System.out.println("(2b) The number of people that have out-degree 1 is: " + g.getNumNodes(1));
        //Testing:
        //System.out.println("The number of nodes that have out-degree 32 is (expect 1): " + g.getNumNodes(32) + "\n");
        //System.out.println("The number of nodes that have out-degree 100 is (expect 0): " + g.getNumNodes(100) + "\n");

        //3. Which node in the graph has the highest in-degree? 
        System.out.println("\n(3) The person with highest in-degree is: " + (g.getMaxInPerson()) + "\n");

        //4. Which woman has the highest in-degree in the graph? How many men have higher in-degrees than her?
        Reader testerGraphReader =  new Reader("datasets/pantheon_nodes_1000.csv", "datasets/pantheon_edges_1000.csv");
        //Reader testerGraphReader =  new Reader("datasets/pantheon_nodes_all.csv", "datasets/pantheon_edges_all.csv");
        AdjListsGraph<Person> testerGraph = testerGraphReader.getGraph();
        int numInDegrees = testerGraph.getPredecessors(g.getMaxWoman()).size();
        
        System.out.println("\n(4a) The woman with highest in-degree is: " 
            + g.getMaxWoman().getData("name") + ", with " + numInDegrees + " in-degrees.");
        System.out.println("\n(4b) The number of men with higher in-degrees than her is: " 
            + g.getNumMenGreaterThan(numInDegrees));

        //5. Question of my choice: I chose to examine the total number of connections within each country.
        System.out.println("\n(5) Question of my choice: Total number of connections within each country.\n\nI was interested in discovering the number of connections within each country,"); 
        System.out.println("since this number can represent how 'well connected' that particular country is, relative to other countries --");
        System.out.println("and I think there is a possible positive correlation between the level of connectedness of people within a country");
        System.out.println("and the degree to which knowledge, information, and ideas sharing happens within that country.");
        
        System.out.println("\nOUTPUT STATS on the number of connections within each country:\n-->" 
              + g.getNumConnectionsWithinEachCountry());
        System.out.println("\nPARTICULAR INSIGHTS on specific countries that I am interested in:\n"
            + "-->United States has " + (g.getNumConnectionsWithinEachCountry().get("United States")+g.getNumConnectionsWithinEachCountry().get("UNITED STATES")) + " connections\n"
            + "-->United Kingdom has " + (g.getNumConnectionsWithinEachCountry().get("United Kingdom")+g.getNumConnectionsWithinEachCountry().get("UNITED KINGDOM")) + " connections\n"
            + "-->China has " + (g.getNumConnectionsWithinEachCountry().get("China")+g.getNumConnectionsWithinEachCountry().get("CHINA")) + " connections\n"
            + "-->Uganda has " + g.getNumConnectionsWithinEachCountry().get("Uganda")+ " connection\n");
        
        System.out.println("*Note: this dataset of people connections is most likely biased towards Western countries since it features Wikipedia, a Western source.");
        System.out.println("Thus Western countries are expected to have a skewed-higher number of connections relative to the rest of countries.\n");


        System.out.println("\n--------------------------------------------------STEP  2: Finding shortest paths between nodes-----------------------------------------------");
        
        //1. Find the shortest path between "Madeleine Albright" '59 and "J. R. R. Tolkien". 
        System.out.println("\n(1a)The shortest path between Madeleine Albright'59 and J.R.R. Tolkien is:\n"
            + g.getShortestPath("Madeleine Albright", "J. R. R. Tolkien") 
            + ".This path is " + (g.getShortestPath("Madeleine Albright", "J. R. R. Tolkien").size()-1) + " steps.");
        
        System.out.println("\n(1b) Pair of my choice is Bill Gates and Stephen King. The shortest path between Bill Gates and Stephen King is:\n"
            + g.getShortestPath("Bill Gates", "Stephen King")
            + ".This path is " + (g.getShortestPath("Bill Gates", "Stephen King").size()-1) + " steps.");
        
        //2.What person or persons are farthest away from Secretary Albright?
        System.out.println("\n(2)The farthest person(s) from Madeleine Albright'59 is/are:\n"
            + g.getFarthestPeople("Madeleine Albright"));

        System.out.println("\n\n-------------------------------------STEP 3: (Extra Credit) Bias in the connectivity of Wikipedia pages-------------------------------------\n");

        System.out.println("(1) Fractions of nodes of each gender");
        System.out.println("Female: " + g.getFraction("gender","Female"));
        System.out.println("Male: " + g.getFraction("gender","Male"));

        System.out.println("\n(2) Interpretation of the representation index:\n");
        System.out.println("The representation index defines how many times more/less likely a page is to link in-group than the page is to link to any random article.");
        System.out.println("If representation index = 1, it means a page is just as likely to link to a woman as the fraction of women articles that exist in the dataset (same goes for to men articles)."); 
        System.out.println("This would mean that a woman article is just as likely to link to a woman article as to a random article.");
        System.out.println("If the representation index of a certain group > 1, for example if it equals 2, " + 
            "then a page in that group is *twice* as likely to link in-group than to link to a random article  (i.e. MORE likely to link in-group).");
        System.out.println("If the representation index of a certain group < 1, for example if it equals 0.75, " + 
            "then a page in that group is *3/4* times as likely to link in-group than to link to a random article (i.e. LESS likely to link in-group).");
        
        System.out.println("\n(3a) Average in-group link fractions for each gender:"); 
        System.out.println("Female: " + g.getAverageInGroupLinkFractions("gender","Female"));
        System.out.println("Male: " + g.getAverageInGroupLinkFractions("gender","Male"));

        System.out.println("\n(3b) Representation indices for each gender:");
        double repIndexFemale = g.getAverageInGroupLinkFractions("gender","Female")/(g.getFraction("gender","Female")); 
        System.out.println("Female: " + repIndexFemale);
        double repIndexMale = g.getAverageInGroupLinkFractions("gender","Male")/(g.getFraction("gender","Male"));
        System.out.println("Male: " + repIndexMale);
        
        String[] domains = new String[] {"INSTITUTIONS","EXPLORATION","ARTS","SCIENCE & TECHNOLOGY","SPORTS","BUSINESS & LAW","HUMANITIES","PUBLIC FIGURE"};
        
        System.out.println("\n(4a) Fractions of nodes of each domain");
        for (int i=0; i<domains.length; i++) {
            double fraction = g.getFraction("domain",domains[i]); 
            System.out.println(domains[i] + ": " + fraction);
        }

        System.out.println("\n(4b) Average in-group link fractions for each domain");
        for (int i=0; i<domains.length; i++) {
            double avgInGroupLinkFractions = g.getAverageInGroupLinkFractions("domain",domains[i]); 
            System.out.println(domains[i] + ": " + avgInGroupLinkFractions);
        }

        System.out.println("\n(4c) Representation indices for each domain");
        for (int i=0; i<domains.length; i++) {
            double repIndex = g.getAverageInGroupLinkFractions("domain",domains[i])/(g.getFraction("domain",domains[i])); 
            System.out.println(domains[i] + ": " + repIndex);
        }

        System.out.println("\n(5) Interpretation and commentary of results:");
        System.out.println("\nLooking at the subset dataset, first of all, it is saddening but also not surprising women are so " + 
            "underrepresented in Wikipedia articles (only 13% of the articles are of women). " +
            "\nHowever, it is very striking that women articles have a 3x representation index, meaning that women are 3 times as likely " + 
            "to link to other women than to a random article. \nThis might mean that women are highly connected with other women, " + 
            "relative to how connected they are with men. This is an interesting insight that powerful women \nare well connected with " +
            "other powerful women -- potentially there is a reverse causation there that powerful women become powerful due to their " + 
            "connections with other powerful women, \nbut obviously there is no enough evidence to draw this conclusion and therefore " +
            "this is just a provoking thought. \nIt is also interesting that men articles (around 0.8 representation index) are less" +
            " likely to link to other men than to a random article. \nThis is a surprising fact, and it may be due to Wikipedia’s efforts" +
            " to include more links to women within existing men’s pages. \nWithin domains, the representation index of 'exploration'" +
            "is an extreme outlier: people within exploration domain are \n25x more likely to link in-group (whereas the average for " + 
            "other groups is around 4x). There is not enough information on why this is, but it would be interesting \nto learn about the " + 
            "characteristics of this “exploration” field which makes the field so much more tightly connected.");
    }
}
