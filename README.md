# Java Autocomplete System

This project implements an autocomplete system in Java using the **De La Briandais (DLB) trie**. The DLB trie is a data structure designed to efficiently store and retrieve strings, commonly used in applications like autocomplete. Unlike traditional tries, where each node stores multiple children for characters, the DLB trie leverages linked lists at each node, allowing for more flexible memory usage and faster lookups in large datasets. The system provides efficient word prediction based on user input.



## Features

- **Word Insertion**: Add words into the DLB trie.
- **Prefix Search**: Predicts words based on user input.
- **Dynamic Word Addition**: Users can add new words to the dictionary.

## DLB Trie Structure

The **DLB trie** is a tree-like data structure where each node represents a character in a word. Nodes are linked via sibling and child pointers, making it efficient for storing and predicting words.

```java
private class DLBNode {
    char data;
    int size;
    boolean isWord;
    DLBNode nextSibling, child;
}
```

## Core Methods
- `add(String word)`: Inserts a word into the DLB trie.
- `advance(char c)`: Appends a character to the current prefix.
- `retreat()`: Removes the last character from the prefix.
- `retrievePrediction()`: Returns a word prediction based on the current prefix.


## Example Usage
```java
AutoComplete ac = new AutoComplete();
ac.add("hello");
ac.add("hi");
ac.advance('h');  // 'h' -> predictions: "hello", "hi"
```


## How to Use

1. **Clone and compile**:

    ```bash
    git clone https://github.com/username/JavaAutocompleteSystem.git
    javac AutoComplete.java
    ```

2. **Run test cases**:

    ```bash
    java Test small.txt
    ```


## License

All source code is available under a BSD 3-clause license. You may freely use and modify the code, without warranty, provided that attribution to the authors is maintained. See the LICENSE.md file for the full license text.

