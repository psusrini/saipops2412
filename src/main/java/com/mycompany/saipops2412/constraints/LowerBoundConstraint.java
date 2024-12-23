/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.constraints;
   
import static com.mycompany.saipops2412.Constants.*;
import com.mycompany.saipops2412.SignificanceEnum;
import static com.mycompany.saipops2412.SignificanceEnum.PRIMARY;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class LowerBoundConstraint {
    
    private final String constraint_Name ;
    private double lowerBound ;
    
    private List <Triplet> coefficientList  =   new   ArrayList <Triplet>  ();    
                
    private double maxLHS = ZERO;
     
    public LowerBoundConstraint (double lowerBound , String name) {
        this.lowerBound = lowerBound;
        constraint_Name = name;
    }
    
    public String toString (){
        
        String result= "\n-----------------------\n LBC " + constraint_Name + ": ";
        for (Triplet triplet : this.coefficientList) {
            result += triplet.constraintCoefficient + " "+ triplet.varName + (triplet.isFractional ?  "(f)": "" ) +   " + ";
        }
        result+= " : "+ this.lowerBound;
        result += " MAX_LHS "+ this.maxLHS;
        return result ;
    }
    
    
    public void add (Triplet triplet ) {       
               
        boolean cond1= (triplet.objectiveCoeffcient > ZERO && triplet.constraintCoefficient > ZERO);
        boolean cond2= (triplet.objectiveCoeffcient < ZERO && triplet.constraintCoefficient < ZERO);
        
        if (cond1 || cond2  ||  triplet.objectiveCoeffcient == DOUBLE_ZERO  )  triplet.significance = SignificanceEnum.PRIMARY;
        
        this.coefficientList .add (triplet) ;  
        
        if (triplet.constraintCoefficient > ZERO) this.maxLHS += triplet.constraintCoefficient;
         
    }
    
   
    
    public void sort () {
       
        //for pessimistic dimensioning
        // sort in natural order 
       
        Collections.sort(this.coefficientList);
         
        
    }
    
    public int getVariableCount () {
        return coefficientList.size();
    }
    
    // copy this constraint into another
    //
    // used by every node in the cplex search tree to get its own copy of every constraint
    //    
    public LowerBoundConstraint getCopy ( ) {
        LowerBoundConstraint twin = new LowerBoundConstraint ( this.lowerBound,this.constraint_Name);
        
        twin.coefficientList .addAll(this.coefficientList);
        twin.maxLHS = this.maxLHS;
       
        return twin;
    }
   
    public Attributes getAttributes (  ) {
        
        Attributes attr = new Attributes ();  
        
        double remainingSlack   = this.maxLHS - this.lowerBound;          
        int numPrimaryVarsExamined   = ZERO;
        
        for (int index = ZERO; index <coefficientList.size(); index ++ ){ 
            
            Triplet triplet = this.coefficientList.get(index);
            double thisCoeffMagnitude =  Math.abs (triplet.constraintCoefficient );
            double thisObjMagnitude =  Math.abs (triplet.objectiveCoeffcient );
            
            if (   triplet.significance.equals(PRIMARY))  {
                
                numPrimaryVarsExamined++;
                
                attr.allPrimaryVars.put( triplet.varName, thisObjMagnitude);
               
                attr.lowestObjMagn_forAnyPrimaryVar = 
                        Math.min (attr.lowestObjMagn_forAnyPrimaryVar, 
                                thisObjMagnitude) ;
                
                remainingSlack -= thisCoeffMagnitude;               
                
                if (remainingSlack == DOUBLE_ZERO){
                    attr.dimension=numPrimaryVarsExamined;
                }else if (remainingSlack < DOUBLE_ZERO){
                    attr.dimension=numPrimaryVarsExamined- ONE;
                }
                
                if (triplet.isFractional){
                    
                    attr.allFractionalPrimaryVars.put( triplet.varName, thisObjMagnitude);
                    
                    if (thisObjMagnitude >attr.highestObjMagn_forAnyFractionalPrimaryVar ){
                        attr.highestObjMagn_FractionalPrimaryVars.clear();
                        attr.highestObjMagn_FractionalPrimaryVars.add(triplet.varName );
                        attr.highestObjMagn_forAnyFractionalPrimaryVar=    thisObjMagnitude;
                    } else if (thisObjMagnitude == attr.highestObjMagn_forAnyFractionalPrimaryVar){
                        attr.highestObjMagn_FractionalPrimaryVars.add(triplet.varName );
                    }                     
                }
                
            } else {
                //secondary var
                if (triplet.isFractional){
                    attr.allFractionalSecondaryVars.put( triplet.varName, thisObjMagnitude);
                }                
            }
            
        }
        
        return   attr  ;  
        
    }
      
    public LowerBoundConstraint applyKnownFixings (TreeMap<String, Boolean> fixings, TreeSet<String> fractionalVariables) {
        this.applyFixings(fixings,fractionalVariables);      
        return this.coefficientList.size () < TWO ?  null: this;
    }
    
    private void applyFixings  (TreeMap<String, Boolean> fixings, TreeSet<String> fractionalVariables ) {
        
        List <Triplet> updated_coefficientList  =   new   ArrayList <Triplet>  ();   
        
        //walk thru coeff list
        for (Triplet triplet:  coefficientList){
            
            Boolean fixedValue = fixings.get( triplet.varName);
            
            if (fractionalVariables.contains(triplet.varName)){
                triplet.isFractional = true;
            } 
            
            if (null!=fixedValue){
                if (  fixedValue){
                    //1 fixed
                    this.lowerBound -= triplet.constraintCoefficient;
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }else {
                    // 0 fixed
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }
                
            }else {
                updated_coefficientList.add (triplet );
            }
        }
        
               
        this.coefficientList= updated_coefficientList;
    }
    
}
