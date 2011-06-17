package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Huolehtii ajanotosta ja teko�lyjen ja sijaintien p�ivitt�misest�.
 */
class GameThread extends Thread
{
	/* S�ikeen tila */
    private boolean running = false;
    
    /* Tarvittavat luokat */
    private Wrapper      wrapper;
    private AbstractMode gameMode;
    
    /* Pelaaja (V�LIAIKAINEN!!!) */
    public Player player;
    
    /* Ajanoton muuttujat */
    private long lastMovementUpdate  = 0;
    private long lastAiUpdate        = 0;
    private long lastCooldownUpdate  = 0;
    private long lastAnimationUpdate = 0;
    
    /* Kriittisten p�ivitysten lista teko�lyj� varten */
    public static ArrayList<AbstractAi> criticalUpdates;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics N�yt�n tiedot
     * @param Context		 Ohjelman konteksti
     */
    public GameThread(DisplayMetrics _dm, Context _context)
    {
        wrapper = Wrapper.getInstance();
        
        criticalUpdates = new ArrayList<AbstractAi>();

        gameMode = new SurvivalMode(_dm, _context);
        
        /* DEBUGGIA!!!! */
        player = new Player(5, 2);
        player.x = 0;
        player.y = 0;
    }

    /**
     * M��ritt�� s�ikeen p��lle tai pois
     */
    public void setRunning(boolean _run)
    {
        running = _run;
    }

    /**
     * Suoritt�� s�ikeen.
     */
    @Override
    public void run()
    {
        // Haetaan p�ivityksille aloitusajat
        lastMovementUpdate  = android.os.SystemClock.uptimeMillis();
        lastAiUpdate        = lastMovementUpdate;
        lastCooldownUpdate  = lastMovementUpdate;
        lastAnimationUpdate = lastMovementUpdate;
        
        //int debugTimes = 0;
        
        // Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi
        while (running) {
        	
        	// Haetaan t�m�nhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            // P�ivitet��n sijainnit ja liikkuminen
            if (currentTime - lastMovementUpdate >= 20) {
            	
            	//LogWriter.startClock("SijaintiJaLiikkuminen");
            	
                lastMovementUpdate = currentTime;
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == 1) {
                        wrapper.enemies.get(i).updateMovement(currentTime);
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == 1) {
                        wrapper.projectiles.get(i).updateMovement(currentTime);
                    }
                }
                //LogWriter.stopClock("SijaintiJaLiikkuminen");
            }
            
            // P�ivitet��n animaatiot
            if (currentTime - lastAnimationUpdate >= 40) {
                
            	//LogWriter.startClock("Animaatiot");
            	
                lastAnimationUpdate = currentTime;
                
                // p�ivitet��n pelaajan animaatio
                if (wrapper.player.usedAnimation != -1) {
                    wrapper.player.update();
                }
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == 1 && wrapper.enemies.get(i).usedAnimation != -1) {
                        wrapper.enemies.get(i).update();
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == 1 && wrapper.projectiles.get(i).usedAnimation != -1) {
                        wrapper.projectiles.get(i).update();
                    }
                }
                
                //LogWriter.stopClock("Animaatiot");
            }
            
            // P�ivitet��n kriittiset teko�lyt
            if (wrapper.player != null && criticalUpdates.size() > 0) {
            	
            	//LogWriter.startClock("KriittisetTekoalyt");
            	
                if (currentTime - lastAiUpdate >= 50) {
                    for (int i = criticalUpdates.size()-1; i >= 0; --i) {
                        criticalUpdates.get(i).handleAi();
                    }
                }
                
                // Tyhjennet��n p�ivityslista
                criticalUpdates.clear();
                
                //LogWriter.stopClock("KriittisetTekoalyt");
            }
           
            // P�ivitet��n teko�lyt
            if (wrapper.player != null) {
            	
            	LogWriter.startClock("PaivitetaanTekoalyt");
            	
                if (currentTime - lastAiUpdate >= 100) {
                    lastAiUpdate = currentTime;
                    
                    int testAmount = 0;
                    for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                            ++testAmount;
                        }
                    }
                    if (testAmount == 0) {
                    	debugTimes = 500;
                    }
                    
                    for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }
                
                //LogWriter.stopClock("PaivitetaanTekoalyt");    
            }
            
            // P�ivitet��n aseiden cooldownit
            if (currentTime - lastCooldownUpdate >= 100) {
            	
            	//LogWriter.startClock("PaivitetaanCooldownit");
            	
            	gameMode.weaponManager.updateCooldowns();
            	
            	//LogWriter.stopClock("PaivitetaanCooldownit");
            	
            }

            // Hidastetaan s�iett� pakottamalla se odottamaan 20 ms
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            /*++debugTimes;
            Log.v("GameThread", "debugTimes" + debugTimes);
            
            if (debugTimes >= 500) {
            	LogWriter.saveData();
            	System.exit(0);
            }*/
        }
    }
}