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

  public Backend() {
    this.tree = new Tree_Placeholder();
  }

  @Override
  public void addRecord(GameRecord record) {
    this.tree.add(record);
  }

  @Override
  public void readData(String filename) throws IOException {
    File file = new File(filename);
    Scanner scan = new Scanner(file);

    if (!scan.hasNextLine()) {
      scan.close();
      return;
    }

    String[] headers = scan.nextLine().split(",");
    int nameIndex = -1;
    int continentIndex = -1;
    int scoreIndex = -1;
    int collectablesIndex = -1;
    int levelIndex = -1;
    int timeIndex = -1;

    for (int i = 0; i < headers.length; i++) {
      String col = headers[i].trim();
      if (col.equals("name")) nameIndex = i;
      else if (col.equals("continent")) continentIndex = i;
      else if (col.equals("score")) scoreIndex = i;
      else if (col.equals("collectables")) collectablesIndex = i;
      else if (col.equals("level")) levelIndex = i;
      else if (col.equals("completion_time")) timeIndex = i;
    }

    while (scan.hasNextLine()) {
      String line = scan.nextLine().trim();
      if (line.isEmpty()) continue;
      String[] cols = line.split(",");

      // build the GameRecord
      String name = cols[nameIndex].trim();
      GameRecord.Continent location = GameRecord.Continent.valueOf(cols[continentIndex].trim());
      int score = Integer.parseInt(cols[scoreIndex].trim());
      int collectables = Integer.parseInt(cols[collectablesIndex].trim());
      int level = Integer.parseInt(cols[levelIndex].trim());
      String completionTime = cols[timeIndex].trim();

      addRecord(new GameRecord(name, location, score, collectables, level, completionTime));
    }

    scan.close();
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
    List<String> result = new ArrayList<>();
    for (int i = 0; i < Math.min(10, records.size()); i++) {
      result.add(records.get(i).getName());
    }
    return result;
  }

  /*
   * Return game records from the backend's tree, filtered by level (this.low and this.high)
   * and completion time (this.filterTime). If any of these filters are null, they are ignored.
   *
   * @return List of GameRecord objects
   */
  protected List<GameRecord> getFilteredGameRecords() {
    if (this.low != null) {
      this.tree.setIteratorMin(
          new GameRecord("", GameRecord.Continent.ANTARCTICA, 0, 0, this.low, ""));
    } else {
      this.tree.setIteratorMin(null);
    }

    if (this.high != null) {
      this.tree.setIteratorMax(
          new GameRecord("", GameRecord.Continent.ANTARCTICA, 0, 0, this.high, ""));
    } else {
      this.tree.setIteratorMax(null);
    }

    List<GameRecord> records = new ArrayList<>();

    for (GameRecord record : this.tree) {
      if (record == null) continue;
      if (this.low != null && record.getLevel() < this.low) continue;
      if (this.high != null && record.getLevel() > this.high) continue;
      if (this.filterTime != null && record.getCompletionTime().compareTo(this.filterTime) > 0) continue;
      records.add(record);
    }
    return records;
  }

  /*
   * Return game records names from the backend's tree. The records filtered by level (this.low and this.high)
   * and completion time (this.filterTime). If any of these filters are null, they are ignored.
   *
   * @return List of GameRecord names (Strings)
   */
  protected List<String> getFilteredRecordNames() {
    List<GameRecord> records = getFilteredGameRecords();
    List<String> names = new ArrayList<>();
    for (GameRecord record : records) {
      names.add(record.getName());
    }
    return names;
  }
}
