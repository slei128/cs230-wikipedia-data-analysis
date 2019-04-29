/**
 * filename: Reader.java
 * description: Takes a CSV datafile and converts it into an array of Rows
 * date: 01/09/19
 * @author Angelina Li
 *
 * NOTE: Do NOT modify this class.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import fastcsv.*;

/**
 * A utility class for reading in 
 */
public class Reader {

    private final AdjListsGraph<Person> g;
    private final Map<String, Person> nameMap;

    /**
     * Constructor for Reader class. Given a path to the files where wikipedia 
     * edges and nodes are being stored on your local machine, will initialize 
     * a graph containing the Persons in the wikipedia dataset.
     *
     * @param nodesFilepath - A filepath representing the file where the wikipedia
     *                        nodes are being kept. (e.g. pantheon.csv)
     * @param edgesFilepath - A filepath representing the file where the wikipedia
     *                        edges are being kepy (e.g. pantheon_edges.csv)
     */
    public Reader(String nodesFilepath, String edgesFilepath) throws IOException {
        System.out.println("Reading in Pantheon dataset:");
        this.g = new AdjListsGraph<Person>();

        // reading nodes
        System.out.println("==> Reading in vertices...");
        CsvParser nodesParser = getParser(nodesFilepath);
        CsvRow nodeRow;
        this.nameMap = new HashMap<String, Person>();
        while ( (nodeRow = nodesParser.nextRow()) != null) {
            Person person = new Person(
                nodeRow.getField("en_curid"),
                nodeRow.getField("name"),
                nodeRow.getField("birthcity"),
                nodeRow.getField("birthstate"),
                nodeRow.getField("countryName"),
                nodeRow.getField("gender"),
                nodeRow.getField("occupation"),
                nodeRow.getField("industry"),
                nodeRow.getField("domain")
            );
            g.addVertex(person);
            this.nameMap.put(person.getData("name"), person);
        }

        // reading edges
        System.out.println("==> Reading in edges...");
        CsvParser edgesParser = getParser(edgesFilepath);
        CsvRow edgeRow;
        int edgesParsed = 0;
        while( (edgeRow = edgesParser.nextRow()) != null) {
            String fromName = edgeRow.getField("from_name");
            String toName = edgeRow.getField("to_name");

            Person fromPerson = this.nameMap.get(fromName);
            Person toPerson = this.nameMap.get(toName);
            g.addArc(fromPerson, toPerson);
        }

        System.out.println("==> All done!");
    }

    /**
     * Given a filepath, will return a CsvParser that can be used to iterate 
     * over the rows of the dataset. Assumes that the file contains a header,
     * and that the data are in UTF_8 format.
     *
     * @throws IOException
     * @param filepath - the filepath to this file.
     * @return the parser over this file.
     */
    private static CsvParser getParser(String filepath) throws IOException {
        File file = new File(filepath);
        CsvReader reader = new CsvReader();
        reader.setContainsHeader(true);
        CsvParser parser = reader.parse(file, StandardCharsets.UTF_8);
        return parser;
    }

    /**
     * Returns a version of the graph that this Reader class compiles.
     * @return the graph that this Reader has compiled.
     */
    public AdjListsGraph<Person> getGraph() {
        return this.g;
    }

    /**
     * Returns a mapping between names and Person objects for all Persons
     * parsed by this Reader. Note that names are unique in this dataset.
     * @return a Map between each name in the dataset and the Person that name
     *         corresponds to.
     */
    public Map<String, Person> getNameMap() {
        return this.nameMap;
    }
    
}
