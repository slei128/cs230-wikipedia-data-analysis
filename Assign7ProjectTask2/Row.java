/**
 * filename: Row.java
 * description: Initializes Row class
 * date: 01/09/19
 * @author Angelina Li
 *
 * NOTE: Do NOT modify this class.
 */

import java.util.HashMap;

public class Row {
    
    private final String name, state;
    private final int year;
    private final HashMap<String, Double> data;

    /**
     * Constructor for Row class. 
     * Each Row represents data on the city-year level.
     */
    public Row(
        String name,
        String state,
        int year,
        double population,
        double renterOccupiedHouseholds,
        double percRenterOccupiedHouseholds,
        double povertyRate,
        double percWhite,
        double percAfricanAmerican,
        double percHispanic,
        double percNativeAmerican,
        double percAsian,
        double percNHPI,
        double percMultiple,
        double percOther,
        double evictionNumber,
        double evictionRate,
        double evictionFilingNumber,
        double evictionFilingRate
    ) {
        this.name = name;
        this.state = state;
        this.year = year;

        this.data = new HashMap<String, Double>();
        this.data.put("population", population);
        this.data.put("renterOccupiedHouseholds", renterOccupiedHouseholds);
        this.data.put("povertyRate", povertyRate);
        this.data.put("percWhite", percWhite);
        this.data.put("percAfricanAmerican", percAfricanAmerican);
        this.data.put("percHispanic", percHispanic);
        this.data.put("percNativeAmerican", percNativeAmerican);
        this.data.put("percAsian", percAsian);
        this.data.put("percNHPI", percNHPI);
        this.data.put("percMultiple", percMultiple);
        this.data.put("percOther", percOther);
        this.data.put("evictionNumber", evictionNumber);
        this.data.put("evictionRate", evictionRate);
        this.data.put("evictionFilingNumber", evictionFilingNumber);
        this.data.put("evictionFilingRate", evictionFilingRate);
    }

    /**
     * Getter method for name variable.
     * @return name data for this Row instance.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Getter method for state variable.
     * @return state data for this Row instance.
     */
    public final String getState() {
        return this.state;
    }

    /**
     * Getter method for year variable.
     * @return year data for this Row instance.
     */
    public final int getYear() {
        return this.year;
    }

    /**
     * Getter method for all numerical values associated with this row.
     * @return numerical value associated with this row.
     */
    public final double getDataValue(String variableName) {
        if( this.data.containsKey(variableName) ) {
            return this.data.get(variableName);
        }
        throw new IllegalArgumentException(
            "Row objects do not contain the variable '" + variableName + "'!");
    }

    /**
     * Returns a String representing all the data in this Row instance.
     @return a String representation of this Row.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("name=" + this.name + ", ");
        sb.append("state=" + this.state + ", ");
        sb.append("year="); sb.append(String.valueOf(this.year));
        sb.append("}");
        return sb.toString();
    }
}