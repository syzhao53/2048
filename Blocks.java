/*  Name: Sylvia Zhao
 *  PennKey: syzhao 
 *  Recitation: 214
 *
 *  Execution: None
 *
 *  Creates blocks of specified values that have different colors depending on the 
 *  block's value
 *
 */
public class Blocks {
    // private fields
    private int val; // number on block
    private double xPos; // x pos on board
    private double yPos; // y pos on board
    private final double HALF_BLOCK_WIDTH = .1;
    
    /*
     * Description: constructor for Blocks that creates a block by initializing its
     *              x and y positions on the board and the value displayed on the 
     *              block
     * Input: none
     * Output: none
     */
    public Blocks(int val, double xPos, double yPos) {
        // initialize fields 
        this.val = val;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    /*
     * Description: draws a block, color depends on the number on the block (val)
     * Input: none
     * Output: none
     */
    public void draw() {
        // call the color function to set the blocks color
        color();
        
        // draw the block at its specified x and y positions
        PennDraw.filledRectangle(xPos, yPos, HALF_BLOCK_WIDTH, HALF_BLOCK_WIDTH);
        
        // convert number on block to a string
        String numOnBlock = "" + val;
        
        if (val == 2 || val == 4) {
            // number on block is black if it is a 2 or a 4
            PennDraw.setPenColor(0, 0, 0);
        } else {
            // number on block is white if it is anything other than a 2 or a 4
            PennDraw.setPenColor(255, 255, 255);
        }
        
        // set font size to 40 and enable bold text
        PennDraw.setFontSize(40);
        PennDraw.setFontBold();
        
        // draw the block's number at the center of the block (block's x and y)
        PennDraw.text(xPos, yPos, numOnBlock);                      
    }
    
    /*
     * Description: sets pen color depending on a block's number (val), helper
     *              function for draw function
     * Input: none
     * Output: none
     */  
    public void color() {
        int red = 0;
        int green = 0;
        int blue = 0;
        
        // set red, green, blue values depending on the block's val
        if (val == 2) {
            // light grey
            red = 237;
            green = 228;
            blue = 219;
        } else if (val == 4) {
            // tan
            red = 235;
            green = 224;
            blue = 202;
        } else if (val == 8) {
            // light orange
            red = 233;
            green = 179;
            blue = 131;
        } else if (val == 16) {
            // medium orange
            red = 233;
            green = 153;
            blue = 109;
        } else if (val == 32) {
            // light red
            red = 239;
            green = 124;
            blue = 94;
        } else if (val == 64) {
            // medium red
            red = 228;
            green = 105;
            blue = 71;
        } else if (val == 128) {
            // yellow
            red = 236;
            green = 207;
            blue = 113;
        } else if (val == 256) {
            // darker yellow
            red = 237;
            green = 203;
            blue = 97;
        } else if (val == 512) {
            // darker yellow
            red = 237;
            green = 200;
            blue = 80;
        } else if (val == 1024) {
            // darker yellow
            red = 237;
            green = 197;
            blue = 63;
        } else if (val == 2048) {
            // darker yellow
            red = 237;
            green = 194;
            blue = 46;
        }
        
        // set pen color with the specified red, green, blue values
        PennDraw.setPenColor(red, green, blue);
    }
    
    /*
     * Description: returns the number displayed on a block (val)
     * Input: none
     * Output: int representing the number displayed on a block (val) 
     */ 
    public int getVal() {
        return val;
    }
    
    /*
     * Description: returns a block's x position
     * Input: none
     * Output: double representing a block's x position
     */ 
    public double getX() {
        return xPos;
    }
    
    /*
     * Description: returns a block's y position
     * Input: none
     * Output: double representing a block's y position
     */ 
    public double getY() {
        return yPos;
    }
    
    /*
     * Description: sets the number displayed on a block (val) to the input int
     * Input: int representing the new number on the block
     * Output: none
     */ 
    public void setVal(int newVal) {
        // change val to the input newVal
        val = newVal;
    }
}