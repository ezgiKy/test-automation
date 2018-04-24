// File to do some "global Variable" to XPath connection and to store some input data
package com.test.util;

public enum Mapping
{

    PAGE_LOAD_TIMEOUT("60"), 
    ELEMENT_EXPECTED_TIME("6"), 
    ELEMENT_LOAD_TIMEOUT("20"),

    BROWSER_MAXIMIZED("true"),
    
    WelcomePageImage("//*[@id='hplogo']"),
    
    SearchTextBox("//*[@id='lst-ib']"),
    
    GoButton("//*[@id='tsf']/div[2]/div[3]/center/input[1]"),

    Downloadpath_Test("C:"), 

    // Dummy End
    Dummy("");

    private final String id;

    private Mapping(final String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }

    private String[] values;

    private Mapping(final String[] values)
    {
        id = "";
        this.values = values;
    }

    public String[] get()
    {
        return values;
    }

    public String get(final int i)
    {
        return values[i];
    }

    public void put(final int i, final String val)
    {
        values[i] = val;
    }

}
