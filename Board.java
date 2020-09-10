/*  Name: Sylvia Zhao
 *  PennKey: syzhao 
 *  Recitation: 214
 *
 *  Execution: java Board
 *
 *  Creates a board with two random blocks to start a game of 2048; allows the player
 *  to use WASD controls to move and merge blocks 
 *
 */
public class Board {
    // private fields
    private int numMoves; // counter for number of moves made
    private boolean[][] blockEmpty; // keeps track of which blocks are empty
    private Blocks[][] blocks; // keeps track of the blocks on the board
    private boolean moveMade; // tracks if a move was made, helps count moves
    
    /*
     * Description: constructor for Board that creates a board by initializing 2D
     *              arrays for whether or not blocks are empty and for block 
     *              values, an int counter for number of moves made, and a boolean 
     *              for if a move was made
     * Input: none
     * Output: none
     */
    public Board() {
        // initialize fields
        numMoves = 0;
        blockEmpty = new boolean[4][4]; // 4 by 4 to represent board
        blocks = new Blocks[4][4]; // 4 by 4 to represent board
        moveMade = false;
        
        // all blocks are "empty" from the start of the game
        for (int i = 0; i < blockEmpty.length; i++) {
            for (int j = 0; j < blockEmpty[i].length; j++) {
                blockEmpty[i][j] = true;
            }
            
            // fill board with blocks of value zero but do not draw
            // if a block has a value of 0, it counts as empty
            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < blocks[i].length; y++) {
                    blocks[x][y] = new Blocks(0, 0, 0);
                }
            }
        }
    }
    
    /*
     * Description: draws the board
     * Input: none
     * Output: none
     */
    public void drawBoard() {
        // draw background 
        PennDraw.setPenColor(185, 173, 162); // color of background
        PennDraw.filledRectangle(.5, .5, .5, .5);
        
        // draw empty block spaces
        PennDraw.setPenColor(203, 193, 182); // tan
        
        // top row
        PennDraw.filledRectangle(.125, .8, .1, .1);
        PennDraw.filledRectangle(.375, .8, .1, .1);
        PennDraw.filledRectangle(.625, .8, .1, .1);
        PennDraw.filledRectangle(.875, .8, .1, .1);
        
        // second row
        PennDraw.filledRectangle(.125, .575, .1, .1);
        PennDraw.filledRectangle(.375, .575, .1, .1);
        PennDraw.filledRectangle(.625, .575, .1, .1);
        PennDraw.filledRectangle(.875, .575, .1, .1);
        
        // third row
        PennDraw.filledRectangle(.125, .35, .1, .1);
        PennDraw.filledRectangle(.375, .35, .1, .1);
        PennDraw.filledRectangle(.625, .35, .1, .1);
        PennDraw.filledRectangle(.875, .35, .1, .1);
        
        // bottom row
        PennDraw.filledRectangle(.125, .125, .1, .1);
        PennDraw.filledRectangle(.375, .125, .1, .1);
        PennDraw.filledRectangle(.625, .125, .1, .1);
        PennDraw.filledRectangle(.875, .125, .1, .1);
        
        // set font size to 28 and bold font
        PennDraw.setFontSize(28);
        PennDraw.setFontBold();
        
        // title at top of screen
        PennDraw.setPenColor(255, 255, 255); // white
        PennDraw.text(.5, .95, "2048");
    }
    
    /*
     * Description: draws an empty block at the specified x and y position; 
     *              simulates the blocks moving as it covers the original spot of 
     *              a block that moves
     * Input: double for the x position of the block, double for the y position of 
     *        the block
     * Output: none
     */
    public void drawEmptyBlock(double xPos, double yPos) {
        // draw empty space
        PennDraw.setPenColor(203, 193, 182); // color of empty spaces
        PennDraw.filledRectangle(xPos, yPos, .1, .1);
    }
    
    /*
     * Description: draws a block of value 2 or 4 at a random space on the board
     * Input: none
     * Output: none
     */
    public void drawRandomBlock() { 
        int randomTwoOrFour = StdRandom.uniform(0, 2); // randomly either 0 or 1
        int numOnBlock = 0; // number displayed on block
        
        // decide if the block is a 2 or a 4 based on the randomly generated 0 or 1
        if (randomTwoOrFour == 0) {
            numOnBlock = 2;
        } else if (randomTwoOrFour == 1) {
            numOnBlock = 4;
        }
        
        // make sure that a block is drawn in a space that is empty
        boolean randomBlock = true;
        
        while (randomBlock) {
            // generate a random row and column for the block
            int row = StdRandom.uniform(0, 4);
            int col = StdRandom.uniform(0, 4);
            
            /* place a block on the board if the random space is empty, otherwise
             * regenerates a random row and column
             */
            if (blockEmpty[row][col]) {
                // create and draw new random block
                Blocks block = new Blocks(numOnBlock, setBlockXPos(col), 
                                          setBlockYPos(row)); 
                block.draw(); 
                
                // update member variable arrays
                blockEmpty[row][col] = false; 
                blocks[row][col] = block;
                
                // end while loop 
                randomBlock = false; 
            }
        }
    }
    
    /*
     * Description: moves blocks up, down, left, or right based on WASD controls,
     *              also increments numMoves if a move was made
     * Input: none
     * Output: none
     */
    public void move() {
        // check if a key was typed
        if (PennDraw.hasNextKeyTyped()) {
            char c = PennDraw.nextKeyTyped();
            
            if (c == 'w') { // moving up
                // iterate across columns
                for (int col = 0; col < blocks.length; col++) {
                    int numMerges = 0; // number of merges made
                    int numFilled = 0; // number of filled blocks in a column
                    int mergesAllowed = 0; // how many merges are allowed
                    int numEmpty = 0; // number of empty spaces after a filled space
                    
                    // count how many blocks in a column are filled
                    for (int r = blocks[col].length - 1; r >= 0; r--) {
                        if (!blockEmpty[r][col]) {
                            ++numFilled;
                        }
                    }
                    
                    if (numFilled == 2 || numFilled == 3) {
                        /* if 2 or 3 blocks are filled in a column, one merge is 
                         * allowed in that column 
                         */ 
                        mergesAllowed = 1;
                    } else if (numFilled == 4) {
                        // array to see how many merges are allowed
                        int[] values = {0, 0, 0, 0}; 
                        
                        // see how many values are repeated  
                        for (int row = 1; row < blocks[col].length; row++) {
                            if (blocks[row][col].getVal() !=
                                blocks[row - 1][col].getVal()) {
                                values[row] = 0;
                            } else if (blocks[row][col].getVal() == 
                                       blocks[row - 1][col].getVal()) { 
                                values[row - 1] += 1;
                            }
                        }
                        
                        // loop through values array for number of merges allowed
                        for (int i = 0; i < values.length; i++) {
                            mergesAllowed += values[i];
                        }
                        
                        /* adjust mergesAllowed if greater than 2 (all 4 blocks are 
                         * the same)
                         */ 
                        if (mergesAllowed > 2) { 
                            mergesAllowed = 2;
                        }
                    }
                    
                    // merging                        
                    for (int same = 0; same < blocks[col].length - 1; same++) {
                        // when mergeable blocks are next to each other
                        if (blocks[same][col].getVal() == 
                            blocks[same + 1][col].getVal() && 
                            blocks[same][col].getVal() != 0 && 
                            numMerges < mergesAllowed && numFilled > 1 && 
                            mergesAllowed > 0) { 
                            /* double the value of the block that is the same 
                             * value as the current block
                             */ 
                            blocks[same + 1][col].setVal(2 * blocks[same][col].
                                                             getVal());
                            blockEmpty[same + 1][col] = false; // block is full
                            
                            // original block becomes empty
                            blocks[same][col].setVal(0); 
                            blockEmpty[same][col] = true;  
                            
                            // draw an empty block where the original block was
                            drawEmptyBlock(setBlockXPos(col), 
                                           setBlockYPos(same));
                            
                            // increment the number of merges made
                            ++numMerges;
                        } else if (blocks[same + 1][col].getVal() == 0 &&
                                   blocks[same][col].getVal() != 0 && numMerges <= 
                                   mergesAllowed && same < 2 && numMerges == 
                                   0 && numFilled > 1 && 
                                   mergesAllowed > 0) { // gap exists btwn blocks
                            // current block is in row 0
                            if (same == 0) {
                                /* if the block in the spot 2 or 3 spots after 
                                 * the current spot has the same value as the 
                                 * current block
                                 */ 
                                if (blocks[same][col].getVal() == 
                                    blocks[same + 2][col].getVal() || 
                                    blocks[same][col].getVal() == 
                                    blocks[same + 3][col].getVal()) {
                                    /* spot that the blocks are merging to
                                     * before being moved  
                                     */ 
                                    int newSpot = 0;
                                    
                                    if (blocks[same][col].getVal() == 
                                        blocks[same + 2][col].getVal()) {
                                        // if block of same value is 2 spots away
                                        newSpot = same + 2; 
                                    } else if (blocks[same][col].getVal() == 
                                               blocks[same + 3][col].getVal()) {
                                        // if block of same value is 3 spots away
                                        newSpot = same + 3;
                                    }
                                    
                                    /* double the value of the block that is the same
                                     * value as the current block
                                     */ 
                                    blocks[newSpot][col].
                                        setVal(2 * blocks[same][col].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[newSpot][col] = false; 
                                    
                                    // original block becomes empty 
                                    blocks[same][col].setVal(0);
                                    blockEmpty[same][col] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(col), 
                                                   setBlockYPos(same));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            } else if (same == 1) { // current row is 1
                                /* if the block in the spot 2 spots after 
                                 * the current spot has the same value as the 
                                 * current block
                                 */ 
                                if (blocks[same][col].getVal() ==
                                    blocks[same + 2][col].getVal()) {
                                    // new spot is two blocks away 
                                    int newSpot = same + 2;
                                    
                                    /* double the value of the block that is the same
                                     * value as the current block
                                     */ 
                                    blocks[newSpot][col].
                                        setVal(2 * blocks[same][col].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[newSpot][col] = false;
                                    
                                    // original block becomes empty 
                                    blocks[same][col].setVal(0);
                                    blockEmpty[same][col] = true; 
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(col),
                                                   setBlockYPos(same));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            }
                        }
                    }
                    
                    // check how many blocks are empty after at least one filled spot
                    for (int co = col; co < col + 1; co++) {
                        for (int r = blocks[col].length - 1; r >= 0; r--) {
                            /* increment numEmpty if block is empty and at least one
                             * spot in the colum is filled
                             */ 
                            if (blockEmpty[r][co] && numFilled != 0) {
                                ++numEmpty;
                            }
                        }
                        
                        // move blocks 
                        for (int e = 0; e < blocks[col].length; e++) {
                            if (!blockEmpty[e][col]) {
                                // new row to which the block will be moved
                                int newRow = e - numEmpty;
                                
                                // make adjustments if newRow is negative
                                if (newRow <= 0 && blockEmpty[0][col]) {
                                    newRow = 0;
                                } else if (newRow <= 0 && !blockEmpty[0][col] && 
                                           e != 0) {
                                    newRow = 1;
                                } else if (newRow <= 0 && !blockEmpty[0][col] && 
                                           e == 0) {
                                    newRow = 0;
                                } else if (!blockEmpty[newRow][col]) { 
                                    ++newRow;
                                } 
                                
                                // redraw blocks
                                if (numEmpty > 0 && numEmpty <= 3 && 
                                    blocks[e][col].getVal() != 0) { 
                                    // new block spot is full
                                    blockEmpty[newRow][col] = false; 
                                    
                                    // create and draw new block
                                    Blocks newBlock = new 
                                        Blocks(blocks[e][col].getVal(), 
                                               setBlockXPos(col), 
                                               setBlockYPos(newRow));  
                                    newBlock.draw();
                                    
                                    // place new block in blocks array
                                    blocks[newRow][col] = newBlock;
                                    
                                    /* space of old block becomes empty if the new 
                                     * spot is not the same as the old spot
                                     */ 
                                    if (newRow != e) {
                                        blockEmpty[e][col] = true;
                                    }
                                }
                                
                                /* draw empty block in original spot if block was 
                                 * moved
                                 */ 
                                if (blocks[e][col].getVal() != 0 && numEmpty > 0 && 
                                    newRow != e && numMerges <= 2) {
                                    drawEmptyBlock(blocks[e][col].getX(), 
                                                   blocks[e][col].getY());     
                                    moveMade = true; // a move was made
                                    
                                    // set original block value to zero
                                    blocks[e][col].setVal(0);
                                }
                            } 
                        }
                        
                        // reset numFilled and numEmpty for next column
                        numFilled = 0;
                        numEmpty = 0;
                    }
                }
            } else if (c == 'a') { // moving left
                // iterate across rows
                for (int row = 0; row <= 3; row++) { 
                    int numMerges = 0; // number of merges made
                    int numFilled = 0; // number of filled spots in a row
                    int mergesAllowed = 0; // number of merges allowed in a row
                    int numEmpty = 0; // number of empty spaces after a filled space
                    
                    // count how many blocks in the row are filled
                    for (int col = 3; col >= 0; col--) {
                        if (!blockEmpty[row][col]) {
                            ++numFilled;
                        }
                    }
                    
                    if (numFilled == 2 || numFilled == 3) {
                        /* if 2 or 3 blocks are filled in a row, one merge is allowed
                         * in that row
                         */ 
                        mergesAllowed = 1;
                    } else if (numFilled == 4) {
                        // array to see how many merges are allowed
                        int[] values = {0, 0, 0, 0}; 
                        
                        // see how many values are repeated 
                        for (int co = 1; co < 4; co++) {
                            if (blocks[row][co].getVal() != 
                                blocks[row][co - 1].getVal()) {
                                values[co] = 0;
                            } else if (blocks[row][co].getVal() == 
                                       blocks[row][co - 1].getVal()) { 
                                values[co - 1] += 1;
                            }
                        }
                        
                        // loop through values array for number of merges allowed
                        for (int i = 0; i < values.length; i++) {
                            mergesAllowed += values[i];
                        }
                        
                        /* adjust mergesAllowed if greater than 2 (all 4 blocks are 
                         * the same)
                         */ 
                        if (mergesAllowed > 2) { 
                            mergesAllowed = 2;
                        }
                    }
                    
                    // merging                        
                    for (int same = 0; same < 3; same++) {
                        // when mergeable blocks are next to each other
                        if (blocks[row][same].getVal() == 
                            blocks[row][same + 1].getVal() &&
                            blocks[row][same].getVal() != 0 && //last edit
                            numMerges < mergesAllowed && numFilled > 1 && 
                            mergesAllowed > 0) { 
                            /* double the value of the block that is the same value 
                             * as the current block
                             */ 
                            blocks[row][same + 1].setVal(2 * blocks[row][same].
                                                             getVal());
                            blockEmpty[row][same + 1] = false; // block is full
                            
                            // original block becomes empty
                            blocks[row][same].setVal(0);
                            blockEmpty[row][same] = true;  
                            
                            // draw an empty block where the original block was
                            drawEmptyBlock(setBlockXPos(same), setBlockYPos(row));
                            
                            // increment the number of merges made
                            ++numMerges;
                        } else if (blocks[row][same + 1].getVal() == 0 &&
                                   blocks[row][same].getVal() != 0 && numMerges <= 
                                   mergesAllowed && same < 2 && numMerges == 0 && 
                                   mergesAllowed > 0 && numFilled > 1) {
                            // if gap exists between blocks 
                            // current block is in column zero
                            if (same == 0) {
                                /* if the block in the spot 2 or 3 spots after 
                                 * the current spot has the same value as the 
                                 * current block
                                 */
                                if (blocks[row][same].getVal() == 
                                    blocks[row][same + 2].getVal() || 
                                    blocks[row][same].getVal() == 
                                    blocks[row][same + 3].getVal()) {
                                    /* spot that the blocks are merging to before 
                                     * being moved
                                     */ 
                                    int newSpot = 0;
                                    
                                    if (blocks[row][same].getVal() == 
                                        blocks[row][same + 2].getVal()) {
                                        newSpot = same + 2;
                                    } else if (blocks[row][same].getVal() == 
                                               blocks[row][same + 3].getVal()) {
                                        newSpot = same + 3;
                                    }
                                    
                                    /* double the value of the block that is the 
                                     * same value as the current block
                                     */ 
                                    blocks[row][newSpot].
                                        setVal(2 * blocks[row][same].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[row][newSpot] = false;
                                    
                                    // original block becomes empty
                                    blocks[row][same].setVal(0);
                                    blockEmpty[row][same] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(same), 
                                                   setBlockYPos(row));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            } else if (same == 1) { // current column is one
                                /* if the block in the spot 2 spots after the 
                                 * current spot has the same value as the current 
                                 * block
                                 */ 
                                if (blocks[row][same].getVal() ==
                                    blocks[row][same + 2].getVal()) {
                                    // new spot is two blocks away
                                    int newSpot = same + 2;
                                    
                                    /* double the value of the block that is the 
                                     * same value as the current block
                                     */ 
                                    blocks[row][newSpot].
                                        setVal(2 * blocks[row][same].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[row][newSpot] = false;
                                    
                                    // original block becomes empty
                                    blocks[row][same].setVal(0);
                                    blockEmpty[row][same] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(same), 
                                                   setBlockYPos(row));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            }
                        }
                    }
                    
                    // check how many blocks are empty after at least one filled spot
                    for (int ro = row; ro < row + 1; ro++) {  
                        for (int column = 3; column >= 0; column--) {
                            /* increment numEmpty if block is empty and at least one
                             * spot in the column is filled
                             */ 
                            if (blockEmpty[row][column] && numFilled != 0) {
                                ++numEmpty;
                            }
                        }
                        
                        // move blocks
                        for (int e = 0; e < 4; e++) {
                            if (!blockEmpty[row][e]) {
                                // new column to which the block will be moved
                                int newCol = e - numEmpty;
                                
                                // make adjustments if newCol is negative
                                if (newCol <= 0 && blockEmpty[row][0]) {
                                    newCol = 0;
                                } else if (newCol <= 0 && !blockEmpty[row][0] &&
                                           e != 0) {
                                    newCol = 1;
                                } else if (newCol <= 0 && !blockEmpty[row][0] &&
                                           e == 0) {
                                    newCol = 0;
                                } else if (!blockEmpty[row][newCol] && 
                                           newCol != 0) { 
                                    ++newCol;
                                }
                                
                                // redraw blocks
                                if (numEmpty > 0 && numEmpty <= 3 && 
                                    blocks[row][e].getVal() != 0) { 
                                    // new block spot is full
                                    blockEmpty[row][newCol] = false; 
                                    
                                    // create and draw new block
                                    Blocks newBlock = new 
                                        Blocks(blocks[row][e].getVal(), 
                                               setBlockXPos(newCol), 
                                               setBlockYPos(row));  
                                    newBlock.draw();
                                    
                                    // place new block in blocks array
                                    blocks[row][newCol] = newBlock;
                                    
                                    /* space of old block becomes empty if the new
                                     * spot is not the same as the old spot
                                     */ 
                                    if (newCol != e) {
                                        blockEmpty[row][e] = true;
                                    }
                                }
                                
                                /* draw empty block in the original spot if the block
                                 * was moved
                                 */ 
                                if (blocks[row][e].getVal() != 0 && numEmpty > 0 && 
                                    newCol != e && numMerges <= 2) {
                                    drawEmptyBlock(blocks[row][e].getX(), 
                                                   blocks[row][e].getY());     
                                    moveMade = true; // a move was made
                                    
                                    // set original block value to zero
                                    blocks[row][e].setVal(0); 
                                }
                            } 
                        }
                        
                        // reset numFilled and numEmpty for next row
                        numFilled = 0;
                        numEmpty = 0;
                    }
                }
            } else if (c == 's') { // moving down
                // iterate across columns
                for (int col = 0; col < blocks.length; col++) {
                    int numMerges = 0; // number of merges made
                    int numFilled = 0; // number of filled spots in a column
                    int mergesAllowed = 0; // number of merges allowed in a column
                    int numEmpty = 0; // number of empty spaces after a filled space
                    
                    // count how many blocks in a column are filled
                    for (int r = blocks[col].length - 1; r >= 0; r--) {
                        if (!blockEmpty[r][col]) {
                            ++numFilled;
                        }
                    }
                    
                    if (numFilled == 2 || numFilled == 3) {
                        /* if 2 or 3 blocks are filled in a column, one merge is 
                         * allowed in that column
                         */ 
                        mergesAllowed = 1;
                    } else if (numFilled == 4) {
                        // array to see how many merges are allowed
                        int[] values = {0, 0, 0, 0}; 
                        
                        // see how many values are repeated 
                        for (int row = 1; row < blocks[col].length; row++) {
                            if (blocks[row][col].getVal() != 
                                blocks[row - 1][col].getVal()) {
                                values[row] = 0;
                            } else if (blocks[row][col].getVal() ==
                                       blocks[row - 1][col].getVal()) { 
                                values[row - 1] += 1;
                            }
                        }
                        
                        // loop through values array for number of merges allowed
                        for (int i = 0; i < values.length; i++) {
                            mergesAllowed += values[i];
                        }
                        
                        /* adjust mergesAllowed if greater than 2 (all 4 blocks are 
                         * the same)
                         */ 
                        if (mergesAllowed > 2) { 
                            mergesAllowed = 2;
                        }
                    }
                    
                    // merging                        
                    for (int same =  blocks[col].length - 1; same > 0; same--) {
                        // when mergeable blocks are next to each other
                        if (blocks[same][col].getVal() == 
                            blocks[same - 1][col].getVal() && 
                            blocks[same][col].getVal() != 0 && 
                            numMerges < mergesAllowed && numFilled > 1 &&
                            mergesAllowed > 0) { 
                            /* double the value of the block that is the same
                             * value as the current block
                             */ 
                            blocks[same - 1][col].setVal(2 * blocks[same][col].
                                                             getVal());
                            blockEmpty[same - 1][col] = false; // block is full
                            
                            // original block becomes empty
                            blocks[same][col].setVal(0);
                            blockEmpty[same][col] = true;  
                            
                            // draw an empty block where the original block was
                            drawEmptyBlock(setBlockXPos(col), setBlockYPos(same));
                            
                            // increment the number of merges made
                            ++numMerges;
                        } else if (blocks[same - 1][col].getVal() == 0 &&
                                   blocks[same][col].getVal() != 0 && numMerges <=
                                   mergesAllowed && same >= 2 && numMerges == 0 && 
                                   numFilled > 1 && mergesAllowed > 0) { 
                            // if gap exists between blocks
                            // current block is in row 3
                            if (same == 3) {
                                /* if the block in the spot 2 or 3 spots after the
                                 * current spot has the same value as the current
                                 * block
                                 */ 
                                if (blocks[same][col].getVal() == 
                                    blocks[same - 2][col].getVal() || 
                                    blocks[same][col].getVal() == 
                                    blocks[same - 3][col].getVal()) {
                                    /* spot that the blocks are merging to before
                                     * being moved
                                     */ 
                                    int newSpot = 0;
                                    
                                    if (blocks[same][col].getVal() == 
                                        blocks[same - 2][col].getVal()) {
                                        // if block of same value is 2 spots away
                                        newSpot = same - 2;
                                    } else if (blocks[same][col].getVal() == 
                                               blocks[same - 3][col].getVal()) {
                                        // if block of same value is 3 spots away
                                        newSpot = same - 3;
                                    }
                                    
                                    /* double the value of the block that is the same
                                     * value as the current block
                                     */ 
                                    blocks[newSpot][col].
                                        setVal(2 * blocks[same][col].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[newSpot][col] = false;
                                    
                                    // original block becomes empty
                                    blocks[same][col].setVal(0);
                                    blockEmpty[same][col] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(col),
                                                   setBlockYPos(same));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            } else if (same == 2) { // current row is 2
                                /* if the block in the spot 2 spots after the current
                                 * spot has the same value as the current block
                                 */ 
                                if (blocks[same][col].getVal() ==
                                    blocks[same - 2][col].getVal()) {
                                    // new spot is two blocks away
                                    int newSpot = same - 2;
                                    
                                    /* double the value of the block that is the same
                                     * value as the current block
                                     */ 
                                    blocks[newSpot][col].
                                        setVal(2 * blocks[same][col].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[newSpot][col] = false;
                                    
                                    // original block becomes empty
                                    blocks[same][col].setVal(0);
                                    blockEmpty[same][col] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(col), 
                                                   setBlockYPos(same));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            }
                        }
                    }
                    
                    // check how many blocks are empty after at least one filled spot
                    for (int co = col; co < col + 1; co++) { 
                        for (int r = blocks[col].length - 1; r >= 0; r--) {
                            /* increment numEmpty if the block is empty and at leat
                             * one spot in the column is filled
                             */ 
                            if (blockEmpty[r][co] && numFilled != 0) {
                                ++numEmpty;
                            }
                        }
                        
                        // move blocks
                        for (int e = blocks[col].length - 1; e >= 0; e--) {
                            if (!blockEmpty[e][col]) {
                                // new row to which the block will be moved
                                int newRow = e + numEmpty;
                                
                                // make adjustments if newRow is negative
                                if (newRow >= 3 && blockEmpty[3][col]) {
                                    newRow = 3;
                                } else if (newRow >= 3 && !blockEmpty[3][col] && 
                                           e != 3) {
                                    newRow = 2;
                                } else if (newRow >= 3 && !blockEmpty[3][col] &&
                                           e == 3) {
                                    newRow = 3;
                                } else if (!blockEmpty[newRow][col]) { 
                                    --newRow;
                                }
                                
                                // redraw blocks
                                if (numEmpty > 0 && numEmpty <= 3 &&
                                    blocks[e][col].getVal() != 0) { 
                                    // new block spot is full
                                    blockEmpty[newRow][col] = false; 
                                    
                                    // create and draw new block
                                    Blocks newBlock = new 
                                        Blocks(blocks[e][col].getVal(),
                                               setBlockXPos(col), 
                                               setBlockYPos(newRow));  
                                    newBlock.draw();
                                    
                                    // place new block in blocks array
                                    blocks[newRow][col] = newBlock;
                                    
                                    /* space of old block becomes empty if the new 
                                     * spot is not the same as the old spot
                                     */ 
                                    if (newRow != e) {
                                        blockEmpty[e][col] = true;
                                    }
                                    
                                }
                                
                                /* draw empty block in original spot if block was
                                 * moved
                                 */ 
                                if (blocks[e][col].getVal() != 0 && numEmpty > 0 && 
                                    newRow != e && numMerges <= 2) {
                                    drawEmptyBlock(blocks[e][col].getX(), 
                                                   blocks[e][col].getY());     
                                    moveMade = true; // a move was made
                                    
                                    // set original block value to zero
                                    blocks[e][col].setVal(0);
                                }
                            } 
                        }
                        
                        // reset numFilled and numEmpty for next column
                        numFilled = 0;
                        numEmpty = 0;
                    }
                }
            } else if (c == 'd') { // moving right
                // iterate across rows
                for (int row = 0; row <= 3; row++) { // 3 is number of rows
                    int numMerges = 0; // number of merges made
                    int numFilled = 0; // number of filled spots in a row
                    int mergesAllowed = 0; // number of merges allowed in a row
                    int numEmpty = 0; // number of empty spaces after a filled space
                    
                    // count how many blocks in the row are filled
                    for (int col = 3; col >= 0; col--) {
                        if (!blockEmpty[row][col]) {
                            ++numFilled;
                        }
                    }
                    
                    if (numFilled == 2 || numFilled == 3) {
                        /* if 2 or 3 blocks are filled in a row, one merge is allowed
                         * in that row
                         */ 
                        mergesAllowed = 1;
                    } else if (numFilled == 4) {
                        // array to see how many merges are allowed
                        int[] values = {0, 0, 0, 0}; // at most 4 diff numbers in col
                        
                        // see how many values are repeated
                        for (int co = 1; co < 4; co++) {
                            if (blocks[row][co].getVal() != 
                                blocks[row][co - 1].getVal()) {
                                values[co] = 0;
                            } else if (blocks[row][co].getVal() == 
                                       blocks[row][co - 1].getVal()) { 
                                values[co - 1] += 1;
                            }
                        }
                        
                        // loop through values array for number of merges allowed
                        for (int i = 0; i < values.length; i++) {
                            mergesAllowed += values[i];
                        }
                        
                        /* adjust mergesAllowed if greater than 2 (all 4 blocks are 
                         * the same)
                         */ 
                        if (mergesAllowed > 2) { 
                            mergesAllowed = 2;
                        }
                    }
                    
                    // merging                        
                    for (int same = 3; same > 0; same--) {
                        // when mergeable blocks are next to each other
                        if (blocks[row][same].getVal() == 
                            blocks[row][same - 1].getVal() && 
                            blocks[row][same].getVal() != 0 && 
                            numMerges < mergesAllowed && numFilled > 1 && 
                            mergesAllowed > 0) { 
                            /* double the value of the block that is the same value 
                             * as the current block
                             */ 
                            blocks[row][same - 1].
                                setVal(2 * blocks[row][same].getVal());
                            blockEmpty[row][same - 1] = false; // block is full
                            
                            // original block becomes empty
                            blocks[row][same].setVal(0);
                            blockEmpty[row][same] = true;  
                            
                            // draw an empty block where the original block was
                            drawEmptyBlock(setBlockXPos(same), setBlockYPos(row));
                            
                            // increment the number of merges made
                            ++numMerges;
                        } else if (blocks[row][same - 1].getVal() == 0 &&
                                   blocks[row][same].getVal() != 0 && numMerges <= 
                                   mergesAllowed && same >= 2 && numMerges == 0 && 
                                   mergesAllowed > 0 && numFilled > 1) { 
                            // if gap exists between blocks
                            // current block is in column 3
                            if (same == 3) {
                                /* if the block in the spot 2 or 3 spots after the 
                                 * current spot has the same value as the current
                                 * block
                                 */ 
                                if (blocks[row][same].getVal() == 
                                    blocks[row][same - 2].getVal() || 
                                    blocks[row][same].getVal() == 
                                    blocks[row][same - 3].getVal()) {
                                    /* spot that the blocks are mergin to before
                                     * being moved
                                     */ 
                                    int newSpot = 0;
                                    
                                    if (blocks[row][same].getVal() == 
                                        blocks[row][same - 2].getVal()) {
                                        // new spot is two blocks away
                                        newSpot = same - 2;
                                    } else if (blocks[row][same].getVal() == 
                                               blocks[row][same - 3].getVal()) {
                                        // new spot is three blocks away
                                        newSpot = same - 3;
                                    }
                                    
                                    /* double the value of the block that is the 
                                     * same value as the current block
                                     */ 
                                    blocks[row][newSpot].
                                        setVal(2 * blocks[row][same].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[row][newSpot] = false;
                                    
                                    // original block becomes empty
                                    blocks[row][same].setVal(0);
                                    blockEmpty[row][same] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(same),
                                                   setBlockYPos(row));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            } else if (same == 2) { // current column is 2
                                /* if the block in the spot 2 spots after the 
                                 * current spot has the same value as the current
                                 * block
                                 */ 
                                if (blocks[row][same].getVal() ==
                                    blocks[row][same - 2].getVal()) {
                                    // new spot is two blocks
                                    int newSpot = same - 2;
                                    
                                    /* double the value of the block that is the 
                                     * same value as the current block
                                     */ 
                                    blocks[row][newSpot].
                                        setVal(2 * blocks[row][same].getVal());
                                    
                                    // block at new spot becomes full
                                    blockEmpty[row][newSpot] = false;
                                    
                                    // original block becomes empty
                                    blocks[row][same].setVal(0);
                                    blockEmpty[row][same] = true;  
                                    
                                    // draw an empty block at the original spot
                                    drawEmptyBlock(setBlockXPos(same),
                                                   setBlockYPos(row));
                                    
                                    // increment number of merges made
                                    ++numMerges;   
                                }
                            }
                        }
                    }
                    
                    // check how many blocks are empty after at least one filled spot
                    for (int ro = row; ro < row + 1; ro++) { 
                        for (int column = 3; column >= 0; column--) {
                            /* increment numEmpty if block is empty and at least one 
                             * spot in the column is filled
                             */ 
                            if (blockEmpty[row][column] && numFilled != 0) {
                                ++numEmpty;
                            }
                        }
                        
                        // move blocks
                        for (int e = 3; e >= 0; e--) {
                            if (!blockEmpty[row][e]) {
                                // new column to which the block will be moved
                                int newCol = e + numEmpty;
                                
                                // make adjustments if newCol is negative
                                if (newCol >= 3 && blockEmpty[row][3]) {
                                    newCol = 3;
                                } else if (newCol >= 3 && !blockEmpty[row][3] && e !=
                                           3) {
                                    newCol = 2;
                                } else if (newCol >= 3 && !blockEmpty[row][3] && e ==
                                           3) {
                                    newCol = 3;
                                } else if (!blockEmpty[row][newCol] && newCol != 0) {
                                    --newCol;
                                }
                                
                                // redraw blocks 
                                if (numEmpty > 0 && numEmpty <= 3 && 
                                    blocks[row][e].getVal() != 0) { 
                                    // new block spot is full
                                    blockEmpty[row][newCol] = false; 
                                    
                                    // create and draw new block
                                    Blocks newBlock = new 
                                        Blocks(blocks[row][e].getVal(), 
                                               setBlockXPos(newCol), 
                                               setBlockYPos(row));  
                                    newBlock.draw();
                                    
                                    // place new block in blocks array
                                    blocks[row][newCol] = newBlock;
                                    
                                    /* space of old block becomes empty if the new
                                     * spot is not the same as the old spot
                                     */ 
                                    if (newCol != e) {
                                        blockEmpty[row][e] = true;
                                    }
                                }
                                
                                /* draw empty block in the original spot if the 
                                 * block was moved
                                 */ 
                                if (blocks[row][e].getVal() != 0 && numEmpty > 0 && 
                                    newCol != e && numMerges <= 2) {
                                    drawEmptyBlock(blocks[row][e].getX(),
                                                   blocks[row][e].getY());     
                                    moveMade = true; // a move was made
                                    
                                    // set original block value to zero
                                    blocks[row][e].setVal(0); 
                                }
                            } 
                        }
                        
                        // reset numFilled and numEmpty for next row  
                        numFilled = 0;
                        numEmpty = 0;
                    }
                }
            }
            
            // draw a new random block if a move was made
            if (moveMade == true) {
                drawRandomBlock();
                moveMade = false; // reset for next move
                
                // increment number of moves made
                ++numMoves; 
            }
        }
    }
    
    /*
     * Description: sets the x position of a block given a column, helper function 
     *              for move function
     * Input: int representing a column on the board
     * Output: double representing the x position of a block
     */
    public double setBlockXPos(int column) {
        if (column == 0) {
            return .125;
        } else if (column == 1) {
            return .375;
        } else if (column == 2) {
            return .625;
        } else {
            return .875; // column == 3
        } 
    }
    
    /*
     * Description: sets the y position of a block given a row, helper function
     *              for move function 
     * Input: int representing a row on the board
     * Output: double representing the y position of a block
     */
    public double setBlockYPos(int row) {
        if (row == 0) {
            return .8;
        } else if (row == 1) {
            return .575;
        } else if (row == 2) {
            return .35;
        } else {
            return .125; // row == 3
        } 
    }
    
    /*
     * Description: checks if the game is over based on the gameWon and gameLost 
     *              functions
     * Input: none
     * Output: boolean representing if the game is over or not
     */
    public boolean isGameOver() {
        return gameWon() || gameLost();
    }
    
    /*
     * Description: checks if the player won the game
     * Input: none
     * Output: boolean representing whether or not the game was won
     */
    public boolean gameWon() {
        /* the game has been won if any of the blocks on the board have the value 
         * 2048
         */ 
        for (int i = 0; i < blockEmpty.length; i++) {
            for (int j = 0; j < blockEmpty[i].length; j++) {
                if (blocks[i][j].getVal() == 2048) {
                    return true;
                }
            }
        }
        
        // return false if no block on the board has the value 2048
        return false;
    }
    
    /*
     * Description: checks if the player lost the game
     * Input: none
     * Output: boolean representing whether or not the game was lost
     */
    public boolean gameLost() {
        for (int i = 0; i < blockEmpty.length; i++) {
            for (int j = 0; j < blockEmpty[i].length; j++) {
                // game can continue if any block is empty or a merge is possible
                if (blockEmpty[i][j] || mergePossible()) { 
                    return false;
                } 
            }
        }
        
        // return true if the board is full and no merges are possible
        return true;
    }
    
    /*
     * Description: checks if any merges overall can be made, helper function for
     *              gameLost function
     * Input: none
     * Output: boolean representing whether or not any merges anywhere on the board
     *         are possible
     */
    public boolean mergePossible() {
        // check if any adjacent blocks have the same value
        for (int i = 0; i < blockEmpty.length; i++) {
            for (int j = 0; j < blockEmpty[i].length; j++) {
                if (i == 0) { // top row
                    if (j == 0) { // top left corner
                        // check values of blocks to the right and below
                        if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    } else if (j == 3) { // top right corner
                        // check values of blocks to the left and below
                        if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j - 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    } else { // other spots in top row
                        // check values of blocks to the left, right, and below
                        if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j - 1].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    }
                } else if (i == 3) { // bottom row
                    if (j == 0) { // bottom left corner
                        // check values of blocks to the right and above
                        if (blocks[i - 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    } else if (j == 3) { // bottom right corner
                        // check values of blocks to the left and above
                        if (blocks[i - 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j - 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    } else { // other spots in bottom row
                        // check values of blocks to the left, right, and above
                        if (blocks[i - 1][j].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j - 1].getVal() == blocks[i][j].getVal() ||
                            blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                            return true;
                        }
                    }
                } else if (j == 0) { // remaining spots in first column
                    // check values of blocks above, below, and to the right
                    if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                        blocks[i - 1][j].getVal() == blocks[i][j].getVal() || 
                        blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                        return true;
                    }
                } else if (j == 3) { // remaining spots in last column
                    // check values of blocks above, below, and to the left
                    if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                        blocks[i - 1][j].getVal() == blocks[i][j].getVal() || 
                        blocks[i][j - 1].getVal() == blocks[i][j].getVal()) {
                        return true;
                    }
                } else { // four spots in the middle of the board
                    // check values of blocks above, below, left, and right 
                    if (blocks[i + 1][j].getVal() == blocks[i][j].getVal() ||
                        blocks[i - 1][j].getVal() == blocks[i][j].getVal() || 
                        blocks[i][j - 1].getVal() == blocks[i][j].getVal() || 
                        blocks[i][j + 1].getVal() == blocks[i][j].getVal()) {
                        return true;
                    } 
                }
            }
        }
        
        // return false if no adjacent blocks have the same values
        return false;
    }
    
    public static void main(String[] args) {
        // create a board
        Board b = new Board();
        
        // draw board
        b.drawBoard();
        
        // draw two random blocks to start off the game
        b.drawRandomBlock();
        b.drawRandomBlock();
        
        // enable animation
        PennDraw.enableAnimation(24);
        
        // allow player to move pieces as long as the game is not over
        while (!b.isGameOver()) {
            // allow pieces to move
            b.move();
            
            // advance animation
            PennDraw.advance();
        }
        
        // disable animation once game is over
        PennDraw.disableAnimation();
        
        // display lose screen if player lost
        if (b.gameLost()) {
            // wait 2 seconds before displaying lose screen
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            PennDraw.clear(); // clear screen
            PennDraw.setPenColor(0, 0, 0); // set pen color to black
            PennDraw.text(.5, .5, "You lost..."); // message
            
            // display number of moves made
            PennDraw.text(.5, .4, "# of moves made: " + b.numMoves); 
        } else if (b.gameWon()) { // display win screen if player won
            // wait 2 seconds before displaying win screen
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            PennDraw.clear(); // clear screen
            PennDraw.setPenColor(0, 0, 0); // set pen color to black
            PennDraw.text(.5, .5, "You won!"); // message
            
            // display number of moves made
            PennDraw.text(.5, .4, "# of moves made: " + b.numMoves);
        }
    }
}
