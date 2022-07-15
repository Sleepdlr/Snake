/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.sql.Connection;

/**
 *
 * @author Viktória
 */
public class Game {

    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection conn = null;
        conn = Database.ConnectDB();
        SnakeGUI gui = new SnakeGUI();
    }
    
}
