import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {

  protected BinarySearchTree<GameRecord> tree;

  protected Integer low = null;
  protected Integer high = null;
  protected String filter = "";

  public Backend() {
    this.tree = new BinarySearchTree<GameRecord>();
  }

  @Override
  public void addRecord(GameRecord record) {
    tree.add(record);
  }

  private static GameRecord.Continent continentToString(String continent) throws IOException {
    switch (continent) {
      case "AFRICA":
        return GameRecord.Continent.AFRICA;
      case "ASIA":
        return GameRecord.Continent.ASIA;
      case "ANTARCTICA":
        return GameRecord.Continent.ANTARCTICA;
      case "AUSTRALIA":
        return GameRecord.Continent.AUSTRALIA;
      case "EUROPE":
        return GameRecord.Continent.EUROPE;
      case "NORTH_AMERICA":
        return GameRecord.Continent.NORTH_AMERICA;
      case "SOUTH_AMERICA":
        return GameRecord.Continent.SOUTH_AMERICA;
      default:
        throw new IOException("Illegal continent: " + continent);
    }
  }

  private GameRecord gameRecordBuilder(String[] headers, String[] cols) throws IOException {
    String name = null;
    GameRecord.Continent location = null;
    int score = 0;
    int collectables = 0;
    int level = 0;
    String completionTime = null;
    for (int i = 0; i < headers.length; i++) {
      if (headers[i] == "name") {
        name = headers[i];
      } else if (headers[i] == "continent") {
        location = continentToString(headers[i]);
      } else if (headers[i] == "score") {
        score = Integer.parseInt(headers[i]);
      } else if (headers[i] == "collectables") {
        collectables = Integer.parseInt(headers[i]);
      } else if (headers[i] == "level") {
        completionTime = headers[i];
      } else if (headers[i] == "completion_time") {
        completionTime = headers[i];
      }
    }
    return new GameRecord(name, location, score, collectables, level, completionTime);
  }

  @Override
  public void readData(String filename) throws IOException {
    FileInputStream file = new FileInputStream(filename);
    Scanner scan = new Scanner(file);
    if (!scan.hasNextLine()) {
      scan.close();
      return;
    }
    String[] headers = scan.nextLine().split(",");
    while (scan.hasNextLine()) {
      String[] cols = scan.nextLine().split(",");
      GameRecord record = gameRecordBuilder(headers, cols);
      tree.add(record);
    }
    scan.close();
  }

  @Override
  public List<String> getAndSetRange(Integer low, Integer high) {
    this.low = low;
    this.high = high;
    return getAndSetRangeHelper(tree.root, new ArrayList<String>());
  }

  private List<String> getAndSetRangeHelper(BinaryNode<GameRecord> node, List<String> names) {
    if (node == null) {
      return new ArrayList<String>();
    }
    if ((this.low == null || node.getEntry().getLevel() >= this.low)
        && (this.high == null || node.getEntry().getLevel() <= this.high)) {
      List<String> left = getAndSetRangeHelper(node.downLeft(), names);
      if (this.filter.isBlank() || node.getEntry().getCompletionTime().compareTo(this.filter) < 0) {
        left.add(node.getEntry().getName());
      }
      left.add(node.getEntry().getName());
      left.addAll(getAndSetRangeHelper(node.downRight(), names));
    }
    return names;
  }

  @Override
  public List<String> applyAndSetFilter(String time) {
    if (time == null) {
      this.filter = "";
    } else {
      this.filter = time;
    }
    return getAndSetRange(null, null);
  }

  @Override
  public List<String> getTopTen() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getTopTen'");
  }
}
