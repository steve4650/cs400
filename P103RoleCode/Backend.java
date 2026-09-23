import java.io.IOException;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {

    protected BinarySearchTree<GameRecord> tree;

    public Backend() {
        this.tree = new BinarySearchTree<GameRecord>();
    }

    @Override
    public void addRecord(GameRecord record) {
        tree.add(record);
    }

    private static GameRecord.Continent continentToString(String continent) throws IOException {
        switch(continent) {
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

    private GameRecord gameRecordBuilder(String[] headers, String[] cols) {
  String name;
  GameRecord.Continent location;
  int score;
  int collectables;
  int level;
  String completionTime;
  for(int i = 0; i < headers.length; i++) {
    // name,continent,score,max_health,damage_taken,damage_given,collectables,level,completion_time
    if(headers[i] =="name") {
        name = headers[i];
    } else if(headers[i] == "continent") {
        location = continentToString(headers[i]);
    } else if(headers[i] == "score") {
        score = Integer.parseInt(headers[i]);
    } else if(headers[i] == "collectables") {
        collectables = Integer.parseInt(headers[i]);
    } else if(headers[i] == "level") {
        completionTime = headers[i];
    } 
    else if(headers[i] == "completion_time") {
        completionTime = headers[i];
    }
    return new GameRecord(name, location, score, collectables, level, completionTime);
}

    @Override
    public void readData(String filename) throws IOException {
         FileInputStream file = new FileInputStream(filename);
         Scanner scan = new Scanner(file);
         if(!scan.hasNextLine()) {
            return;
         }
         String[] headers = scan.nextLine().split(",");
         while(scan.hasNextLine()) {
         String[] cols = scan.nextLine().split(",");
         GameRecord record = gameRecordBuilder(headers, cols);
         tree.add(record);
         }
    }

    @Override
    public List<String> getAndSetRange(Integer low, Integer high) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAndSetRange'");
    }

    @Override
    public List<String> applyAndSetFilter(String time) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyAndSetFilter'");
    }

    @Override
    public List<String> getTopTen() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopTen'");
    } 

}
}