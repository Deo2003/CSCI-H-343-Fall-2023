[Question 1] What function roughly fits that graph? 
f(n) = n for flood().
[Question 2] What is the time complexity of your flood() function based on analyzing its code? Answer the question in your write-up and write the complexity as a line of comment in your code above the function.

The time complexity of the flood() function is O(N), where N is the number of cells on the game board. This is because the function uses a Breadth-First Search (BFS) algorithm to explore and flood-fill the connected region of cells.

[Question 3] Does your analysis match up with what you see in the graph?
If not, double-check your analysis.

Since the time complexity of the flood() function is linear (O(N)), and the graph is roughly linear when plotting relationship between the input size (board size) and the time taken to execute the function, then the analysis matches up with the graph.

[Question 4] What data structures did you use (linked lists, arrays…) ? Is the time complexity of your flood function the best it can be or can you do better? Why?

In the flood() function, we used a HashSet (visited) to keep track of visited coordinates and a Queue (queue) to perform the BFS traversal.

The time complexity of O(N) is optimal for this problem because we need to visit each cell on the board at most once to determine whether it should be flood-filled or not. 

[Question 5] What is the time complexity of your alternative implementation? Comment the function with its time complexity in your code and write down the time complexity in your write-up.
f(n) = n^2 for flood1(). Time complexity of the flood1() function: O(N^2)