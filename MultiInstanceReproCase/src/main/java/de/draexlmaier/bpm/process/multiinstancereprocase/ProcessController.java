package de.draexlmaier.bpm.process.multiinstancereprocase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

@Named("controller")
public class ProcessController
{
    public List<String> getDemoCollection()
    {
        return new ArrayList<String>(Arrays.asList("1", "2", "3"));
    }
}