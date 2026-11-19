import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
/**
 * This is the backend interface that will integrate with a frontend developer's code 
 * that can make use of this functionality. It makes use of
 * a GraphADT to perform shortest path computations.
 */
public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;

    /*
    * Implementing classes should support the constructor below.
    * @param graph object to store the backend's graph data
    */
    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
    }

    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @Override
    public void loadDotFile(String filename) throws IOException {
		//clear graph first
		for (String node : new ArrayList<>(graph.getAllNodes())){
			graph.removeNode(node);
		}

		//create pattern to match against dot file
		Pattern pattern = Pattern.compile("\"(.+?)\"\\s*->\\s*\"(.+?)\"\\s*\\[seconds=([0-9.]+)\\]");

		//read file
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
			String line;
			while((line = reader.readLine())!=null){
				//match each line of the file against pattern
				Matcher match = pattern.matcher(line);
				if(match.find()){
					String source = match.group(1);
					String dest = match.group(2);
					double secs = Double.parseDouble(match.group(3));
					//add to graph
					graph.addNode(source);
					graph.addNode(dest);
					graph.addEdge(source,dest,secs);
				}
			}
		}
		
	}

    /**
     * Returns a list of all locations in the graph.
     * @return list of all location names
     */
    @Override
    public List<String> getAllLocations() {
		//use getAllNodes from GraphADT
        return new ArrayList<>(graph.getAllNodes());
    }

    /**
     * Return the sequence of locations along the shortest path from start to
     * end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the nodes along the shortest path from start to end,
     *         or an empty list if no such path exists
     */
    @Override
    public List<String> findShortestPathLocations(String start, String end) {
        //use shortestPathNodes from GraphADT and catch exception
		try{
			return graph.shortestPathNodes(start,end);
		}
		catch (NoSuchElementException error){
			return new ArrayList<>();
		}
    }

    /**
     * Return the times in seconds between each two nodes on the shortest path
     * from start to end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the times in seconds between two nodes along the
     * shortest path from start to end, or an empty list if no such path exists
     */
    @Override
    public List<Double> findShortestPathTimes(String start, String end) {
        //get list of nodes on shortest path
		List<String> path = findShortestPathLocations(start,end);
		List<Double> times = new ArrayList<>();
		
		//get edge weight between each set of nodes
		for (int i=0; i<path.size()-1; i++){
			times.add(graph.getEdge(path.get(i),path.get(i+1)));
		}
		return times;
    }

    /**
     * Returns the list of locations that can be reached when starting from the
     * provided start, and travelling maxTime in seconds.
     * @param start the location to find the reachable locations from
     * @param maxTime is the maximum time it can take to get from from the
     * start to report a location
     * @return the list of locations that can be reached from start in maxTime
     * or fewer seconds
     * @throws NoSuchElementException if start does not exist
     */
    @Override
    public List<String> getReachableWithin(String start, double maxTime) throws NoSuchElementException {
        //catch incorrect start entered
		if (!graph.containsNode(start)){
			throw new NoSuchElementException("Start location not found: "+start);
		}

		List<String> reachable = new ArrayList<>();
		
		//loop over all nodes
		for(String location : graph.getAllNodes()){
			if (location.equals(start)){
				continue;
			}
			try{
				//check if path is shorter than maxTime
				if(graph.shortestPathCost(start,location) <= maxTime) {
					reachable.add(location);
				}
			}
			catch (NoSuchElementException error){}
		}
		return reachable;
    }
}
