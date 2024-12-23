/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412;

/**
 *
 * @author sst119
 */
public class Parameters {
    
    public static final int  MIP_EMPHASIS =  2;
    
    //cplex config related
    public static final int  HEUR_FREQ  = -1 ;    
    public static final int  FILE_STRATEGY= 3;  
    public static final int MAX_THREADS =  System.getProperty("os.name").toLowerCase().contains("win") ? 1 : 32;
     
    public static final int MAX_PERIODS =3;
    public static final int PERIOD_DURATION_IN_SECONDS =60*60; //60 minute periods
     
    public static final HeuristicEnum HEURISTIC_TO_USE = HeuristicEnum.PURE_CPLEX;
    
    public static final String PRESOLVED_MIP_FILENAME =              
            System.getProperty("os.name").toLowerCase().contains("win") ?
     
    //   "c:\\temp\\mips\\2club200v.pre.sav":
    //       "c:\\temp\\mips\\LTL8.pre.sav": 
    //    "c:\\temp\\mips\\bab1.pre.sav":
    //  "F:\\temporary files here recovered\\bab2.pre.sav":
    //  "F:\\temporary files here recovered\\bab6.pre.sav":
            
    //  "F:\\temporary files here recovered\\ds.pre.sav":    
    
    //  "F:\\temporary files here recovered\\hanoi5.pre.sav":  
            
    //  "F:\\temporary files here recovered\\ivu52.pre.sav":
            
    //  "F:\\temporary files here recovered\\neos-beardy.pre.sav":        
    //  "F:\\temporary files here recovered\\neos-952987.pre.sav":
    //   "F:\\temporary files here recovered\\neos-954925.pre.sav":  
            
    //   "c:\\temp\\mips\\opm2-z10-s4.pre.sav":
    //  "F:\\temporary files here recovered\\opm2-z12-s7.pre.sav":        
    //  "F:\\temporary files here recovered\\opm2-z12-s8.pre.sav":   
    //  "F:\\temporary files here recovered\\opm2-z12-s14.pre.sav":   
            
            
    //   "F:\\temporary files here recovered\\p6b.pre.sav":
    //   "F:\\temporary files here recovered\\protfold.pre.sav":
            
    //  "F:\\temporary files here recovered\\queens-30.pre.sav":
            
    //  "F:\\temporary files here recovered\\rail03.pre.sav":
    //  "F:\\temporary files here recovered\\reblock354.pre.sav":            
    //   "F:\\temporary files here recovered\\rmine10.pre.sav":        
    //  "F:\\temporary files here recovered\\rvb-sub.pre.sav":
            
    //  "F:\\temporary files here recovered\\s100.pre.sav":
    //  "F:\\temporary files here recovered\\s1234.pre.sav":            
    //   "F:\\temporary files here recovered\\seymour-disj-10.pre.sav":      
    //  "F:\\temporary files here recovered\\sorrell3.pre.sav":    
    //  "F:\\temporary files here recovered\\stp3d.pre.sav":
    //  "F:\\temporary files here recovered\\supportcase10.pre.sav":
            
    //  "F:\\temporary files here recovered\\v150d30-2hopcds.pre.sav":
            
    //  "F:\\temporary files here recovered\\wnq.pre.sav":
                                                    
     "C:\\temp\\MIPS\\knapsack.lp":          
     
            
    // MIPs with no objective such as bnatt500        
            
    //Open problems        
     
    "PBO.pre.sav";
    
           
    //for perf variability testing  
    public static final long PERF_VARIABILITY_RANDOM_SEED = 0;
    public static final java.util.Random  PERF_VARIABILITY_RANDOM_GENERATOR =             
            new  java.util.Random  (PERF_VARIABILITY_RANDOM_SEED);   
    
    
}
