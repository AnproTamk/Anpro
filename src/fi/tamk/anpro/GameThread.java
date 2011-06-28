package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Huolehtii ajanotosta ja tekoälyjen ja sijaintien päivittämisestä.
 */
class GameThread extends Thread
{
    /* Säikeen tila */
    private boolean running   = false;
    public  boolean allLoaded = false;
    
    /* Tarvittavat luokat */
    private Wrapper      wrapper;
    private AbstractMode gameMode;
    private TouchManager touchManager;
    public  Hud          hud;
    
    /* Pelaaja (VÄLIAIKAINEN!!!) */
    public Player player;
    
    /* Ajanoton muuttujat */
    private long waveStartTime          = 0;
    private long lastMovementUpdate     = 0;
    private long lastAiUpdateStateOne   = 0;
    private long lastAiUpdateStateTwo   = 0;
    private long lastAiUpdateStateThree = 0;
    private long lastAiUpdateStateFour  = 0;
    private long lastCooldownUpdate     = 0;
    private long lastGameModeUpdate		= 0;
    
    /* Tekoälyn nopeutus */
    private float updateSpeedUp = 1;
    
    /* Muille luokille välitettävät muuttujat (tallennetaan väliaikaisesti, sillä muut
       luokat voidaan luoda vasta kun renderöijä on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GLSurfaceView  surface;
    private GameActivity   gameActivity;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     */
    public GameThread(DisplayMetrics _dm, Context _context, GLSurfaceView _surface, GameActivity _gameActivity)
    {
        wrapper = Wrapper.getInstance();
        
        dm      	 = _dm;
        context 	 = _context;
        surface 	 = _surface;
        gameActivity = _gameActivity;
    }

    /**
     * Määrittää säikeen päälle tai pois
     */
    public void setRunning(boolean _run)
    {
        running = _run;
    }

    /**
     * Suorittää säikeen.
     */
    @Override
    public void run()
    {
    	/* Luodaan Hud */
        hud = new Hud(context);
    	
        /* Luodaan pelitila */
        gameMode = new SurvivalMode(dm, context, gameActivity);
        
        /* Luodaan TouchManager */
        touchManager = new TouchManager(dm, surface, context, hud);
        
        /* Merkataan kaikki ladatuksi */
        allLoaded = true;
        
        /* Haetaan päivityksille aloitusajat */
        waveStartTime		   = android.os.SystemClock.uptimeMillis();
        lastMovementUpdate     = waveStartTime;
        lastAiUpdateStateOne   = waveStartTime;
        lastAiUpdateStateTwo   = waveStartTime;
        lastAiUpdateStateThree = waveStartTime;
        lastAiUpdateStateFour  = waveStartTime;
        lastCooldownUpdate     = waveStartTime;
        lastGameModeUpdate	   = waveStartTime;
        
        /* Suoritetaan säiettä kunnes se määritetään pysäytettäväksi */
        while (running) {
            
            // Haetaan tämänhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* Päivitetään sijainnit ja liikkuminen */
            if (currentTime - lastMovementUpdate >= 10) {
                
                lastMovementUpdate = currentTime;
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY ||
                        wrapper.enemyStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
                        
                        wrapper.enemies.get(i).updateMovement(currentTime);
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY ||
                        wrapper.projectileStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
                        
                        wrapper.projectiles.get(i).updateMovement(currentTime);
                    }
                }
            }
            
            /* Päivitetään tekoälyjen päivitysvälit */
            if (currentTime - waveStartTime >= 3000) {
                updateSpeedUp = 2;
            }
           
            /* Päivitetään tekoälyt */
            if (wrapper.player != null) {

                // Päivitetään tila 1
                if (currentTime - lastAiUpdateStateOne >= (400 / updateSpeedUp)) {
                    lastAiUpdateStateOne = currentTime;
                    
                    for (int i : wrapper.priorityOneEnemies) {
                        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityOneProjectiles) {
                        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 2
                if (currentTime - lastAiUpdateStateTwo >= (200 / updateSpeedUp)) {
                    lastAiUpdateStateTwo = currentTime;
                    
                    for (int i : wrapper.priorityTwoEnemies) {
                        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityTwoProjectiles) {
                        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 3
                if (currentTime - lastAiUpdateStateThree >= (100 / updateSpeedUp)) {
                    lastAiUpdateStateThree = currentTime;
                    
                    for (int i : wrapper.priorityThreeEnemies) {
                        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityThreeProjectiles) {
                        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 4
                if (currentTime - lastAiUpdateStateFour >= (50 / updateSpeedUp)) {
                    lastAiUpdateStateFour = currentTime;
                    
                    for (int i : wrapper.priorityFourEnemies) {
                        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityFourProjectiles) {
                        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }
            }
            
            /* Päivitetään aseiden cooldownit */
            if (currentTime - lastCooldownUpdate >= 100) {
            	lastCooldownUpdate = currentTime;
                gameMode.weaponManager.updateCooldowns();
            }
            
            /* Päivitetään vihollisaallot */
            if (currentTime - lastGameModeUpdate >= 1000) {
                if (SurvivalMode.enemiesLeft == 0) {
                    // TODO:
                    waveStartTime = android.os.SystemClock.uptimeMillis();
                    updateSpeedUp = 1;
                    
                    gameMode.startWave();
                }
            }

            /* Hidastetaan säiettä pakottamalla se odottamaan 20 ms */
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}