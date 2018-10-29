

/*

AUTHOR:      John Lu

DESCRIPTION: This file contains your agent class, which you will
             implement.

NOTES:       - If you are having trouble understanding how the shell
               works, look at the other parts of the code, as well as
               the documentation.

             - You are only allowed to make changes to this portion of
               the code. Any changes to other portions of the code will
               be lost when the tournament runs your code.
*/

package src;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import src.World;

//import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import src.Action.ACTION;

public class MyAI extends AI {
	int x,y;
	Point point;
	Queue<Point> safe;
	boolean firstVisit;
	boolean visited[][];
	boolean mineAlreadyInQueue[][];
	int numRows,numCols;
	HashMap<Point,Integer> mines;
	int noOfSteps;
	World world;
	int flag_x;
	int flag_y;
	boolean flagSet;
	// ########################## INSTRUCTIONS ##########################
	// 1) The Minesweeper Shell will pass in the board size, number of mines
	// 	  and first move coordinates to your agent. Create any instance variables
	//    necessary to store these variables.
	//
	// 2) You MUST implement the getAction() method which has a single parameter,
	// 	  number. If your most recent move is an Action.UNCOVER action, this value will
	//	  be the number of the tile just uncovered. If your most recent move is
	//    not Action.UNCOVER, then the value will be -1.
	// 
	// 3) Feel free to implement any helper functions.
	//
	// ###################### END OF INSTURCTIONS #######################
	
	// This line is to remove compiler warnings related to using Java generics
	// if you decide to do so in your implementation.
	@SuppressWarnings("unchecked")


	public MyAI(int rowDimension, int colDimension, int totalMines, int startX, int startY) {
		this.safe=new LinkedList();
		System.out.println(startX+"  " + startY);
		this.x=startX-1;
		this.y=startY-1;
		this.point=new Point(x,y);
		this.firstVisit=true;
		this.numRows=rowDimension;
		this.numCols=colDimension;
		visited=new boolean[numRows][numCols];
		noOfSteps=0;
		this.flagSet=false;
		//world=new World();
		for(int i=0;i<numRows;i++)
		{
			Arrays.fill(visited[i], false);
		}
		mineAlreadyInQueue=new boolean[numRows][numCols];
		for(int i=0;i<numRows;i++)
		{
			Arrays.fill(mineAlreadyInQueue[i], false);
		}
		mines=new HashMap();
		
		// ################### Implement Constructor (required) ####################	
	}
	
	// ################## Implement getAction(), (required) #####################
	public Action getAction(int number) {
		if(firstVisit)
		{
			this.firstVisit=false;
			visited[(int)point.getX()][(int)point.getY()]=true;
			System.out.println(this.x+"  " + this.y);
			return new Action(ACTION.UNCOVER,this.x+1,this.y+1);
		}
		System.out.println("tile no" + number);
		if(number==0 && !flagSet)
		{
			if(fillSafeQueue(point))
			{
				System.out.println("successfull filled");
			}
		}
		else if(!flagSet)
		{
			System.out.println("saw a mine around");
			fillMines(point);
		}
		point=pointToUncover();
		if(point==null)
		{
			System.out.println("safe queue empty");
			if(flagSet)
			{
				Point toUncover=uncoverRemainingMines();
				printWorld();
				return new Action(ACTION.UNCOVER,(int)toUncover.getX()+1,(int)toUncover.getY()+1);
				
			}
			else
			{
				markTheFlag();
				this.flagSet=true;
				return new Action(ACTION.FLAG,flag_x+1,flag_y+1);
			}
			
		}
		visited[(int)point.getX()][(int)point.getY()]=true;
		noOfSteps++;
		printWorld();
		System.out.println(safe);
		System.out.println(mines);
		

		
		
		
	
	
		
		

		return new Action(ACTION.UNCOVER,(int)point.getX()+1,(int)point.getY()+1);
	}
	public boolean fillSafeQueue(Point point)
	{
		int centerX=(int)point.getX();
		int centerY=(int)point.getY();
		if(centerX-1>=0)
		{
			if(!visited[centerX-1][centerY] && !mineAlreadyInQueue[centerX-1][centerY])
			{
				safe.add(new Point(centerX-1,centerY));
				mineAlreadyInQueue[centerX-1][centerY]=true;
			}
			if(centerY-1>=0 && !visited[centerX-1][centerY-1] && !mineAlreadyInQueue[centerX-1][centerY-1])
			{
				safe.add(new Point(centerX-1,centerY-1));
				mineAlreadyInQueue[centerX-1][centerY-1]=true;
			}
			if(centerY+1<numCols && !visited[centerX-1][centerY+1] && !mineAlreadyInQueue[centerX-1][centerY+1])
			{
				safe.add(new Point(centerX-1,centerY+1));
				mineAlreadyInQueue[centerX-1][centerY+1]=true;
			}
			
		}
		if(centerY-1>=0 && !visited[centerX][centerY-1] && !mineAlreadyInQueue[centerX][centerY-1])
		{
			safe.add(new Point(centerX,centerY-1));
			mineAlreadyInQueue[centerX][centerY-1]=true;
		}
		if(centerY+1<numCols && !visited[centerX][centerY+1] && !mineAlreadyInQueue[centerX][centerY+1])
		{
			safe.add(new Point(centerX,centerY+1));
			mineAlreadyInQueue[centerX][centerY+1]=true;
		}
		if(centerX+1<numRows)
		{
			if(!visited[centerX+1][centerY] && !mineAlreadyInQueue[centerX+1][centerY])
			{
				safe.add(new Point(centerX+1,centerY));
				mineAlreadyInQueue[centerX+1][centerY]=true;
			}
			if(centerY-1>=0 && !visited[centerX+1][centerY-1] && !mineAlreadyInQueue[centerX+1][centerY-1])
			{
				safe.add(new Point(centerX+1,centerY-1));
				mineAlreadyInQueue[centerX+1][centerY-1]=true;
			}
			if(centerY+1<numCols && !visited[centerX+1][centerY+1] && !mineAlreadyInQueue[centerX+1][centerY+1])
			{
				safe.add(new Point(centerX+1,centerY+1));
				mineAlreadyInQueue[centerX+1][centerY+1]=true;
			}
			
		}
		return true;
		
	}
	public Point pointToUncover()
	{
		return safe.poll();
	}
	public void fillMines(Point point)
	{
		int centerX=(int)point.getX();
		int centerY=(int)point.getY();
		System.out.println("fill mine from centerx and center y" + centerX+ "  " + centerY);
		if(centerX-1>=0)
		{
			if(!mineAlreadyInQueue[centerX-1][centerY]&& !visited[centerX-1][centerY] && !checkIfMineExits(centerX-1,centerY))
				mines.put(new Point(centerX-1,centerY),1);
			if(centerY-1>=0 && !visited[centerX-1][centerY-1]&&  !mineAlreadyInQueue[centerX-1][centerY-1] && !checkIfMineExits(centerX-1,centerY-1))
			{
				//if(!checkIfMineExits(point))
					mines.put(new Point(centerX-1,centerY-1),1);
			}
			if(centerY+1<numCols && !visited[centerX-1][centerY+1]&& !mineAlreadyInQueue[centerX-1][centerY+1] && !checkIfMineExits(centerX-1,centerY+1))
			{
				//if(!checkIfMineExits(point))
					mines.put(new Point(centerX-1,centerY+1),1);
			}
			
		}
		if(centerY-1>=0 && !visited[centerX][centerY-1]&& !mineAlreadyInQueue[centerX][centerY-1])
		{	
			if(!checkIfMineExits(centerX,centerY-1))
				mines.put(new Point(centerX,centerY-1),1);
		}
		if(centerY+1<numCols && !visited[centerX][centerY+1] && !mineAlreadyInQueue[centerX][centerY+1])
		{
			if(!checkIfMineExits(centerX,centerY+1))
				mines.put(new Point(centerX,centerY+1),1);
		}
		if(centerX+1<numRows && !visited[centerX+1][centerY])
		{
			if(!visited[centerX+1][centerY] && !mineAlreadyInQueue[centerX+1][centerY] && !checkIfMineExits(centerX+1,centerY))
				mines.put(new Point(centerX+1,centerY),1);
			
			if(centerY-1>=0 && !visited[centerX+1][centerY-1] && !mineAlreadyInQueue[centerX+1][centerY-1])
			{
				if(!checkIfMineExits(centerX+1,centerY-1))
					mines.put(new Point(centerX+1,centerY-1),1);
			}
			if(centerY+1<numCols && !visited[centerX+1][centerY+1] && !mineAlreadyInQueue[centerX+1][centerY+1])
			{
				if(!checkIfMineExits(centerX+1,centerY+1))
					mines.put(new Point(centerX+1,centerY+1),1);
			}
			
		}
		
	}
	public boolean checkIfMineExits(int x1,int y1)
	{
		//System.out.println("already exists");
		for (java.util.Map.Entry<Point, Integer> entry : mines.entrySet())
		{
			if((int)entry.getKey().getX()==x1 && (int)entry.getKey().getY()==y1)
			{
				System.out.println("already exists");
				mines.put(entry.getKey(), entry.getValue()+1);
				
				return true;
			}
		}
		return false;
	
		
	}
	public void printWorld()
	{
		
		for(int i=0;i<numRows;i++)
		{
			for(int j=0;j<numCols;j++)
			{
				System.out.print(visited[i][j]+"  ");
			}
			System.out.println();
		}
		return;
	}
	public void markTheFlag()
	{
		int max=Integer.MIN_VALUE;
		Point toRemove=null;
		for (java.util.Map.Entry<Point, Integer> entry : mines.entrySet())
		{
			if(entry.getValue()>max)
			{
				max=entry.getValue();
				flag_x=(int)entry.getKey().getX();
				flag_y=(int)entry.getKey().getY();
				toRemove=entry.getKey();
				
			}
		}
		mines.remove(toRemove);
		
		return;
	}
	public Point uncoverRemainingMines()
	{
		Point temp;
		for (java.util.Map.Entry<Point, Integer> entry : mines.entrySet())
		{
			temp=entry.getKey();
			mines.remove(entry.getKey());
			return temp;
		}
		return null;
		
		
	}

	// ################### Helper Functions Go Here (optional) ##################
	// ...
}
