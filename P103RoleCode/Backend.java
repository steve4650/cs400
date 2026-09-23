import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {

  IterableSortedCollection<GameRecord> tree;
  Integer low = null;
  Integer high = null;
  String filterTime = null;

  public Backend(IterableSortedCollection<GameRecord> tree) {
    this.tree = tree;
  }

  @Override
  public void addRecord(GameRecord record) {
    this.tree.add(record);
  }

  @Override
  public void readData(String filename) throws IOException {
    File file = new File(filename);
    try (Scanner scan = new Scanner(file)) {
      if (!scan.hasNextLine()) {
        return;
      }
      String[] headers = scan.nextLine().split(",");
      int nameIdx = -1;
      int continentIdx = -1;
      int scoreIdx = -1;
      int collectablesIdx = -1;
      int levelIdx = -1;
      int timeIdx = -1;

      for (int i = 0; i < headers.length; i++) {
        String h = headers[i].trim();
        if (h.equals("name")) nameIdx = i;
        else if (h.equals("continent")) continentIdx = i;
        else if (h.equals("score")) scoreIdx = i;
        else if (h.equals("collectables")) collectablesIdx = i;
        else if (h.equals("level")) levelIdx = i;
        else if (h.equals("completion_time")) timeIdx = i;
      }

      while (scan.hasNextLine()) {
        String line = scan.nextLine().trim();
        if (line.isEmpty()) continue;
        String[] cols = line.split(",");

        String name = cols[nameIdx].trim();
        GameRecord.Continent location = GameRecord.Continent.valueOf(cols[continentIdx].trim());
        int score = Integer.parseInt(cols[scoreIdx].trim());
        int collectables = Integer.parseInt(cols[collectablesIdx].trim());
        int level = Integer.parseInt(cols[levelIdx].trim());
        String completionTime = cols[timeIdx].trim();

        GameRecord record =
            new GameRecord(name, location, score, collectables, level, completionTime);
        addRecord(record);
      }
    }
  }

  @Override
  public List<String> getAndSetRange(Integer low, Integer high) {
    this.low = low;
    this.high = high;
    return getFilteredRecordNames();
  }

  @Override
  public List<String> applyAndSetFilter(String time) {
    this.filterTime = time;
    return getFilteredRecordNames();
  }

  @Override
  public List<String> getTopTen() {
    List<GameRecord> records = getFilteredGameRecords();
    records.sort((r1, r2) -> Integer.compare(r2.getCollectables(), r1.getCollectables()));
    List<String> topTenNames = new ArrayList<>();
    int limit = Math.min(10, records.size());
    for (int i = 0; i < limit; i++) {
      topTenNames.add(records.get(i).getName());
    }
    return topTenNames;
  }

  long timeToSeconds(String timeStr) {
    if (timeStr == null || timeStr.trim().isEmpty()) return -1;
    String[] parts = timeStr.trim().split(":");
    if (parts.length != 3) return -1;
    try {
      long h = Long.parseLong(parts[0]);
      long m = Long.parseLong(parts[1]);
      long s = Long.parseLong(parts[2]);
      return h * 3600 + m * 60 + s;
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  List<GameRecord> getFilteredGameRecords() {
    if (this.low != null) {
      this.tree.setIteratorMin(new GameRecord("", null, 0, 0, this.low, ""));
    } else {
      this.tree.setIteratorMin(null);
    }

    if (this.high != null) {
      this.tree.setIteratorMax(new GameRecord("", null, 0, 0, this.high, ""));
    } else {
      this.tree.setIteratorMax(null);
    }

    List<GameRecord> records = new ArrayList<>();
    long filterSec = timeToSeconds(this.filterTime);

    for (GameRecord record : this.tree) {
      if (record == null) continue;
      if (this.low != null && record.getLevel() < this.low) continue;
      if (this.high != null && record.getLevel() > this.high) continue;
      if (filterSec >= 0) {
        long recSec = timeToSeconds(record.getCompletionTime());
        if (recSec < 0 || recSec >= filterSec) continue;
      }
      records.add(record);
    }
    return records;
  }

  List<String> getFilteredRecordNames() {
    List<GameRecord> records = getFilteredGameRecords();
    List<String> names = new ArrayList<>();
    for (GameRecord record : records) {
      names.add(record.getName());
    }
    return names;
  }
}
