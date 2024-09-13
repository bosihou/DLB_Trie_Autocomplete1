/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

import java.util.ArrayList;

public class AutoComplete implements AutoCompleteInterface {

    private DLBNode root;
    private StringBuilder currentPrefix;
    private DLBNode currentNode;

    private DLBNode savedNode; 
    // To store the last currentNode in the Trie if we start to have excessive
    // advance
    private int excessiveAdvances = 0;
    // To keep track of number of excessive advances




    //TODO: Add more instance variables if you need to

    public AutoComplete() {
        root = null;
        currentPrefix = new StringBuilder();
        currentNode = null;
    }

    /**
     * Adds a word to the dictionary in O(word.length()) time
     *
     * @param word the String to be added to the dictionary
     * @return true if add is successful, false if word already exists
     * @throws IllegalArgumentException if word is the empty string
     */
    public boolean add(String word) {
        //TODO: implement this method

        if (word == null) throw new IllegalArgumentException("calls add() with an empty string");
        if (word == "") throw new IllegalArgumentException("calls add() with an empty string");

        if (wordExists(root, word, 0)) {
            return false;
        } else {
            DLBNode result = put(root, word, 0);
            root = result;
            return true;
        }

    }

    // To detect whether the word already exists
    private boolean wordExists(DLBNode x, String key, int pos) {
        DLBNode curr = x;
        if (curr == null) {
            return false;
        }

        if (curr.data == key.charAt(pos)) {
            if (pos < key.length() - 1) {
                return wordExists(curr.child, key, pos + 1);
            } else {
                return curr.isWord;
            }
        } else {
            return wordExists(curr.nextSibling, key, pos);
        }

    }

    // To put a key into the Trie
    private DLBNode put(DLBNode x, String key, int pos) {
        DLBNode result = x;

        if (x == null) {
            result = new DLBNode(key.charAt(pos));
            result.size++;
            if (pos < key.length() - 1) {
                result.child = put(result.child, key, pos + 1); /*TODO: Recurse on the child node*/ ;
                result.child.parent = result; 

            } else {
                result.isWord = true;
            }


        } else if (x.data == key.charAt(pos)) {
            x.size++;
            if (pos < key.length() - 1) {
                result.child = put(result.child, key, pos + 1);
                result.child.parent = result; 
            } else {
                result.isWord = true;
            }

        } else {
            result.nextSibling = put(result.nextSibling, key, pos);
            result.nextSibling.previousSibling = result;
        }

        return result;


    }


//================================================================================


    /**
     * appends the character c to the current prefix in O(1) time. This method
     * doesn't modify the dictionary.
     *
     * @param c: the character to append
     * @return true if the current prefix after appending c is a prefix to a word
     * in the dictionary and false otherwise
     */


    //Be aware, update currentNode!
    public boolean advance(char c) {
        //TODO: implement this method

        if (c == 'd') {
            System.out.println("a");
        }


        currentPrefix.append(c);
        String st = currentPrefix.toString();

        return isPrefix(root, st, 0);

    }

    private boolean isPrefix(DLBNode x, String key, int pos) {
        DLBNode curr = x;
        if (x == null) { // It means that we start to have excessive advances
            if (excessiveAdvances == 0) {
                savedNode = currentNode; //save the last valid currentNode
                currentNode = new DLBNode(key.charAt(pos));

                excessiveAdvances++;

            } else if (excessiveAdvances > 0 ) {
                DLBNode previousCurr = currentNode;

                currentNode.child = new DLBNode(key.charAt(pos));
                currentNode = currentNode.child;
                currentNode.parent = previousCurr;

                excessiveAdvances++;
            }




            return false;
        }

        if (curr.data == key.charAt(pos)) {
            if (pos < key.length() - 1) {
                
                return isPrefix(curr.child, key, pos + 1);
            } else {
                currentNode = curr; // Update currentNode. Modify the sentence if bugs occur here
                return true;
            }
        } else {
            return isPrefix(curr.nextSibling, key, pos);
        }


    }







    /**
     * removes the last character from the current prefix in O(1) time. This
     * method doesn't modify the dictionary.
     *
     * @throws IllegalStateException if the current prefix is the empty string
     */
    public void retreat() { // need to implement by myself
        //TODO: implement this method

        

        if (currentPrefix.length() == 0) {
            throw new IllegalStateException();
        }


        currentPrefix.deleteCharAt(currentPrefix.length() - 1);
        if (currentPrefix.length() == 0) {
            currentNode = new DLBNode('0');
            // We create this new Node so that the method getNumberOfPrediction
            // can be correctly executed (cannot pass null Node)

            return;

        }


        char currLastChar = currentPrefix.charAt(currentPrefix.length() -1);




        if (excessiveAdvances >0 ) { // It means we still have excessive advances

            if (currentNode.parent != null) {
                currentNode = currentNode.parent;


            } else {
                currentNode = savedNode; 
                // no more excessive advances
                // back to previous savedNode


                savedNode = null;
            }

            excessiveAdvances--; // Dcrement excessiveAdvances
            return;
        }



        
        if (currentNode.parent != null) {
            currentNode = currentNode.parent;
            return;
        }


        DLBNode tempCurr = currentNode.previousSibling;


        //find currentNode recursively
        currentNode = findNewCurrentNode(tempCurr, currLastChar);
    }

    private DLBNode findNewCurrentNode(DLBNode curr, char currLastChar) {
        DLBNode result1;

        if (curr.parent == null) {
            result1 = findNewCurrentNode(curr.previousSibling, currLastChar);
        } else {
            if (curr.parent.data == currLastChar) {
                result1 = curr.parent;

            } else {
                result1 = findNewCurrentNode(curr.previousSibling, currLastChar);
            }
        }

        return result1;
    }




    /**
     * resets the current prefix to the empty string in O(1) time
     */
    public void reset() {
        currentPrefix = new StringBuilder();
        currentNode = null;
    }

    /**
     * @return true if the current prefix is a word in the dictionary and false
     * otherwise
     */
    public boolean isWord() {
        //TODO: implement this method

        //Note: this is the method of the AutoComplete class, not the field of the Node
        //Similar method. After locating the last char of the prefix, search down (or right)
        // until last char of currentPrefix. Return that boolean (isWord, field)
        
        
        // if (excessiveAdvances >0) {
        //     return false;
        // }

        return isWord(root, currentPrefix.toString(), 0);
    }

    private boolean isWord(DLBNode x, String key, int pos) {
        DLBNode curr = x;
        if (x == null) {
            return false;
        }

        if (curr.data == key.charAt(pos)) {
            if (pos < key.length() - 1) {
                return isPrefix(curr.child, key, pos + 1);
            } else {
                return true;
            }
        } else {
            return isPrefix(curr.nextSibling, key, pos);
        }

    }





    /**
     * adds the current prefix as a word to the dictionary (if not already a word)
     * The running time is O(length of the current prefix).
     */
    public void add() {
        //TODO: implement this method
        if (!wordExists(root, currentPrefix.toString(), 0)) {
            root = put(root, currentPrefix.toString(), 0);
            excessiveAdvances = 0;
        }


        //However, it is simpler to just call the other add method (i.e., add(string)),
        //   which will start from the root of the trie
    }



    /**
     * @return the number of words in the dictionary that start with the current
     * prefix (including the current prefix if it is a word). The running time is
     * O(1).
     */
    public int getNumberOfPredictions() { // need to implement by myself
        //TODO: implement this method

        int result = currentNode.size;

        return result;
    }

    /**
     * retrieves one word prediction for the current prefix. The running time is
     * O(prediction.length()-current prefix.length())
     *
     * @return a String or null if no predictions exist for the current prefix
     */
    public String retrievePrediction() {
        //TODO: implement this method

        //similar method. After locating the last char of the prefix, search down (or right)
        //    until isWord (the field, not the method) of that Node is true
        //Retrieve that prediction

        //Note: Do not change currentNode!
        StringBuilder sb = new StringBuilder();

        int currPrefixLength = currentPrefix.length();

        int currIndex = 0;
        DLBNode currNode = root;

        while (currIndex < currPrefixLength) {
            if (currNode != null) {
                if (currNode.data == currentPrefix.charAt(currIndex)) { // down
                    sb.append(currNode.data);
                    currNode = currNode.child;
                    currIndex++;
                } else { // right
                    currNode = currNode.nextSibling;
                }
            } else {
                return null;
            }


        }

        if (currNode == null) {
            return sb.toString();
        }

        if (currNode.parent != null && currNode.parent.isWord) {
            return sb.toString();
        }


        sb.append(currNode.data);

        while (!currNode.isWord) {
            currNode = currNode.child;
            sb.append(currNode.data);
        }



        return sb.toString();
    }




    /* ==============================
     * Helper methods for debugging.
     * ==============================
     */

    //print the subtrie rooted at the node at the end of the start String
    public void printTrie(String start) {
        System.out.println("==================== START: DLB Trie Starting from \"" + start + "\" ====================");
        if (start.equals("")) {
            printTrie(root, 0);
        } else {
            DLBNode startNode = getNode(root, start, 0);
            if (startNode != null) {
                printTrie(startNode.child, 0);
            }
        }

        System.out.println("==================== END: DLB Trie Starting from \"" + start + "\" ====================");
    }

    //a helper method for printTrie
    private void printTrie(DLBNode node, int depth) {
        if (node != null) {
            for (int i = 0; i < depth; i++) {
                System.out.print(" ");
            }
            System.out.print(node.data);
            if (node.isWord) {
                System.out.print(" *");
            }
            System.out.println(" (" + node.size + ")");
            printTrie(node.child, depth + 1);
            printTrie(node.nextSibling, depth);
        }
    }

    //return a pointer to the node at the end of the start String.
    private DLBNode getNode(DLBNode node, String start, int index) {
        if (start.length() == 0) {
            return node;
        }
        DLBNode result = node;
        if (node != null) {
            if ((index < start.length() - 1) && (node.data == start.charAt(index))) {
                result = getNode(node.child, start, index + 1);
            } else if ((index == start.length() - 1) && (node.data == start.charAt(index))) {
                result = node;
            } else {
                result = getNode(node.nextSibling, start, index);
            }
        }
        return result;
    }

    //The DLB node class
    private class DLBNode {
        private char data;
        private int size;
        private boolean isWord;
        private DLBNode nextSibling; //right
        private DLBNode previousSibling; // left
        private DLBNode child; //down
        private DLBNode parent; // up

        private DLBNode(char data) {
            this.data = data;
            size = 0;
            isWord = false;
            nextSibling = previousSibling = child = parent = null; // initialization
        }
    }
}
