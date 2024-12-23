/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops2412.callbacks;
   
import static com.mycompany.saipops2412.Constants.*;
import static com.mycompany.saipops2412.HeuristicEnum.*;
import static com.mycompany.saipops2412.Parameters.*;
import com.mycompany.saipops2412.constraints.Attributes;
import com.mycompany.saipops2412.constraints.LowerBoundConstraint;
import com.mycompany.saipops2412.heuristics.SaiBASE_heuristic;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class SAIPOPS_Callback extends IloCplex.BranchCallback{
    
    
    private        TreeMap<String, IloNumVar>  mapOfAllVariablesInTheModel ;
    private        TreeMap<Integer, HashSet<LowerBoundConstraint>>  mapOfAllConstraintsInTheModel;
    private    TreeMap<String, Double>  objectiveFunctionMap =null; 
  
     
    public SAIPOPS_Callback (  
               TreeMap<String, Double>  objectiveFunctionMap,
            TreeMap<String, IloNumVar>  mapOfAllVariablesInTheModel ,
            TreeMap<Integer, HashSet<LowerBoundConstraint>> mapOfAllConstraintsInTheModel 
            
            ){
        
         
         this. objectiveFunctionMap = objectiveFunctionMap;
         this.mapOfAllVariablesInTheModel=mapOfAllVariablesInTheModel;
         this.mapOfAllConstraintsInTheModel=mapOfAllConstraintsInTheModel;
        
    }
        
    @Override
    protected void main() throws IloException {
        
        if ( getNbranches()> ZERO ){  
            
            String branchingVar = null;
            
            //get fixed and fractional vars
            TreeMap<String, Boolean> fixings = new  TreeMap<String, Boolean>();
            TreeMap<String, Double>  freeVariables = new TreeMap<String, Double>  ();
            TreeSet <String> fractionalVariables = new TreeSet <String> ();
            getFreeAndFixedVars (freeVariables, fixings, fractionalVariables) ;
            
            //get branching recommendation
            try {
                
                Set< Attributes>  smallestConstraints = 
                        new HashSet  < Attributes>  ();
                final int LOWEST_KNOWN_PRIMARY_DIM = getSmallestConstraints (
                        smallestConstraints,  
                        fixings ,   
                        fractionalVariables);
                
                 
                String thisNodeID=getNodeId().toString();
                if (thisNodeID.equals( MIPROOT_NODE_ID)){
                    //root node
                    if (HEURISTIC_TO_USE.equals(SAIPOPS_BCP) && LOWEST_KNOWN_PRIMARY_DIM==ONE) {
                        //update objectives for all primary vars at the lowest dim
                    }
                } 
                
           
                 
                
                branchingVar = (new SaiBASE_heuristic (smallestConstraints, LOWEST_KNOWN_PRIMARY_DIM)) .getBranchingVariable();
           
             

                //overrule cplex branching
                if ( ! HEURISTIC_TO_USE.equals(PURE_CPLEX)) overruleCplexBranching (branchingVar) ; 
                
                
            } catch (Exception ex ){
                System.err.println( ex);
                ex.printStackTrace();
                exit(ONE);
            }
            
        }
    }
    
     
            
     
    
   
        
    
    //collect  candidates
    //return the dim
    private int getSmallestConstraints (Set<  Attributes> candidates, 
            TreeMap<String, Boolean> fixedVariables ,   
            TreeSet <String> fractionalVariables){
        
        int LOWEST_KNOWN_PRIMARY_DIM   = BILLION;
        
        //walk thru all the constraints to collect information
        for (HashSet<LowerBoundConstraint> lbcSet : mapOfAllConstraintsInTheModel.values()){
            for (LowerBoundConstraint lbc : lbcSet){
                LowerBoundConstraint lbc_LocalCopy = lbc.getCopy();
                lbc_LocalCopy= lbc_LocalCopy.applyKnownFixings(fixedVariables, fractionalVariables);
               
                if (null != lbc_LocalCopy){
                    
                    Attributes attr = lbc_LocalCopy.getAttributes();
                    
                    if (attr.highestObjMagn_forAnyFractionalPrimaryVar < ZERO){
                        //skip over constraint that has no fractional primary variables
                        continue; 
                    }
                    
                    if (attr.dimension <LOWEST_KNOWN_PRIMARY_DIM ){
                        LOWEST_KNOWN_PRIMARY_DIM = attr.dimension;
                        candidates.clear();
                        candidates.add(  attr);
                    } else if (attr.dimension ==LOWEST_KNOWN_PRIMARY_DIM) {
                        candidates.add (  attr );
                    }
                    
                }
                
            }
            
        }
        
        return   LOWEST_KNOWN_PRIMARY_DIM     ;
        
    }
       
    private  void getFreeAndFixedVars (  
             TreeMap<String, Double>  freeVariables,
              TreeMap<String, Boolean> fixings,
              TreeSet <String> fractionalvariables) throws IloException {
       
        IloNumVar[] allVariables = new  IloNumVar[mapOfAllVariablesInTheModel.size()] ;
        int index =ZERO;
        for  (Map.Entry <String, IloNumVar> entry : mapOfAllVariablesInTheModel.entrySet()) {
            //
            allVariables[index++] = entry.getValue();
        }
        
        double[] varValues = getValues (allVariables) ;
        IloCplex.IntegerFeasibilityStatus [] status =   getFeasibilities(allVariables);
        
        index =-ONE;
        for (IloNumVar var: allVariables){
            index ++;
            
            Double ub = getUB(var) ;
            Double lb = getLB(var) ;
            if (  status[index].equals(IloCplex.IntegerFeasibilityStatus.Infeasible)){
                freeVariables.put  (var.getName(),varValues[index] ) ;    
                fractionalvariables.add( var.getName());
            }else if (HALF < Math.abs (lb-ub) ) {
                freeVariables.put  (var.getName(),varValues[index] ) ;     
                                
            } else {
                
                fixings.put (var.getName(), varValues[index] > HALF) ;
                
                //System.err.println(var.getName() +" fixed at "+ varValues[index] + " has lb "+lb + " and ub "+ub) ;
                
            }            
        }
               
    }
    
    private void  overruleCplexBranching(String branchingVarName ) throws IloException {
        IloNumVar[][] vars = new IloNumVar[TWO][] ;
        double[ ][] bounds = new double[TWO ][];
        IloCplex.BranchDirection[ ][]  dirs = new  IloCplex.BranchDirection[ TWO][];
        getArraysNeededForCplexBranching(branchingVarName, vars , bounds , dirs);

        //create both kids 

        double lpEstimate = getObjValue();
        IloCplex.NodeId zeroChildID =  makeBranch( vars[ZERO][ZERO],  bounds[ZERO][ZERO],
                                              dirs[ZERO][ZERO],  lpEstimate  );
        IloCplex.NodeId oneChildID = makeBranch( vars[ONE][ZERO],  bounds[ONE][ZERO],
                                                 dirs[ONE][ZERO],   lpEstimate );
        
        
        //System.out.println("Zero child "+ zeroChildID);
        //System.out.println("One child "+ oneChildID);
        
    }
    
    private void getArraysNeededForCplexBranching (String branchingVar,IloNumVar[][] vars ,
                                                   double[ ][] bounds ,IloCplex.BranchDirection[ ][]  dirs ){
        
        IloNumVar branchingCplexVar = mapOfAllVariablesInTheModel.get(branchingVar );
                 
        //    System.out.println("branchingCplexVar is "+ branchingCplexVar);
                 
        //get var with given name, and create up and down branch conditions
        vars[ZERO] = new IloNumVar[ONE];
        vars[ZERO][ZERO]= branchingCplexVar;
        bounds[ZERO]=new double[ONE ];
        bounds[ZERO][ZERO]=ZERO;
        dirs[ZERO]= new IloCplex.BranchDirection[ONE];
        dirs[ZERO][ZERO]=IloCplex.BranchDirection.Down;

        vars[ONE] = new IloNumVar[ONE];
        vars[ONE][ZERO]=branchingCplexVar;
        bounds[ONE]=new double[ONE ];
        bounds[ONE][ZERO]=ONE;
        dirs[ONE]= new IloCplex.BranchDirection[ONE];
        dirs[ONE][ZERO]=IloCplex.BranchDirection.Up;
    }
    
}
     
  
 

