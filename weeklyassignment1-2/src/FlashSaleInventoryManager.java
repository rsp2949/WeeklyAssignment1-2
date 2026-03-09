import java.util.*;

public class FlashSaleInventoryManager {

    private HashMap<String, Integer> stock = new HashMap<>();
    private HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    public FlashSaleInventoryManager() {
        stock.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedList<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        } else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        System.out.println(manager.checkStock("IPHONE15_256GB") + " units available");

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}