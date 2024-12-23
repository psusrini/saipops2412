/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.heuristics;

import static com.mycompany.saipops2412.Constants.*;
import static com.mycompany.saipops2412.HeuristicEnum.MOM_E;
import static com.mycompany.saipops2412.Parameters.*;
import com.mycompany.saipops2412.constraints.Attributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public    class SaiBASE_heuristic {
    
    protected Set< Attributes>  smallestConstraints;
    protected   int LOWEST_KNOWN_PRIMARY_DIM ;
    
    //identify variables that appear only as primary
      
    protected Map<String, Double>  primaryOnly_WithObjectiveMagnitude = new TreeMap <String, Double> ();
    protected Map<String, Integer> primaryOnly_WithFrequency          = new TreeMap <String, Integer> ();
    
    protected Map<String, Integer> primary_WithFrequency          = new TreeMap <String, Integer> ();
    
    public SaiBASE_heuristic (Set<Attributes>  smallestAttrs, int LOWEST_KNOWN_PRIMARY_DIM){
        this. LOWEST_KNOWN_PRIMARY_DIM = LOWEST_KNOWN_PRIMARY_DIM ;
        smallestConstraints = smallestAttrs;
        identifyPrimaryOnlyVariables();
    }
    
    public    String  getBranchingVariable (   ){
         
        TreeSet<String>  candidates = new TreeSet<String>(); 
         
        if (isEveryVariableZeroObjective (   )|| HEURISTIC_TO_USE.equals(MOM_E) ){
            //use MOMS on primary_WithFrequency
            
            int highest_known_freq = -ONE;
            for (Map.Entry<String, Integer> entry : primaryOnly_WithFrequency.entrySet()  ){
                if (highest_known_freq < entry.getValue()){
                    highest_known_freq = entry.getValue();
                    candidates.clear();
                    candidates.add(entry.getKey());
                } else if (highest_known_freq==entry.getValue()){
                    candidates.add(entry.getKey());
                }
            }
            
        }else  {
            //use POPS on smallestConstraints
            candidates = SaiPOPS_Method.getBranchingCandidates(LOWEST_KNOWN_PRIMARY_DIM, smallestConstraints);
        }
        
        //random tiebreak        
        String[] candidateArray = candidates.toArray(new String[ZERO]);        
        return candidateArray[ PERF_VARIABILITY_RANDOM_GENERATOR.nextInt(candidates.size())];
    }
    
    private    boolean isEveryVariableZeroObjective (   ) {
        boolean result = true;
        
        if (primaryOnly_WithObjectiveMagnitude.isEmpty()){
            result = false;
        } else {
            for (Double val: primaryOnly_WithObjectiveMagnitude.values()){
                if (val >ZERO){
                    result = false;
                    break;
                }
            }
        }
        
        return result;
    }
         
    private void  identifyPrimaryOnlyVariables (){
        
        Set<String> fractionalSecondaryVars = new HashSet <String> ();
        
        for (Attributes attr:  smallestConstraints ){
            //
            for (Map.Entry <String, Double> entry :  attr.allFractionalPrimaryVars.entrySet()){
                primaryOnly_WithObjectiveMagnitude.put(entry.getKey(), entry.getValue());
                Integer current = primaryOnly_WithFrequency.get( entry.getKey());
                if (current ==null) current =ZERO;
                primaryOnly_WithFrequency.put( entry.getKey(), ONE + current);
                primary_WithFrequency.put( entry.getKey()    , ONE + current);
            }
            fractionalSecondaryVars.addAll (attr.allFractionalSecondaryVars.keySet());
             
        }
        
        for (String secondaryVar :  fractionalSecondaryVars){
            primaryOnly_WithObjectiveMagnitude.remove( secondaryVar);
            primaryOnly_WithFrequency.remove( secondaryVar);
        }        
         
    }
}
