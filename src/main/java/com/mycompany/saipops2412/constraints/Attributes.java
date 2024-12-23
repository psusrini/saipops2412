/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.constraints;
   
import static com.mycompany.saipops2412.Constants.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author sst119
 */
public class Attributes {
          
    //dimension is the number of primary variables that 
    //must be fixed unfavorably to gaurantee that all the other
    //variables in the constraint will be fixed to make the constraint feasible
    //
    //note that dimension 1 leads to BCP
    //
    //note also we use use pessimistic dimensioning
    public int dimension = ZERO ;
        
    public double lowestObjMagn_forAnyPrimaryVar  =BILLION; 
    public double highestObjMagn_forAnyFractionalPrimaryVar  =-ONE; 
    public HashSet<String> highestObjMagn_FractionalPrimaryVars = new HashSet<String> ();
   
    public TreeMap<String, Double> allPrimaryVars             = new TreeMap<String, Double> ();
    public TreeMap<String, Double> allFractionalPrimaryVars   = new TreeMap<String, Double> ();
    public TreeMap<String, Double> allFractionalSecondaryVars = new TreeMap<String, Double> ();
            
}
