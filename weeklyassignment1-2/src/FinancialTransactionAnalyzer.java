import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class FinancialTransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);
                System.out.println("(" + other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    public void findTwoSumWithTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {
                    System.out.println("(" + other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {

            List<Transaction> list = entry.getValue();

            if (list.size() > 1) {

                System.out.print("{amount:" + list.get(0).amount + ", merchant:" + list.get(0).merchant + ", accounts:[");

                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }

                System.out.println("]}");
            }
        }
    }

    public void findKSum(int k, int target) {
        kSumHelper(new ArrayList<>(), 0, k, target);
    }

    private void kSumHelper(List<Transaction> current, int index, int k, int target) {

        if (current.size() == k) {

            int sum = 0;

            for (Transaction t : current) {
                sum += t.amount;
            }

            if (sum == target) {

                System.out.print("(");

                for (Transaction t : current) {
                    System.out.print(t.id + " ");
                }

                System.out.println(")");
            }

            return;
        }

        for (int i = index; i < transactions.size(); i++) {

            current.add(transactions.get(i));
            kSumHelper(current, i + 1, k, target);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        FinancialTransactionAnalyzer analyzer = new FinancialTransactionAnalyzer();

        long now = System.currentTimeMillis();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", "acc1", now));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", "acc2", now + 1000));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", "acc3", now + 2000));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", "acc4", now + 3000));

        System.out.println("Two Sum:");
        analyzer.findTwoSum(500);

        System.out.println("Two Sum within 1 hour:");
        analyzer.findTwoSumWithTimeWindow(500, 3600000);

        System.out.println("Duplicate Transactions:");
        analyzer.detectDuplicates();

        System.out.println("K Sum (k=3 target=1000):");
        analyzer.findKSum(3, 1000);
    }
}