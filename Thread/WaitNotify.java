// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.concurrent.*;
class Data{
    private String packet;
    private boolean transfer=true;
    
    public synchronized String receive(){
        while(transfer){
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Thread Interruption");
            }
        }
        transfer=true;
        String returnPacket=packet;
        notifyAll();
       return returnPacket;
    }
    public synchronized void send(String packet){
        while(!transfer){
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.err.println("Thread Interruption");
            }
        }
        transfer=false;
        this.packet=packet;
        notifyAll();
       
    }
}
class Sender implements Runnable {
    private Data data;
 
    public Sender(Data data){
        this.data=data;
    }
 
    public void run() {
        String packets[] = {
          "First packet",
          "Second packet",
          "Third packet",
          "Fourth packet",
          "End"
        };
 
        for (String packet : packets) {
            data.send(packet);

            // Thread.sleep() to mimic heavy server-side processing
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.err.println("Thread Interrupted"); 
            }
        }
    }
}
 class Receiver implements Runnable {
    private Data load;
 
    public Receiver(Data load){
        this.load=load;
    }
 
    public void run() {
        for(String receivedMessage = load.receive();
          !"End".equals(receivedMessage);
          receivedMessage = load.receive()) {
            
            System.out.println(receivedMessage);

            //Thread.sleep() to mimic heavy server-side processing
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.err.println("Thread Interrupted"); 
            }
        }
    }
}
public class WaitNotify {
    public static void main(String[] args) {
        Data data = new Data();
    Thread sender = new Thread(new Sender(data));
    Thread receiver = new Thread(new Receiver(data));
    
    sender.start();
    receiver.start();

    }
}
