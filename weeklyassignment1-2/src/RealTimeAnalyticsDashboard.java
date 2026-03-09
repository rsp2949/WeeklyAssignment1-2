import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalyticsDashboard {

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event event) {

        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        trafficSources.put(event.source, trafficSources.getOrDefault(event.source, 0) + 1);
    }

    public void getDashboard() {

        List<Map.Entry<String, Integer>> pages = new ArrayList<>(pageViews.entrySet());

        pages.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Top Pages:");

        int count = 0;

        for (Map.Entry<String, Integer> entry : pages) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)");

            count++;
            if (count == 10) break;
        }

        int total = 0;

        for (int v : trafficSources.values()) {
            total += v;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.println(entry.getKey() + ": " + String.format("%.2f", percent) + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalyticsDashboard dashboard = new RealTimeAnalyticsDashboard();

        dashboard.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        dashboard.processEvent(new Event("/sports/championship", "user_777", "direct"));
        dashboard.processEvent(new Event("/sports/championship", "user_888", "google"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_999", "google"));

        dashboard.getDashboard();
    }
}