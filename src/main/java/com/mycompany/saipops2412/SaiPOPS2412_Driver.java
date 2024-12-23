/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.saipops2412;

import static com.mycompany.saipops2412.Constants.*;
import com.mycompany.saipops2412.constraints.utils.Solver;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author sst119
 */
public class SaiPOPS2412_Driver {
    
    //https://github.com/psusrini/saipops2412.git
    
    private static final Logger logger ;
    private static  IloCplex cplex;
    
    static {
        logger= Logger.getLogger(SaiPOPS2412_Driver.class.getSimpleName() );
        //logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler(SaiPOPS2412_Driver.class.getSimpleName()+ ".log");

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);

            logger.info("Logging initialized.");
            

        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
            exit(ONE);
        }
        
    }

    public static void main(String[] args) throws Exception {
        
        Solver solver = new Solver ( ) ;
        
        solver.solve ( );
        logger.info("Test Completed !");
        
    }
}
