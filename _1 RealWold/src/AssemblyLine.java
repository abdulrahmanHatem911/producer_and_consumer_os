import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class AssemblyLine {
    private BlockingQueue<CarPart> carParts;
    private Semaphore mutex;
    public Semaphore fillCount;
    public Semaphore emptyCount;

    public AssemblyLine(int size) {
        // Initialize the blocking queue of car parts with a fixed size
        carParts = new LinkedBlockingQueue<CarPart>(size);
        mutex = new Semaphore(1);
        fillCount = new Semaphore(0);
        emptyCount = new Semaphore(size);
    }

    // Add a car part to the assembly line
    public void addCarPart(CarPart carPart) throws InterruptedException {
        try {
            emptyCount.acquire();
            mutex.acquire();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        carParts.put(carPart);
        mutex.release();
        fillCount.release();
    }

    // Remove a car part from the assembly line
    public CarPart removeCarPart() throws InterruptedException {

        try {
            fillCount.acquire();
            mutex.acquire();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        CarPart carPart = carParts.poll(1, TimeUnit.SECONDS);
        if (carPart != null) {
            mutex.release();
            emptyCount.release();

        }
        return carPart;
    }

    public int size() {
        return carParts.size();
    }

    public Queue<CarPart> getCarParts() {
        return carParts;
    }
}