/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.constraints;
  
import static com.mycompany.saipops2412.Constants.*;
import com.mycompany.saipops2412.SignificanceEnum;

 



/**
 *
 * @author sst119
 */
public class Triplet implements Comparable<Triplet >  {
     
    public String varName;
    public Double objectiveCoeffcient;
    public Double constraintCoefficient;    
    public boolean isFractional = false;
    public SignificanceEnum significance = SignificanceEnum.SECONDARY ;
            
    public Triplet (String varName,Double constraintCoefficient, Double objectiveCoeffcient ) {
        this.varName = varName;
        this.constraintCoefficient =constraintCoefficient;
        this.objectiveCoeffcient =objectiveCoeffcient;
        
    }    
    
    
    
    public int compareTo(Triplet another) {    
        int result = ZERO;
        double val =  Math.abs (another.constraintCoefficient) -  Math.abs(this.constraintCoefficient) ;
        if (val > ZERO) {
            result = -ONE;
        } else if (val < ZERO){
            result = ONE;
        }  
         
        return result;
    }
    
}
