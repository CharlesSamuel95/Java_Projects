/* 

Assign 4

Author: Charles Samuel
*/ 

import java.util.LinkedList;
import java.util.Queue;

/*Class represents all cars. Each car contains a car number a bound(left of right)
 and how long they will spend in the tunnel(this is dependent on whither they are
 left or right bound). Also contains getter methods to get these values*/
 class Cars {
    private long carNumber = 0;
    private String bounded = "";
    private long tunnelTime = 0;

    public Cars(long number, String bound, long time){
        carNumber = number;
        bounded = bound;
        tunnelTime = time;
    }

    public long getCarNumber(){
        return carNumber;
    }


    public long getTunnelTime(){
        return tunnelTime;
    }

    public String getBound(){
        return bounded;
    }    
}// end of Cars

/*This class will create left bound cars in the while loop. It also calls the
addToWaitingQueue() and enterTunnel() methods during each iteration.

Varibles: sc will store reference to Synchronize_Car object 
            nextLefttCar stores the next/current car number*/
 class Left_Cars implements Runnable{
    Cars car;
    Synchronize_Car sc;
    long nextLeftCar = 0;

    public Left_Cars(Synchronize_Car sc){
        this.sc = sc;
    }

    public void run(){
       while(true){
         car = new Cars(nextLeftCar,"Left-Bound",1000);
         nextLeftCar += 2;

         System.out.println(car.getBound() + " Car " + car.getCarNumber() + 
                            " wants to enter the tunnel");

         sc.addToWatingQueue(car);
         sc.enterTunnel(car.getBound());
       }
    }
}// end of Left_Car



/*This class will create right bound cars in the while loop. It also calls the
addToWaitingQueue() and enterTunnel() methods during each iteration.

Varibles: sc will store reference to Synchronize_Car object 
            nextRightCar stores the next/current car number*/
class Right_Cars implements Runnable{
     Cars car;
     Synchronize_Car sc;
     long nextRightCar = 1;

    public Right_Cars(Synchronize_Car sc){
        this.sc = sc;
    }

     public void run(){
         while(true){
            car = new Cars(nextRightCar,"Right-Bound",1010);
            nextRightCar += 2;
            System.out.println(car.getBound() + " Car " + car.getCarNumber() + 
                                " wants to enter the tunnel");

            sc.addToWatingQueue(car);
            sc.enterTunnel(car.getBound());
        }
    }
}// end of Right_Car

/*Main class. Creates a waiting queue that each car must enter before they 
can enter the tunnel. It creates two threads that receives a reference to 
object sc. These threads handle either left or right cars.*/
public class Synchronize_Car{
    static Queue<Cars> waitingQueue = new LinkedList<Cars>();
    
    public static void main(String[] args){
        Synchronize_Car sc = new Synchronize_Car();
        Thread leftTask = new Thread(new Left_Cars(sc));
        Thread rightTask = new Thread(new Right_Cars(sc));
   
        leftTask.start();
        rightTask.start();
    }//end of main


    /*Threads must call addToWaitingQueue be for they can call this method.
    One car can only enter the tunnel. Current thread needs to check
    if the cars bound that it passes equals bound of the head car. If it does
    then it removes it and notifies any waiting thread. Else it puts itself in
    the waiting state.
    
    Example: Left_Car thread creates a car then calls this method. If the
    head car's bound is left then this thread will remove the head car,
    notify any waiting thread, and have the head car enter the tunnel. Else
    it will put itself in a waiting state and allow the Right_Car thread
    handle this car. */
    public void enterTunnel(String carBound){
        try{
            synchronized(waitingQueue){
                if(carBound.compareTo(waitingQueue.peek().getBound()) == 0){
                    Cars nextCar = waitingQueue.remove();

                    System.out.println(nextCar.getBound() + " Car " + nextCar.getCarNumber() + 
                    " is in the tunnel");

                    waitingQueue.notify();

                    Thread.sleep(nextCar.getTunnelTime());

                    System.out.println(nextCar.getBound() + " Car " + nextCar.getCarNumber() + 
                    " is exiting the tunnel");
                }

                else{
                   waitingQueue.wait();
                }
                
            }// end of synchronized block
        }// end of try

        catch(Exception e){
            e.printStackTrace();
        }
    }//end of method

    /*Adds a new car into the waiting queue.*/
    public synchronized void addToWatingQueue(Cars car){
        waitingQueue.add(car);
    }
    
}// end of Synchronize_Car