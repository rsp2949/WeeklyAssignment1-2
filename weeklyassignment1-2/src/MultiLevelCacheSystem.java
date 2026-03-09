import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class MultiLevelCacheSystem {

    private int L1_CAPACITY = 10000;
    private int L2_CAPACITY = 100000;

    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;

    private LinkedHashMap<String, VideoData> L1Cache =
            new LinkedHashMap<String, VideoData>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > L1_CAPACITY;
                }
            };

    private LinkedHashMap<String, VideoData> L2Cache =
            new LinkedHashMap<String, VideoData>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > L2_CAPACITY;
                }
            };

    private HashMap<String, Integer> accessCount = new HashMap<>();

    private VideoData fetchFromDatabase(String videoId) {
        return new VideoData(videoId, "VideoContent_" + videoId);
    }

    public VideoData getVideo(String videoId) {

        if (L1Cache.containsKey(videoId)) {
            l1Hits++;
            return L1Cache.get(videoId);
        }

        if (L2Cache.containsKey(videoId)) {
            l2Hits++;
            VideoData data = L2Cache.get(videoId);
            L1Cache.put(videoId, data);
            return data;
        }

        l3Hits++;

        VideoData data = fetchFromDatabase(videoId);

        L2Cache.put(videoId, data);

        accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);

        return data;
    }

    public void invalidate(String videoId) {
        L1Cache.remove(videoId);
        L2Cache.remove(videoId);
        accessCount.remove(videoId);
    }

    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0 / total);

        System.out.println("L1 Hit Rate: " + String.format("%.2f", l1Rate) + "%");
        System.out.println("L2 Hit Rate: " + String.format("%.2f", l2Rate) + "%");
        System.out.println("L3 Hit Rate: " + String.format("%.2f", l3Rate) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");
        cache.getVideo("video_123");

        cache.getStatistics();
    }
}