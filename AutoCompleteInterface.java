/**
 * An interface for a dictionary that provides word suggestions for an 
 * auto-complete system and maintains a current prefix
 */

public interface AutoCompleteInterface {

  /**
   * Adds a word to the dictionary in O(alphabet size*word.length()) time
   * @param word the String to be added to the dictionary
   * @return true if add is successful, false if word already exists
   * @throws IllegalArgumentException if word is the empty string
   */
    public boolean add(String word);

  /**
   * appends the character c to the current prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the current prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
    public boolean advance(char c);

  /**
   * removes the last character from the current prefix in O(alphabet size) 
   * time. This method doesn't modify the dictionary.
   * @throws IllegalStateException if the current prefix is the empty string
   */
    public void retreat();

  /**
   * resets the current prefix to the empty string in O(1) time
   */
    public void reset();
    
  /**
   * @return true if the current prefix is a word in the dictionary and false
   * otherwise. The running time is O(1).
   */
    public boolean isWord();

  /**
   * adds the current prefix as a word to the dictionary (if not already a word)
   * The running time is O(alphabet size*length of the current prefix). 
   */
    public void add();

  /** 
   * @return the number of words in the dictionary that start with the current 
   * prefix (including the current prefix if it is a word). The running time is 
   * O(1).
   */
    public int getNumberOfPredictions();
  
  /**
   * retrieves one word prediction for the current prefix. The running time is 
   * O(prediction.length()-current prefix.length())
   * @return a String or null if no predictions exist for the current prefix
   */
    public String retrievePrediction();

}