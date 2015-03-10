/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Solver;

/**
 *
 * @author felipe
 */
/* demo.java */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lpsolve.*;

public class Demo {

	public Demo() {
	}

	public int execute() throws LpSolveException {
          LpSolve lp;
          int Ncol, j, ret = 0;

          /* We will build the model row by row
             So we start with creating a model with 0 rows and 2 columns */
          Ncol = 2; /* there are two variables in the model */

          /* create space large enough for one row */
          int[] colno = new int[Ncol];//{x y }
          double[] row = new double[Ncol];//{x.0 y.0 }

          lp = LpSolve.makeLp(0, Ncol);
          System.out.println("get lp "+lp.getLp());
          if(lp.getLp() == 0)// esto se usa para drse cuenta si contruyo el lp si retorna 0 es por que no  funciono y lo que hace es cambiar el flag  
            ret = 1; /* couldn't construct a new model... */

          if(ret == 0) {// si se cumple esto es por que ya se contruyo el lp
            /* let us name our variables. Not required, but can be useful for debugging */
            lp.setColName(1, "x");
            lp.setColName(2, "y");

           lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */

            /* construct first row (120 x + 210 y <= 15000) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = 120;
            System.out.println(row[0] + " - " + row[1]);

            colno[j] = 2; /* second column */
            row[j++] = 210;//j=1
            System.out.println(row[0] + " - " + row[1]);
            
            //j=2
            /* add the row to lpsolve */
            lp.addConstraintex(j, row, colno, LpSolve.LE, 15000);
          }

          if(ret == 0) {
            /* construct second row (110 x + 30 y <= 4000) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = 110;

            colno[j] = 2; /* second column */
            row[j++] = 30;

            /* add the row to lpsolve */
            lp.addConstraintex(j, row, colno, LpSolve.LE, 4000);
          }

          if(ret == 0) {
            /* construct third row (x + y <= 75) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = 1;

            colno[j] = 2; /* second column */
            row[j++] = 1;

            /* add the row to lpsolve */
            lp.addConstraintex(j, row, colno, LpSolve.LE, 75);
          }

          if(ret == 0) {
            lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */

            /* set the objective function (143 x + 60 y) */
            j = 0;

            colno[j] = 1; /* first column */
            row[j++] = 143;

            colno[j] = 2; /* second column */
            row[j++] = 60;

            /* set the objective in lpsolve */
            lp.setObjFnex(j, row, colno);
          }

          if(ret == 0) {
            /* set the object direction to maximize */
            lp.setMaxim();

            /* just out of curioucity, now generate the model in lp format in file model.lp */
            lp.writeLp("model222.lp");

            /* I only want to see important messages on screen while solving */
            lp.setVerbose(LpSolve.IMPORTANT);

            /* Now let lpsolve calculate a solution */
            ret = lp.solve();
            if(ret == LpSolve.OPTIMAL)
              ret = 0;
            else
              ret = 5;
          }

          if(ret == 0) {
            /* a solution is calculated, now lets get some results */

            /* objective value */
            System.out.println("Objective value: " + lp.getObjective());

            /* variable values */
            lp.getVariables(row);
            for(j = 0; j < Ncol; j++)
              System.out.println(lp.getColName(j + 1) + ": " + row[j]);

            /* we are done now */
          }

          /* clean up such that all used memory by lpsolve is freed */
          if(lp.getLp() != 0)
            lp.deleteLp();

          return(ret);
        }

	public static void main(String[] args) {
		try {
			new Demo().execute();
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}
}