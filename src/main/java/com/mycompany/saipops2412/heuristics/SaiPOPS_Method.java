/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.heuristics;

import static com.mycompany.saipops2412.Constants.*;
import com.mycompany.saipops2412.constraints.Attributes;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
class SaiPOPS_Method {
    
    public static   TreeSet<String>   getBranchingCandidates (int lowestKnownDim,   Set< Attributes>  attributeSet  ){
        
        Set< Attributes>  shortenedAttributeSet = new HashSet<Attributes> () ;
        if (lowestKnownDim > ONE){
            //  KnownLOW should be as high as possible
            
            double highestKnownLOW = - ONE;
            for (Attributes attr:  attributeSet){
                if (highestKnownLOW < attr.lowestObjMagn_forAnyPrimaryVar){
                    highestKnownLOW = attr.lowestObjMagn_forAnyPrimaryVar;
                    shortenedAttributeSet.clear();
                    shortenedAttributeSet.add (attr) ;
                }else if (highestKnownLOW == attr.lowestObjMagn_forAnyPrimaryVar){
                    shortenedAttributeSet.add (attr) ;
                }
            }
            
        } else {
            shortenedAttributeSet =  attributeSet;
        }
        
        //now simply select the highest obj magn fractional primary variables from shortenedAttributeSet
        TreeSet<String>  candidates = new TreeSet<String>(); 
         
        double highestKnown  = - ONE;
        for (Attributes attr:  shortenedAttributeSet){
            if (highestKnown < attr.highestObjMagn_forAnyFractionalPrimaryVar){
                highestKnown = attr.highestObjMagn_forAnyFractionalPrimaryVar;
                candidates.clear();
                candidates.addAll (attr.highestObjMagn_FractionalPrimaryVars);
            }else if (highestKnown == attr.highestObjMagn_forAnyFractionalPrimaryVar){
                candidates.addAll (attr.highestObjMagn_FractionalPrimaryVars);
            }
        }
        
        return candidates;
        
    }
    
}
