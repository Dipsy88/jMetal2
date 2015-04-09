//  ZDT3.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.problems;

import java.util.ArrayList;

import main.ReadTestCasesArtificialData;
import model.Consequence;
import model.Priority;
import model.Probability;
import model.TestCase;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.ArrayReal;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

/** 
 * Class representing problem ZDT3
 */
public class Kongsberg extends Problem {
    private int timeBudget = 10;
	
	public TestCase caseList[] = new TestCase[100];
	private ReadTestCasesArtificialData readTestCasesArtificialData = new ReadTestCasesArtificialData();
	
	// Get data from database
	public void getTestCases(){
		
		readTestCasesArtificialData.readFile();
		caseList = readTestCasesArtificialData.testCase;		
	}
	
 /**
  * Constructor.
  * Creates default instance of problem ZDT3 (30 decision variables.
  * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
  */    
  public Kongsberg(String solutionType) throws ClassNotFoundException {
    this(solutionType, 100); // 30 variables by default
  } // ZDT3
  
  
 /** 
  * Constructor.
  * Creates a instance of ZDT3 problem.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real", "BinaryReal, and "ArrayReal". 
  */    
  public Kongsberg(String solutionType, Integer numberOfVariables) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  5;
    numberOfConstraints_=  0;
    problemName_        = "Kongsberg";
    getTestCases();
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0;
      upperLimit_[var] = 1;
    } // for
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else if (solutionType.compareTo("ArrayReal") == 0)
    	solutionType_ = new ArrayRealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }
  } //ZDT3
      
  
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */    
  public void evaluate(Solution solution) throws JMException {
	  XReal vars = new XReal (solution);
	  double [] x = new double[numberOfVariables_];
	  double [] f = new double[numberOfObjectives_] ;
	  
	  ArrayList<TestCase> tempCase2 = new ArrayList<TestCase>();
	  ArrayList<TestCase> tempCaseList = new ArrayList<TestCase>();

	  for (int i=0;i<numberOfVariables_;i++){
		 x[i] = vars.getValue(i);
			//double a = variables[0].getValue();
			if (x[i] > 0.5)
				tempCase2.add(caseList[i]);

		}

       
//    f[0]        = x.getValue(0)     ;
//    double g    = this.evalG(x)                 ;
//    double h    = this.evalH(f[0],g)                 ;
//    f[1]        = h * g                           ;
//    solution.getDecisionVariables();

	  double temptime =0,priorityValue=0, probabilityValue=0, consequenceValue =0, overallProbability=0, overallPriority=0, overallConsequence=0, 
    		prioritySum =0, probabilitySum =0, consequenceSum =0, count =0, time =0, ePriorityValue=0, eProbabilityValue=0, eConsequenceValue =0,
    				ePrioritySum = 0, eProbabilitySum = 0, eConsequenceSum = 0, overallEProbability=0, overallEPriority=0, overallEConsequence=0;
    
	  for (int j=0;j<tempCase2.size();j++){
		  double timeTemp=0;
		  Priority priorityTemp = new Priority();
		  Probability probabilityTemp = new Probability();	
		  Consequence consequenceTemp = new Consequence();
		  TestCase tempCase = new TestCase();
		  
		  tempCase = tempCase2.get(j);	
		  
		  timeTemp = tempCase.getTimeExecution();
		  temptime += timeTemp;
		  priorityTemp = tempCase.getPriority();
		  probabilityTemp = tempCase.getProbability();
		  consequenceTemp = tempCase.getConsequence();
		
		  if (priorityTemp.getName().equalsIgnoreCase("higher")||priorityTemp.getName().equalsIgnoreCase("high")||priorityTemp.getName().equalsIgnoreCase("medium")
				||priorityTemp.getName().equalsIgnoreCase("low")||priorityTemp.getName().equalsIgnoreCase("lower"))
			  priorityValue= convertPriority(priorityTemp);
		  else 
			  priorityValue=0;
		
		  if (probabilityTemp.getName().equalsIgnoreCase("high") || probabilityTemp.getName().equalsIgnoreCase("medium") || probabilityTemp.getName().equalsIgnoreCase("low"))
			  probabilityValue= convertProbability(probabilityTemp);
		  else
			probabilityValue= 0 ;
		
		  if (consequenceTemp.getName().equalsIgnoreCase("higher")||consequenceTemp.getName().equalsIgnoreCase("high")||consequenceTemp.getName().equalsIgnoreCase("medium")
				||consequenceTemp.getName().equalsIgnoreCase("low")||consequenceTemp.getName().equalsIgnoreCase("lower"))
			  consequenceValue = convertConsequence(consequenceTemp);			
		  else 
			  consequenceValue =0;
		
		count++;
		tempCaseList.add(tempCase);
		prioritySum+=  priorityValue;
		probabilitySum += probabilityValue;
		consequenceSum += consequenceValue;
		
		ePriorityValue = Nor(priorityValue/timeTemp);
		eProbabilityValue = Nor(probabilityValue/timeTemp);
		eConsequenceValue = Nor(consequenceValue/timeTemp);
		
		ePrioritySum += ePriorityValue;
		eProbabilitySum += eProbabilityValue;
		eConsequenceSum += eConsequenceValue;
	
    }
    overallPriority = prioritySum/count;
	overallProbability = probabilitySum/count;
	overallConsequence = consequenceSum/count;
	
	overallEPriority = ePrioritySum/count;
	overallEProbability = eProbabilitySum/count;
	overallEConsequence = eConsequenceSum/count;
	
	time = 1-Nor(Math.abs(temptime-timeBudget));
    
	 f[0]        = 1- time    ;
	 f[1]        = 1- overallPriority     ;
	 f[2]        = 1- overallProbability    ;
	 f[3]        = 1- overallConsequence     ;
	// f[4]        = 1- (time*0.2 + overallPriority*0.8 + overallProbability*0.0 + overallProbability*0.0);
	 f[4]        = 1- (time*0.2 + overallEPriority*0.8 + overallEProbability*0.0 + overallEConsequence*0.0);
	
	 for (int i = 0; i < numberOfObjectives_; i++)
	      solution.setObjective(i,f[i]);    
	 int j =0;
	 if (f[0] == 0)

		 j =1;
	 j++;
	 
  } //evaluate
  
  public  ArrayList<TestCase> getTestCase(Solution solution) throws JMException{
	  ArrayList<TestCase> tempCase2 = new ArrayList<TestCase>();
	  Variable[] variables=(solution.getDecisionVariables());
	  ArrayReal arrayReal = new ArrayReal();
	  arrayReal = (ArrayReal) variables[0];

	  XReal vars = new XReal (solution);
	  
	  for (int i=0;i<numberOfVariables_;i++){

			//double a = variables[0].getValue();
			if (arrayReal.array_[i].intValue()  > 0.5)
				tempCase2.add(caseList[i]);
			
			
//			if (tempCase2.size()!=testCaseList.size()){
//				if (v[i]==1){
//					tempset.add(i);
//					tempCase2.add(testCaseList.get(k));
//					|
//				}
//				k++;
//			}		
		}
	  return tempCase2;
	  
  }
//normalization function
	public double Nor(double n) {
		double m = n / (n + 1);
		return m;
	}
  
  public double convertPriority(Priority priority){
		double priorityNum=0;
		if (priority.getName().equalsIgnoreCase("higher"))
			priorityNum=1;
		else if (priority.getName().equalsIgnoreCase("high"))
			priorityNum=0.8;
		else if (priority.getName().equalsIgnoreCase("medium"))
			priorityNum=0.6;
		else if (priority.getName().equalsIgnoreCase("low"))
			priorityNum=0.4;
		else if (priority.getName().equalsIgnoreCase("lower"))
			priorityNum=0.2;
		
		return priorityNum;
	}
	
	public double convertProbability(Probability probability){
		double priorityNum=0;
		if (probability.getName().equalsIgnoreCase("high"))
			priorityNum=1;
		else if (probability.getName().equalsIgnoreCase("medium"))
			priorityNum=0.66;
		else if (probability.getName().equalsIgnoreCase("low"))
			priorityNum=0.33;
				
		return priorityNum;
	}
	
	public double convertConsequence(Consequence consequence){
		double consequenceNum=0;
		if (consequence.getName().equalsIgnoreCase("higher"))
			consequenceNum=1;
		else if (consequence.getName().equalsIgnoreCase("high"))
			consequenceNum=0.8;
		else if (consequence.getName().equalsIgnoreCase("medium"))
			consequenceNum=0.6;
		else if (consequence.getName().equalsIgnoreCase("low"))
			consequenceNum=0.4;
		else if (consequence.getName().equalsIgnoreCase("lower"))
			consequenceNum=0.2;
		
		return consequenceNum;
	}

	
	/** 
	   * Evaluates the constraint overhead of a solution 
	   * @param solution The solution
	   * @throws JMException 
	   */  
	  public void evaluateConstraints(Solution solution) throws JMException {
		
		
	}

   
  /**
  * Returns the value of the ZDT3 function H.
  * @param f First argument of the function H.
  * @param g Second argument of the function H.
  */
  public double evalH(double f, double g) {
    double h = 0.0;
    h = 1.0 - java.lang.Math.sqrt(f/g) 
        - (f/g)*java.lang.Math.sin(10.0*java.lang.Math.PI*f);
    return h;        
  } //evalH
} // ZDT3
