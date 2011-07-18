package fi.tamk.anpro;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Lataa ja varastoi tekstuurit ja hallitsee niiden piirt�misen ruudulle.
 */
public class GLRenderer implements Renderer
{
    /* Vakiot peliobjektien tekstuureille ja animaatioille */
    public static final int TEXTURE_STATIC   = 0;
    public static final int TEXTURE_INACTIVE = 1;
    
    public static final int ANIMATION_STATIC   = 0; // TODO: Vaihda j�rjestyst�! Useimmilla on STATIC ja DESTROY,
    public static final int ANIMATION_MOVE     = 1; // joten ne pit�� asettaa ensimm�isiksi.
    public static final int ANIMATION_SHOOT    = 2;
    public static final int ANIMATION_DESTROY  = 3;
    public static final int ANIMATION_DISABLED = 4;
    
    public static final int ANIMATION_COLLECTED = 0;

    /* Vakiot HUDin tekstuureille ja animaatioille */
    public static final int TEXTURE_BUTTON_NOTSELECTED = 0;
    public static final int TEXTURE_BUTTON_SELECTED    = 1;
    public static final int TEXTURE_JOYSTICK           = 2;
    public static final int TEXTURE_HEALTH             = 3;
    public static final int TEXTURE_COOLDOWN           = 14;
    public static final int TEXTURE_COUNTER			   = 24;
    public static final int TEXTURE_ARMOR			   = 34;
    public static final int TEXTURE_GUIDEARROW		   = 47;
    public static final int TEXTURE_RADAR			   = 49;
    public static final int TEXTURE_MESSAGE			   = 50;
    
    public static final int ANIMATION_CLICK         = 0;
    public static final int ANIMATION_RADAR_WARNING = 1;
    
    /* Animaatioiden ja tekstuurien m��r�t */
    public static final int AMOUNT_OF_PLAYER_TEXTURES        = 4;
    public static final int AMOUNT_OF_ALLY_TEXTURES          = 1;
    public static final int AMOUNT_OF_ENEMY_TEXTURES         = 4;
    public static final int AMOUNT_OF_PROJECTILE_TEXTURES    = 4;
    public static final int AMOUNT_OF_HUD_TEXTURES           = 51;
    public static final int AMOUNT_OF_OBSTACLE_TEXTURES      = 3;
    public static final int AMOUNT_OF_COLLECTABLE_TEXTURES   = 1;
    public static final int AMOUNT_OF_MOTHERSHIP_TEXTURES    = 1;
    
    public static final int AMOUNT_OF_PLAYER_ANIMATIONS      = 5;
    public static final int AMOUNT_OF_ALLY_ANIMATIONS        = 4;
    public static final int AMOUNT_OF_ENEMY_ANIMATIONS       = 5;
    public static final int AMOUNT_OF_PROJECTILE_ANIMATIONS  = 5;
    public static final int AMOUNT_OF_HUD_ANIMATIONS         = 5;
    public static final int AMOUNT_OF_EFFECT_ANIMATIONS      = 9;
    public static final int AMOUNT_OF_OBSTACLE_ANIMATIONS    = 0;
    public static final int AMOUNT_OF_COLLECTABLE_ANIMATIONS = 1;
    public static final int AMOUNT_OF_MOTHERSHIP_ANIMATIONS  = 0;
    
    /* Latausruudun tekstuurit ja tila */
    private GLSpriteSet loadingTexture;
    private boolean showLoadingScreen = false;
   
    /* Piirrett�v�t animaatiot ja objektit */
    public static GLSpriteSet[]     playerTextures;
    public static GLSpriteSet[]   playerAnimations;
    public static GLSpriteSet[][]   allyTextures;
    public static GLSpriteSet[][] allyAnimations;
    public static GLSpriteSet[][]   enemyTextures;
    public static GLSpriteSet[][] enemyAnimations;
    public static GLSpriteSet[][]   projectileTextures;
    public static GLSpriteSet[][] projectileAnimations;
    public static GLSpriteSet[]     mothershipTextures;
    public static GLSpriteSet[]   mothershipAnimations;
    
    public static GLSpriteSet[]     hudTextures;
    public static GLSpriteSet[]   hudAnimations;
    
    public static GLSpriteSet[]   effectAnimations;
    
    public static GLSpriteSet[][]   obstacleTextures;
    public static GLSpriteSet[][] obstacleAnimations;
    
    public static GLSpriteSet[]     collectableTextures;
    public static GLSpriteSet[]   collectableAnimations;
    
    public static GLSpriteSet       starBackgroundTexture;
    
    /* Ohjelman konteksti ja resurssit */
    private Context   context;
    private Resources resources;
    
    /* Tarvittavat oliot */
    private Wrapper       wrapper;
    private GameThread    gameThread = null;
    
    /* Lataustiedot (kertoo, onko tekstuureja viel� ladattu) */
    public        boolean allLoaded     = false;
    public static boolean loadingFailed = false;
    
    /* Animaatiop�ivitysten muuttujat */
    private long lastAnimationUpdate;
    private long lastMessageUpdate;
    private int  updateBeat = 1;
    private boolean updateAnimations = true;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context        Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param Resources      Ohjelman resurssit
     * @param DisplayMetrics N�yt�n tiedot
     */
    public GLRenderer(Context _context, GLSurfaceView _surface, Resources _resources, DisplayMetrics _dm)
    {
        // M��ritet��n taulukoiden koot
        playerTextures        = new GLSpriteSet[AMOUNT_OF_PLAYER_TEXTURES];
        playerAnimations      = new GLSpriteSet[AMOUNT_OF_PLAYER_ANIMATIONS];
        allyTextures          = new GLSpriteSet[2][AMOUNT_OF_ALLY_TEXTURES];
        allyAnimations        = new GLSpriteSet[2][AMOUNT_OF_ALLY_ANIMATIONS];
        enemyTextures         = new GLSpriteSet[5][AMOUNT_OF_ENEMY_TEXTURES];
        enemyAnimations       = new GLSpriteSet[5][AMOUNT_OF_ENEMY_ANIMATIONS];
        projectileTextures    = new GLSpriteSet[5][AMOUNT_OF_PROJECTILE_TEXTURES];
        projectileAnimations  = new GLSpriteSet[5][AMOUNT_OF_PROJECTILE_ANIMATIONS];
        mothershipTextures    = new GLSpriteSet[AMOUNT_OF_MOTHERSHIP_TEXTURES];
        mothershipAnimations  = new GLSpriteSet[AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        hudTextures           = new GLSpriteSet[AMOUNT_OF_HUD_TEXTURES];
        hudAnimations         = new GLSpriteSet[AMOUNT_OF_HUD_ANIMATIONS];
        effectAnimations      = new GLSpriteSet[AMOUNT_OF_EFFECT_ANIMATIONS];
        obstacleTextures      = new GLSpriteSet[3][AMOUNT_OF_OBSTACLE_TEXTURES];
        obstacleAnimations    = new GLSpriteSet[3][AMOUNT_OF_OBSTACLE_ANIMATIONS];
        collectableTextures   = new GLSpriteSet[AMOUNT_OF_COLLECTABLE_TEXTURES];
        collectableAnimations = new GLSpriteSet[AMOUNT_OF_COLLECTABLE_ANIMATIONS];
        
        // Tallennetaan konteksti ja resurssit
        context   = _context;
        resources = _resources;
        
        // Otetaan Wrapper k�ytt��n
        wrapper = Wrapper.getInstance();
    }

    /**
     * M��ritt�� OpenGL-asetukset ja lataa tekstuurit.
     * Android kutsuu t�t� automaattisesti.
     * 
     * @param _gl     OpenGL-konteksti
     * @param _config OpenGL-asetukset
     */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan k�ytt��n shademalli
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirret��n tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // M��ritet��n l�pin�kyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // M��ritet��n blendausasetukset
        _gl.glEnable(GL10.GL_BLEND);
        _gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        // M��ritet��n syvyysasetukset
        _gl.glEnable(GL10.GL_DEPTH_TEST);
        _gl.glClearDepthf(1.0f);
        
        // TODO: Kaksi alempaa rivi� jotenkin ep�loogisessa paikassa
    	// Ladataan latausruudun tekstuuri
    	loadingTexture = new GLSpriteSet(_gl, context, R.drawable.loading, 1);
    }

    /**
     * M��ritt�� pinnan uudet asetukset.
     * Android kutsuu t�t� automaattisesti.
     * 
     * @param _gl      OpenGL-konteksti
     * @param _width   N�yt�n leveys
     * @param _height  N�yt�n korkeus
     */
    public void onSurfaceChanged(GL10 _gl, int _width, int _height)
    {
        // Estet��n nollalla jakaminen
        if (_height == 0) {
            _height = 1;
        }

        // Resetoidaan viewport
        _gl.glViewport(0, 0, _width, _height);

        // Valitaan ja resetoidaan projektiomatriisi
        _gl.glMatrixMode(GL10.GL_PROJECTION);
        _gl.glLoadIdentity();
        
        // M��ritet��n ruudun koko (OpenGL:�� varten)
        GLU.gluOrtho2D(_gl, -(_width/2), (_width/2), -(_height/2), (_height/2));

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /**
     * K�sittelee pinnan tuhoamisen j�lkeiset toiminnot.
     * Android kutsuu t�t� automaattisesti.
     */
    public void onSurfaceDestroyed() {
    	// ...
    }

    /**
     * K�y l�pi piirtolistat ja piirt�� tarvittavat tekstuurit ruudulle.
     * Android kutsuu t�t� automaattisesti (maks. 60 kertaa sekunnissa).
     * 
     * @param _gl OpenGL-konteksti
     */
    public void onDrawFrame(GL10 _gl)
    {
        // Otetaan 2D-piirt�minen k�ytt��n
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        
        // Tyhj�t��n ruutu ja syvyyspuskuri
        _gl.glClearColor(0, 0, 0, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        if (showLoadingScreen) {
	    	try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			showLoadingScreen = false;
        }
        
        /* Tarkastetaan onko tekstuurit ladattu */
        if (allLoaded && gameThread.allLoaded) {
            
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - lastAnimationUpdate >= 40) {
                lastAnimationUpdate = currentTime;
                updateAnimations = true;
            }
            
            /* K�yd��n l�pi piirtolistat ja p�ivitet��n animaatiot */
        	// T�htitausta
            for (int i = wrapper.backgroundStars.size()-1; i >= 0; --i) {
            	wrapper.backgroundStars.get(i).draw(_gl);
            }
            
            // Emoalus
            if (wrapper.mothership != null) {
            	wrapper.mothership.draw(_gl);
            	if (updateAnimations && wrapper.mothership.usedAnimation != -1 && updateBeat % wrapper.mothership.animationSpeed == 0) {
                	wrapper.mothership.update();
                }
            }
            
            // Viholliset
            for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	            if (wrapper.enemyStates.get(i) != Wrapper.INACTIVE) {
	                wrapper.enemies.get(i).draw(_gl);
	                if (updateAnimations && wrapper.enemies.get(i).usedAnimation != -1 && updateBeat % wrapper.enemies.get(i).animationSpeed == 0) {
	                	wrapper.enemies.get(i).update();
	                }
	            }
            }
            
            // Ammukset
            for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
            	if (wrapper.projectileStates.get(i) != Wrapper.INACTIVE) {
                	wrapper.projectiles.get(i).draw(_gl);
                    if (updateAnimations && wrapper.projectiles.get(i).usedAnimation != -1 && updateBeat % wrapper.projectiles.get(i).animationSpeed == 0) {
                        wrapper.projectiles.get(i).update();
                    }
            	}
            }
        
            // Pelaaja
            if (wrapper.player != null && wrapper.playerState != Wrapper.INACTIVE) {
                wrapper.player.draw(_gl);
            	if (updateAnimations && wrapper.player.usedAnimation != -1 && updateBeat % wrapper.player.animationSpeed == 0) {
                    wrapper.player.update();
                }
            }
            
            // Efektit
            for (int i = wrapper.effects.size()-1; i >= 0; --i) {
                if (wrapper.effectStates.get(i) != Wrapper.INACTIVE) {
                    wrapper.effects.get(i).draw(_gl);
                    if (updateAnimations && wrapper.effects.get(i).usedAnimation != -1 && updateBeat % wrapper.effects.get(i).animationSpeed == 0) {
                        wrapper.effects.get(i).update();
                    }
                }
            }
            
            // Ker�tt�v�t esineet
            for (int i = wrapper.collectables.size()-1; i >= 0; --i) {
                if (wrapper.collectableStates.get(i) != Wrapper.INACTIVE) {
                    wrapper.collectables.get(i).draw(_gl);
                    if (updateAnimations && wrapper.collectables.get(i).usedAnimation != -1 && updateBeat % wrapper.collectables.get(i).animationSpeed == 0) {
                        wrapper.collectables.get(i).update();
                    }
                }
            }
            
            // Aurinko, planeetat ja asteroidit
            for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
                if (wrapper.obstacleStates.get(i) != Wrapper.INACTIVE) {
                	wrapper.obstacles.get(i).draw(_gl);
                	if (updateAnimations && wrapper.obstacles.get(i).usedAnimation != -1 && updateBeat % wrapper.obstacles.get(i).animationSpeed == 0) {
                        wrapper.obstacles.get(i).update();
                	}
                }
            }
            
            // HUD
            for (int i = wrapper.guiObjects.size()-1; i >= 0; --i) {
                if (wrapper.guiObjectStates.get(i) != Wrapper.INACTIVE) {
            		wrapper.guiObjects.get(i).draw(_gl);
                	if (updateAnimations && wrapper.guiObjects.get(i).usedAnimation != -1 && updateBeat % wrapper.guiObjects.get(i).animationSpeed == 0) {
                		wrapper.guiObjects.get(i).update();
                	}
                }
            }
            // TODO: Napit pit�isi lis�t� Wrapperin piirtolistalle, jottei render�ij�n
            // tarvitse kutsua sek� pelis�iett� ett� HUDia nappeja p�ivitt��kseen.
            for (int i = gameThread.hud.buttons.size()-1; i >= 0; --i) {
            	gameThread.hud.buttons.get(i).update();
            }
            if (currentTime - lastMessageUpdate >= 50) {
            	lastMessageUpdate = currentTime;
            	
	            for (int i = wrapper.messages.size()-1; i >= 0; --i) {
	            	if (wrapper.messageStates.get(i) != Wrapper.INACTIVE) {
	            		wrapper.messages.get(i).updateAngle();
	            	}
	            }
            }
            
            updateAnimations = false;
                
            // Kasvatetaan updateBeat:ia ja aloitetaan kierros alusta, mik�li raja ylitet��n.
            // T�ll� animaatioiden p�ivitt�minen tahdistetaan; Animaatiot voivat n�ky� joko
            // joka kierroksella, joka toisella, joka nelj�nnell� tai joka kahdeksannella
            // kierroksella.
            ++updateBeat;
            
            if (updateBeat > 32) {
                updateBeat = 1;
            }
        }
        /* Tekstuureja ei ole viel� ladattu */
        else if (!allLoaded && gameThread != null) {
        	// N�ytet��n latausruutu
        	loadingTexture.draw(_gl, 0, 0, 90, 0);
        	showLoadingScreen = true;
        	
            // Ladataan grafiikat ja k�ynnistet��n pelis�ie
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: K�sittele virhe PAREMMIN
            	System.exit(0);
            }
        }
    }

    /**
     * Yhdist�� render�ij�n pelis�ikeeseen tallentamalla pelis�ikeen pointterin muistiin.
     * 
     * @param _gameThread Osoitin pelis�ikeeseen
     */
    public final void connectToGameThread(GameThread _gameThread) {
        gameThread = _gameThread;
    }

    /**
     * Lataa kaikki tekstuurit ja animaatiot.
     * 
     * @param _gl OpenGL-konteksti
     * 
     * @return Onnistuiko lataaminen?
     */
    private final boolean loadTextures(GL10 _gl)
    {
        /* Ladataan pelaajan grafiikat */
        playerTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.player_tex_0, 1); 
        playerAnimations[3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        
        /* Ladataan liittolaisten grafiikat */
        // Emoalus
        mothershipTextures[0] = new GLSpriteSet(_gl, context, R.drawable.mothership_tex_0, 1);
        
        // Liittolainen #1
        allyTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1);
        allyAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1);

        /* Ladataan vihollisten grafiikat */
        // Vihollinen #1
        enemyTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy1_tex_0, 1);
        enemyAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        enemyAnimations[0][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20);
        
        // Vihollinen #2
        enemyTextures[1][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy2_tex_0, 1);
        enemyAnimations[1][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        enemyAnimations[1][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20);
        
        // Vihollinen #3
        enemyTextures[2][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy3_tex_0, 1);
        enemyAnimations[2][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        enemyAnimations[2][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20);
        
        // Vihollinen #4
        enemyTextures[3][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy4_tex_0, 1);
        enemyAnimations[3][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        enemyAnimations[3][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20);

        // Vihollinen #4
        enemyTextures[4][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy5_tex_0, 1);
        enemyAnimations[4][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20);
        enemyAnimations[4][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20);

        /* Ladataan ammusten grafiikat */
        // Vakioase
        projectileTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilelaser_tex_0, 1);
        projectileAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.projectilelaser_destroy_anim, 5);

        // EMP
        projectileTextures[1][0]   = new GLSpriteSet(_gl, context, R.drawable.projectileemp_destroy_anim, 1);
        projectileAnimations[1][3] = new GLSpriteSet(_gl, context, R.drawable.projectileemp_destroy_anim, 10);

        // Py�riv� laser
        projectileTextures[2][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilespinninglaser_destroy_anim, 1);
        projectileAnimations[2][3] = new GLSpriteSet(_gl, context, R.drawable.projectilespinninglaser_destroy_anim, 10);

        // Bomb
        projectileTextures[3][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1);
        projectileAnimations[3][3] = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1);

        // Missile
        projectileTextures[4][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilemissile_tex_0, 1);
        projectileAnimations[4][3] = new GLSpriteSet(_gl, context, R.drawable.projectilemissile_destroy_anim, 1);

        /* Ladataan k�ytt�liittym�n grafiikat */
        // Napit
        hudTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.button_tex_0, 1);
        hudTextures[1]   = new GLSpriteSet(_gl, context, R.drawable.button_tex_1, 1);
        
        hudAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.button_clicked_anim, 9);

        // Joystick
        hudTextures[2]  = new GLSpriteSet(_gl, context, R.drawable.joystick_tex_0, 1);

        // El�m�palkki
        hudTextures[3]  = new GLSpriteSet(_gl, context, R.drawable.healthbar_texs, 11);

        // Cooldown-tekstuurit
        hudTextures[14] = new GLSpriteSet(_gl, context, R.drawable.cooldown_texs, 10);

        //Counter-tekstuurit
        hudTextures[24] = new GLSpriteSet(_gl, context, R.drawable.counter_texs, 10);

        // Armorpalkki
        hudTextures[34] = new GLSpriteSet(_gl, context, R.drawable.armorbar_texs, 11);
        
        // Aseet
        hudTextures[45] = new GLSpriteSet(_gl, context, R.drawable.missile_tex_0, 1);
        hudTextures[46] = new GLSpriteSet(_gl, context, R.drawable.missile_tex_1, 1);
        
        // Kohteen osoittavat nuolet
        hudTextures[47] = new GLSpriteSet(_gl, context, R.drawable.collectablearrow_tex_0, 1);
        hudTextures[48] = new GLSpriteSet(_gl, context, R.drawable.mothershiparrow_tex_0, 1);
        
        // Tutka
        hudTextures[49]  = new GLSpriteSet(_gl, context, R.drawable.radar_tex_0, 1);
        hudAnimations[1] = new GLSpriteSet(_gl, context, R.drawable.radar_warning_anim, 3);
        
        // Ilmoitukset
        hudTextures[50] = new GLSpriteSet(_gl, context, R.drawable.outofboundsmessage_tex_0, 1);
        
        /* Ladataan efektien grafiikat */
        // Huutomerkki
        effectAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.exclamationmark_effect_anim, 1);
        
        // Kysymysmerkki
        effectAnimations[1] = new GLSpriteSet(_gl, context, R.drawable.questionmark_effect_anim, 1);
        
        // Armor-suoja
        effectAnimations[2] = new GLSpriteSet(_gl, context, R.drawable.armor_effect_anim, 9);
        
        // Combomultiplier
        effectAnimations[4] = new GLSpriteSet(_gl, context, R.drawable.combo_effect_texs, 4);
        
        /* Ladataan kartan grafiikat */
        obstacleTextures[0][0] = new GLSpriteSet(_gl, context, R.drawable.planetearth_tex_0, 1);
        obstacleTextures[0][1] = new GLSpriteSet(_gl, context, R.drawable.planet_tex_0, 1);
        obstacleTextures[1][0] = new GLSpriteSet(_gl, context, R.drawable.asteroid_tex_0, 1);
        obstacleTextures[2][0] = new GLSpriteSet(_gl, context, R.drawable.star_tex_0, 1);
    	starBackgroundTexture  = new GLSpriteSet(_gl, context, R.drawable.bgstar_tex_0, 1);
    	
    	/* Ladataan ker�tt�vien esineiden grafiikat */
    	collectableTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.collectable_tex_0, 1);
    	collectableAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.collectable_collected_anim, 12);

    	/* Tarkistetaan virheet */
        if (!loadingFailed) {
        	allLoaded = true;
        	
        	return true;
        }
        else {
        	return false;
        }
    }

    /**
     * K�ynnist�� pelis�ikeen.
     */
    private final void startThread()
    {
        if (gameThread != null) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }
}
