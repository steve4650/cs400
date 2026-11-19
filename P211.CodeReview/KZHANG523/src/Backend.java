import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * The backend class that handles the transactions between the frontend and the graph. It uses some
 * core methods safely update the graph and validates inputs.
 */
public class Backend implements BackendInterface {
  // Static variables
  private static final String[] GRAPH_REQ_KEYWORDS = {"graph", "digraph"};
  private static final String GRAPH_STRICT_KEYWORD = "strict";

  GraphADT<String, Double> graph;

  /**
   * Constructor for the backend, takes in a graph object so that the backend has a reference to it
   * and may read/update it as needed.
   *
   * @param graph An object implementing the GraphADT interface using a String as the data type in
   *     each node and a Double for edge weights.
   */
  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  @Override
  public List<String> getAllLocations() {
    return graph.getAllNodes();
  }

  @Override
  public List<String> findShortestPathLocations(String start, String end) {
    return graph.shortestPathNodes(start, end);
  }

  @Override
  public List<Double> findShortestPathTimes(String start, String end) {
    List<String> locationArray = findShortestPathLocations(start, end);
    List<Double> returnArray = new ArrayList<>();

    for (int i = 0; i < (locationArray.size() - 1); i++) {
      String first = locationArray.get(i);
      String second = locationArray.get(i + 1);
      returnArray.add(graph.shortestPathCost(first, second));
    }
    return returnArray;
  }

  @Override
  public List<String> getReachableWithin(String start, double maxTime)
      throws NoSuchElementException {
    List<String> allLocations = getAllLocations();
    List<String> reachableList = new ArrayList<>();
    for (String location : allLocations) {
      if (graph.shortestPathCost(start, location) <= maxTime) {
        reachableList.add(location);
      }
    }
    return reachableList;
  }

  @Override
  public void loadDotFile(String filename) throws IOException {
    ArrayList<NodeEdgeRel> relationshipsToProcess = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      // Find the first opening curly brace due to dot notation
      StringBuilder header = new StringBuilder();
      String line = br.readLine();

      // Now, if line is null, then this file is already empty.
      // Decision here is to not throw an exception but rather to quit, since
      // an empty graph is still a graph.
      if (line == null) {
        return;
      }

      while ((line != null) && (!line.contains("{"))) {
        // For string matching, force everything to be lowercase.
        String sanitizedString = line.strip().toLowerCase();

        if (!sanitizedString.isEmpty()) {
          header.append(sanitizedString);

          // Adding space since strings across multiple lines are valid
          header.append(' ');
        }

        // Advance to next line
        line = br.readLine();
      }

      // If we reached the end without ever encountering an open brace, this
      // doesn't seem to be a correctly-formatted dot file. Throw exception.
      if (line == null) {
        throw new IOException("Failed to parse dot file, no open brace encountered.");
      }

      // Otherwise, we are currently on the line where the open brace is encountered.
      line = line.strip();
      int braceIndex = line.indexOf('{');
      if (braceIndex == -1) {
        // Something went terribly wrong, file changed under our nose?
        throw new IOException(
            "File expected to contain data with open brace, but it wasn't found upon reading.");
      }

      // Build rest of header if there are more characters before open brace.
      header.append(line.substring(0, braceIndex));

      String headerString = header.toString().strip();

      ArrayList<String> headerPartition =
          new ArrayList<>(Arrays.asList(headerString.split("\\s+")));

      if (headerPartition.isEmpty() || (headerPartition.size() > 3)) {
        throw new IOException("File does not define a valid graph with: " + headerString);
      }

      switch (headerPartition.size()) {
        case 1 -> {
          // Must be either "graph" or "digraph"
          String onlyKeyword = headerPartition.getFirst();
          if (!isRequiredGraphKeyword(onlyKeyword)) {
            throw new IOException("DOT file does not have a required graph type identifier");
          }
        }

        case 2 -> {
          // Can either have the required keyword followed by an ID, or the keyword prefixed by
          // "strict"
          if (isRequiredGraphKeyword(headerPartition.get(1))) {
            if (!headerPartition.getFirst().equals(GRAPH_STRICT_KEYWORD)) {
              throw new IOException(
                  headerPartition.getFirst()
                      + " is not a valid keyword/modifier for the DOT file.");
            }
          } else if (isRequiredGraphKeyword(headerPartition.getFirst())) {
            // This is fine
          } else {
            throw new IOException(
                "DOT file does not have valid keywords for graph type and"
                    + " either one of ID or 'strict'.");
          }
        }

        case 3 -> {
          // We must have "strict graph|digraph [ID]"
          if (!isRequiredGraphKeyword(headerPartition.get(1))) {
            throw new IOException("DOT file needs to have either 'digraph' or 'graph'.");
          }

          if (headerPartition.getFirst().equals(GRAPH_STRICT_KEYWORD)) {
            throw new IOException(
                "Only 'strict' can be used as the optional prefix to 'graph' or 'digraph'.");
          }
        }
      }

      // Once we're here, we know we've validated the first portion of the file, we can proceed and
      // read the contents.
      // The line variable still contains the line in the file with the open brace. There may be
      // data directly after it.
      StringBuilder dataBuffer = new StringBuilder();
      if (line.length() > (braceIndex + 1)) {
        dataBuffer.append(line.substring(braceIndex + 1, line.length()));
        dataBuffer.append(' ');
      }

      // Next, keep looping through the rest of the file to get data and process them.
      while ((line != null)) {
        line = br.readLine();
        if (line == null) {
          break;
        } else {
          line = line.strip();
        }
        dataBuffer.append(line);
        dataBuffer.append(' ');
      }

      DotParser parser = new DotParser();
      parser.queueTokensFromString(dataBuffer.toString());
      relationshipsToProcess = parser.processAll();
    } catch (Exception e) {
      throw new IOException("Exception occurred when reading file. Details: " + e.getMessage());
    }

    // After all that error handling, only update graph if there's meaningful data to import
    // and no errors in the previous processes
    try {
      if (!relationshipsToProcess.isEmpty()) {
        // Clear everything out
        for (String node : graph.getAllNodes()) {
          graph.removeNode(node);
        }

        for (NodeEdgeRel rel : relationshipsToProcess) {
          double weight = Double.parseDouble(rel.getAttribute("seconds"));
          graph.addNode(rel.getSourceNode());
          graph.addNode(rel.getTargetNode());
          graph.addEdge(rel.getSourceNode(), rel.getTargetNode(), weight);
        }
      }
    } catch (Exception e) {
      throw new IOException("Exception occurred when updating nodes. Details: " + e.getMessage());
    }
  }

  class DotParser {
    ArrayList<Token> cache;
    Queue<Token> tokenQueue;
    boolean readyToParse = false;

    public DotParser() {
      cache = new ArrayList<>();
      tokenQueue = new ArrayDeque<>();
    }

    /**
     * Given a collection of tokens, queues them up for evaluation. This method alone does not
     * trigger processing.
     *
     * @param collection The iterable collection of tokens to pass in
     */
    void queueTokens(Iterable<Token> collection) {
      for (Token t : collection) {
        tokenQueue.add(t);
      }
    }

    /**
     * Given a string read from a file, attempt to queue up tokens to be analyzed by the dot parser.
     * If there are any errors during tokenization, this method will throw an exception and will not
     * do anything.
     *
     * @param input The string used as input.
     */
    public void queueTokensFromString(String input) {
      queueTokens(tokenizeString(input));
    }

    // If the queue has tokens with proper separators, continually parse into relationships as long
    // as they are valid.
    public ArrayList<NodeEdgeRel> processAll() {
      ArrayList<NodeEdgeRel> listOfRels = new ArrayList<>();
      while (!tokenQueue.isEmpty()) {
        NodeEdgeRel result = process();
        if (result == null) {
          // Just in case
          break;
        }
        listOfRels.add(result);
      }
      return listOfRels;
    }

    /**
     * Does one round of collecting tokens into the cache and trying to interpret it as a edge
     * object.
     *
     * @return A NodeEdgeRel object if successful, otherwise null (if still waiting on more tokens)
     *     or throws exception if parsing failed.
     */
    public NodeEdgeRel process() {
      if (!readyToParse) {
        // Catch up to see if there are any more tokens to read
        // and provide more context
        stageTokens();
      }

      if (!readyToParse) {
        // If still not marked ready, we already cleared the queue.
        // There's nothing to parse yet. Needs more tokens.
        return null;
      }

      // Otherwise, we can attempt a parse.
      NodeEdgeRel result = null;
      try {
        result = parseTokensIntoRel(cache);
      } catch (Exception e) {
        // If any exceptions, this combination of tokens is bad. Clear them
        // and pass exception upwards. Do cleanup here.
        cacheReset();
        throw new IllegalArgumentException("Parsing failed on queue: " + e.getMessage());
      }
      cacheReset();
      return result;
    }

    /** Resets the cache status so the parser knows it's back to a waiting state. */
    void cacheReset() {
      cache.clear();
      readyToParse = false;
    }

    /** Helper to stage tokens for the parser to read */
    void stageTokens() {
      if (cache.isEmpty() && !stageFirstValidToken()) {
        // Then we don't have enough information to create an edge, we
        // haven't even gotten an identifer
        return;
      }

      while (!tokenQueue.isEmpty()) {
        Token poppedToken = tokenQueue.poll();
        cache.add(poppedToken);

        // Keep reading until we eventually pop a separator or the end.
        if (poppedToken.getTokenType() == TokenType.SEPARATOR) {
          readyToParse = true;
          break;
        }

        if (poppedToken.getTokenType() == TokenType.GRAPH_END) {
          readyToParse = true;
          break;
        }
      }
    }

    /**
     * Finds the first ID token and places it in the cache. Wastes tokens as necessary.
     *
     * @return True if successfully loaded first token, false otherwise.
     */
    boolean stageFirstValidToken() {
      if (!cache.isEmpty()) {
        return false;
      }

      while (!tokenQueue.isEmpty()) {
        Token poppedToken = tokenQueue.poll();
        if (poppedToken.getTokenType() == TokenType.ID) {
          cache.add(poppedToken);
          return true;
        }
      }
      return false;
    }
  }

  class NodeEdgeRel {
    String sourceNode;
    String targetNode;
    boolean isDirected;
    HashMap<String, String> attributes;

    public NodeEdgeRel(
        String sourceNode,
        String targetNode,
        boolean isDirected,
        HashMap<String, String> attributes) {
      this.sourceNode = sourceNode;
      this.targetNode = targetNode;
      this.isDirected = isDirected;
      this.attributes = attributes;
    }

    /**
     * Gets an attribute given a key.
     *
     * @param key The key to use to search for the attribute value
     * @return The value under the key, can be null.
     */
    public String getAttribute(String key) {
      return attributes.get(key);
    }

    public String getSourceNode() {
      return sourceNode;
    }

    public String getTargetNode() {
      return targetNode;
    }

    public boolean isDirected() {
      return isDirected;
    }
  }

  /**
   * This function assumes a lot and is expected to throw exceptions. This will be caught at a
   * higher level when implemented.
   *
   * @param tokens An arraylist of tokens
   * @return A NodeEdgeRel object if parsing was successful
   */
  NodeEdgeRel parseTokensIntoRel(ArrayList<Token> tokens) {
    int currPosition = 0;
    String sourceNode;
    String targetNode;
    boolean isDirected = false;
    HashMap<String, String> attributes = new HashMap<>();

    boolean foundStart = false;

    while (currPosition < tokens.size()) {
      Token currToken = tokens.get(currPosition);
      TokenType tokType = currToken.getTokenType();

      // Some guardrails so that we only eval one at a time and also don't go off the edge
      if (tokType == TokenType.SEPARATOR) {
        // This one depends on the separator tokens not being before the
        // usable tokens when this is called.
        break;
      }
      if (tokType == TokenType.GRAPH_END) {
        break;
      }

      if (tokType == TokenType.ID) {
        // Then we are getting somewhere
        foundStart = true;
        break;
      }
      currPosition++;
    }

    if (!foundStart) {
      throw new NoSuchElementException("The current set of tokens cannot be parsed into a result.");
    }

    if ((tokens.size() - currPosition) < 3) {
      throw new IllegalArgumentException(
          "There are not enough supplied tokens to parse into a relationship");
    }

    // At our current position, we should have the source node ID
    sourceNode = tokens.get(currPosition).getValue();
    currPosition++;

    // Next is our edge operator
    Token operator = tokens.get(currPosition);
    if (operator.getTokenType() != TokenType.EDGE_OPERATOR) {
      throw new IllegalArgumentException(
          "Expected an operator during parsing, did not find a valid token");
    }
    if (operator.getValue().equals("->")) {
      isDirected = true;
    }
    currPosition++;

    // Next up is the target node
    targetNode = tokens.get(currPosition).getValue();
    currPosition++;

    // Attribute handling, technically optional per DOT grammar
    if (currPosition < tokens.size()) {
      Token startAttr = tokens.get(currPosition);
      if (startAttr.tokenType == TokenType.ATTR_START) {
        currPosition++;

        ArrayList<Token> tempAttrList = new ArrayList<>();
        while ((currPosition < tokens.size())
            && (tokens.get(currPosition).getTokenType() != TokenType.ATTR_END)) {
          tempAttrList.add(tokens.get(currPosition));
          currPosition++;
        }

        // Walk the temporary list of tokens to add attributes
        attributes = walkAttributes(tempAttrList);
      } else if ((startAttr.getTokenType() == TokenType.GRAPH_END)
          || (startAttr.getTokenType() == TokenType.SEPARATOR)) {
        // We are done
      } else {
        throw new IllegalArgumentException("Unexpected extra token after edge establishment");
      }
    }

    return new NodeEdgeRel(sourceNode, targetNode, isDirected, attributes);
  }

  /**
   * Given a list of tokens between the attribute start and end tokens, parse out attributes that we
   * need to associate with the edge.
   *
   * @param tokens A list of tokens to parse
   * @return A hashmap contining the key-value attribute pairs to use.
   */
  HashMap<String, String> walkAttributes(ArrayList<Token> tokens) {
    int currPosition = 0;
    HashMap<String, String> attributes = new HashMap<>();
    while (currPosition < tokens.size()) {
      Token currToken = tokens.get(currPosition);

      switch (currToken.getTokenType()) {
        case TokenType.ID -> {
          if ((currPosition + 2) >= tokens.size()) {
            throw new NoSuchElementException(
                "Attempted to parse attributes, but ran into unexpected truncation");
          }

          currPosition++;

          Token equalsToken = tokens.get(currPosition);
          if (equalsToken.getTokenType() != TokenType.EQUALS) {
            throw new IllegalArgumentException(
                "Expected an attribution assignment '=' symbol. "
                    + "Instead, found: "
                    + equalsToken.getValue());
          }

          currPosition++;

          Token rhsToken = tokens.get(currPosition);
          if (rhsToken.getTokenType() != TokenType.ID) {
            throw new IllegalArgumentException(
                "Attempted to assign a non-ID token to an attribute.");
          }

          // If we made it down here, this is an expected sequence of characters.
          attributes.put(currToken.getValue(), rhsToken.getValue());
          break;
        }

        case TokenType.SUB_SEPARATOR -> {
          // This is fine, do nothing and just let advance happen
        }

        default -> {
          throw new IllegalArgumentException(
              "Unexpected token encountered when parsing attributes. "
                  + "Value found: "
                  + currToken.getValue());
        }
      }
      currPosition++;
    }
    return attributes;
  }

  /**
   * Main helper function to take string input and attempt to tokenize it. This will return an
   * ArrayList that can be fed into an analyzer to make sense of the tokens.
   *
   * @param input The raw string to tokenize.
   * @return An ArrayList of parsed tokens.
   */
  ArrayList<Token> tokenizeString(String input) {
    int currentIndex = 0;
    ArrayList<Token> ary = new ArrayList<>();
    boolean subSeparatorSwitch = false;
    while (currentIndex < input.length()) {
      switch (input.charAt(currentIndex)) {

        // ID parsing with quotes
        case '"' -> {
          // Double quote symbol, indicates an ID. Grab all
          // characters until we see another double quote.
          StringBuilder tokValue = new StringBuilder();
          boolean isEscape;
          boolean successEnd = false;
          currentIndex++;
          while ((currentIndex < input.length())) {
            isEscape = false;

            // Advance and indicate escaped double quote. Backslash is the escape character
            if (input.charAt(currentIndex) == '\\') {
              if (((currentIndex + 1) < input.length())
                  && (input.charAt(currentIndex + 1) == '"')) {
                currentIndex++;
                isEscape = true;
              }
            }

            // Otherwise, handle end of ID or add value to stringbuilder
            if (!isEscape && (input.charAt(currentIndex) == '"')) {
              successEnd = true;
              break;
            }

            // Now add value
            tokValue.append(input.charAt(currentIndex));
            currentIndex++;
          }

          if (!successEnd) {
            throw new IllegalArgumentException(
                "Unable to parse an ID cleanly. "
                    + "Resulting string looks like: "
                    + tokValue.toString());
          }

          // Add token to arraylist
          ary.add(new Token(TokenType.ID, tokValue.toString()));
          break;
        }

        // Empty space situation
        case ' ', '\t' -> {
          // If it's just spaces, skip until it's no longer a space or we reach the end
          break;
        }

        // Finding edge operator
        case '-' -> {
          // This is one of our edge operators
          if (currentIndex >= (input.length() - 1)) {
            throw new IllegalArgumentException(
                "Expected edge operator but " + "unexpectedly reached end of parseable string.");
          }

          // Otherwise, we expect the next character to be either '>' or '-'
          currentIndex++; // Advance
          switch (input.charAt(currentIndex)) {
            case '>' -> {
              // Directed
              ary.add(new Token(TokenType.EDGE_OPERATOR, "->"));
            }

            case '-' -> {
              // Undirected
              ary.add(new Token(TokenType.EDGE_OPERATOR, "--"));
            }

            default -> {
              throw new IllegalArgumentException(
                  "Expected edge operator but found invalid symbol: "
                      + '-'
                      + input.charAt(currentIndex));
            }
          }
          break;
        }

        // Attribute start
        case '[' -> {
          ary.add(new Token(TokenType.ATTR_START, "["));
          subSeparatorSwitch = true;
          break;
        }

        // Attribute end
        case ']' -> {
          ary.add(new Token(TokenType.ATTR_END, "]"));
          subSeparatorSwitch = false;
          break;
        }

        // Equals
        case '=' -> {
          ary.add(new Token(TokenType.EQUALS, "="));
          break;
        }

        // Separators
        case ';', ',' -> {
          if (subSeparatorSwitch) {
            ary.add(
                new Token(TokenType.SUB_SEPARATOR, Character.toString(input.charAt(currentIndex))));
          } else {
            ary.add(new Token(TokenType.SEPARATOR, Character.toString(input.charAt(currentIndex))));
          }
          break;
        }

        // Graph start
        case '{' -> {
          ary.add(new Token(TokenType.GRAPH_START, "{"));
          break;
        }

        case '}' -> {
          ary.add(new Token(TokenType.GRAPH_END, "}"));
          break;
        }

        // Handling any other case where there's not a special character
        default -> {
          // If not otherwise caught by one of the above, this is likely just a regular identifier
          char currentChar = input.charAt(currentIndex);

          if (!isValidIDCharFirst(currentChar)) {
            throw new IllegalArgumentException(
                "Unexpected starting character for identifier: " + currentChar);
          }

          StringBuilder idString = new StringBuilder();
          idString.append(currentChar);
          currentIndex++;

          // Okay, let's see if we can parse this as a numeral. We do this first as the generic ID
          // can't start with a digit. currentChar is now behind the current index, only used for
          // the first parseable character determination
          if (isValidNumeral(currentChar)) {
            boolean hasDecimal = false;
            while (currentIndex < input.length()) {
              // At the start, we are already on the 2nd character
              char mayBeADigit = input.charAt(currentIndex);

              if (!isValidNumeral(mayBeADigit)) {
                if (isValidAlphaNumeric(mayBeADigit)) {
                  // Something is not right
                  throw new IllegalArgumentException(
                      "Attempted to parse numeral but "
                          + "found alphanumeric ID allowed characters directly after.");
                } else {
                  break;
                }
              }

              if (mayBeADigit == '.') {
                if (!hasDecimal) {
                  // First decimal point encountered, this is fine
                  hasDecimal = true;
                } else {
                  throw new IllegalArgumentException(
                      "Attempted to parse numeral but saw " + "an extra decimal point.");
                }
              }

              idString.append(mayBeADigit);
              currentIndex++;
            }
          } else {
            // This is an alphanumeric ID
            while (currentIndex < input.length()) {
              char mayBeAlphaNum = input.charAt(currentIndex);

              if (!isValidAlphaNumeric(mayBeAlphaNum)) {
                if (isValidNumeral(mayBeAlphaNum)) {
                  throw new IllegalArgumentException(
                      "Attepmted to parse alphanumeric "
                          + "character but instead found a number-specific symbol "
                          + "that is not allowed for a regular ID.");
                } else {
                  // our ID ends here, this is the start of the next token
                  break;
                }
              }

              idString.append(mayBeAlphaNum);
              currentIndex++;
            }
          }
          currentIndex--; // Move back one so overall advance handles correctly
          ary.add(new Token(TokenType.ID, idString.toString()));
        }
      }

      // Advance counter no matter what after each branch evaluation
      currentIndex++;
    }

    return ary;
  }

  boolean isValidIDCharFirst(char charToTest) {
    return isValidNumeral(charToTest) || isValidAlphaNumeric(charToTest);
  }

  /**
   * Determines if a character is valid for parsing a numeral
   *
   * @return True if valid numeral character, false otherwise
   */
  boolean isValidNumeral(char charToTest) {
    return Character.isDigit(charToTest) || (charToTest == '.');
  }

  /**
   * Determines if a character is valid for parsing an alphanumeric ID string
   *
   * @return True if valid character for ID, false otherwise
   */
  boolean isValidAlphaNumeric(char charToTest) {
    return Character.isAlphabetic(charToTest)
        || Character.isDigit(charToTest)
        || (charToTest == '_');
  }

  /**
   * Helper enum that works along side the Token internal class to facilitate with parsing the dot
   * file.
   */
  enum TokenType {
    ID("ID"), // Numbers are treated as identifiers
    EDGE_OPERATOR("Edge operator"),
    ATTR_START("Attribute left bracket"),
    ATTR_END("Attribute right bracket"),
    EQUALS("Equals"),
    SEPARATOR("Separator"),
    GRAPH_START("Graph DOT file data start"),
    GRAPH_END("Graph DOT file data end"),
    SUB_SEPARATOR("Sub Separator");

    private final String description;

    private TokenType(String s) {
      description = s;
    }

    @Override
    public String toString() {
      return description;
    }
  }

  /** Helper class for parsing the dot file provided into discrete components */
  class Token {
    TokenType tokenType;
    String value;

    public Token(TokenType tokenType, String value) {
      this.tokenType = tokenType;
      this.value = value;
    }

    /**
     * Gets the token type for the token for comparison and parsing purposes.
     *
     * @return The token type
     */
    public TokenType getTokenType() {
      return tokenType;
    }

    /**
     * Gets the string value represented by this token
     *
     * @return The token value
     */
    public String getValue() {
      return value;
    }

    /**
     * Parses the contained value into a double, if able
     *
     * @return A double that represents the string value
     * @throws NumberFormatException if string is not numeric
     */
    public double parseValueToDouble() {
      return Double.parseDouble(value);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[ ");
      sb.append("Token type: ");
      sb.append(tokenType);
      sb.append(", ");
      sb.append("Value: ");
      sb.append(value);
      sb.append(" ]");
      return sb.toString();
    }
  }

  /**
   * Helper function for a place to determine if a parsed keyword is indeed one that is required per
   * the grammar.
   *
   * @param inputString The string to test
   * @return True if the string is a required keyword for a DOT file, false otherwise
   */
  private boolean isRequiredGraphKeyword(String inputString) {
    String lowerCaseString = inputString.toLowerCase();

    for (String keyword : GRAPH_REQ_KEYWORDS) {
      if (lowerCaseString.equals(keyword.toLowerCase())) {
        return true;
      }
    }

    return false;
  }
}
