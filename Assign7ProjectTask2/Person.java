/**
 * filename: Person.java
 * description: Initializes Row class
 * date: 02/26/19
 * @author Angelina Li
 *
 * NOTE: Do NOT modify this class.
 */

import java.util.HashMap;
import java.util.Map;


public class Person {
    
    private final Map<String, String> personData;

    public Person(
        String id,
        String name,
        String birthCity,
        String birthState,
        String countryName,
        String gender,
        String occupation,
        String industry,
        String domain
    ) {
        this.personData = new HashMap<String, String>();
        this.personData.put("id", id);
        this.personData.put("name", name);
        this.personData.put("birthCity", birthCity);
        this.personData.put("birthState", birthState);
        this.personData.put("countryName", countryName);
        this.personData.put("gender", gender);
        this.personData.put("occupation", occupation);
        this.personData.put("industry", industry);
        this.personData.put("domain", domain);
    }

    /**
     * Getter method for data variable.
     * @return requested data value for this Person instance.
     */
    public final String getData(String variable) {
        return this.personData.get(variable);
    }

    @Override
    public boolean equals(Object obj) {
        if ( ! (obj instanceof Person)) { return false; }
        final Person that = (Person) obj;
        return this.personData.equals(that.personData);
    }

    @Override
    public int hashCode() {
        return this.personData.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person {");
        sb.append("\n\tid=" + this.getData("id"));
        sb.append("\n\tname=" + this.getData("name"));
        sb.append("\n\tbirthCity=" + this.getData("birthCity"));
        sb.append("\n\tbirthState=" + this.getData("birthState"));
        sb.append("\n\tcountryName=" + this.getData("countryName"));
        sb.append("\n\tgender=" + this.getData("gender"));
        sb.append("\n\toccupation=" + this.getData("occupation"));
        sb.append("\n\tindustry=" + this.getData("industry"));
        sb.append("\n\tdomain=" + this.getData("domain"));
        sb.append("\n}");
        return sb.toString();
    }

}