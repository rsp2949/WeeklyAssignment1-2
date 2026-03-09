import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {

        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            if (!node.queries.contains(query)) {
                node.queries.add(query);
            }
        }
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>(node.queries);

        results.sort((a, b) -> frequency.get(b) - frequency.get(a));

        if (results.size() > 10) {
            return results.subList(0, 10);
        }

        return results;
    }

    public void updateFrequency(String query) {
        addQuery(query);
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java features");
        system.addQuery("java tutorial");

        List<String> suggestions = system.search("jav");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s);
            rank++;
        }

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("Updated frequency for: java 21 features");
    }
}