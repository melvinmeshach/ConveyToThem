package com.ctt.ofyservices;

/**
 * Created by ronak on 22/03/17.
 */
public class OfyFilter
{
    public String parameter;
    public String value;
    public String operation = "==";

    public OfyFilter(String parameter, String value, String operation)
    {
        this.parameter = parameter;
        this.value     = value;
        this.operation = operation;
    }
}
