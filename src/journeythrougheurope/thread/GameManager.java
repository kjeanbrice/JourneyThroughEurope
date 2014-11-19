/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.thread;

import journeythrougheurope.ui.PlayerManager;

/**
 *
 * @author Karl
 */
public class GameManager 
{
 
   private PlayerManager player;
   
   public GameManager(PlayerManager player)
   {
       this.player = player;
   }
   
   
   public PlayerManager getPlayerManager()
   {
       return player;
   }
    
}
