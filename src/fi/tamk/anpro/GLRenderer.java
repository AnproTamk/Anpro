package fi.tamk.anpro;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.DisplayMetrics;

/**
 * Lataa ja varastoi tekstuurit ja hallitsee niiden piirtämisen ruudulle.
 */
public class GLRenderer implements Renderer
{
    /* Vakiot peliobjektien tekstuureille ja animaatioille */
    public static final int TEXTURE_STATIC   = 0;
    public static final int TEXTURE_INACTIVE = 1;
    
    public static final int ANIMATION_STATIC   = 0; // TODO: Vaihda järjestystä! Useimmilla on STATIC ja DESTROY,
    public static final int ANIMATION_MOVE     = 1; // joten ne pitää asettaa ensimmäisiksi.
    public static final int ANIMATION_SHOOT    = 2;
    public static final int ANIMATION_DESTROY  = 3;
    public static final int ANIMATION_DISABLED = 4;
    
    public static final int ANIMATION_COLLECTED = 1;

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
    
    public static final int ANIMATION_CLICK = 0;
    public static final int ANIMATION_READY = 1;
    
    /* Animaatioiden ja tekstuurien määrät */
    public static final int AMOUNT_OF_PLAYER_TEXTURES        = 4;
    public static final int AMOUNT_OF_ALLY_TEXTURES          = 1;
    public static final int AMOUNT_OF_ENEMY_TEXTURES         = 4;
    public static final int AMOUNT_OF_PROJECTILE_TEXTURES    = 4;
    public static final int AMOUNT_OF_HUD_TEXTURES           = 63;
    public static final int AMOUNT_OF_OBSTACLE_TEXTURES      = 3;
    public static final int AMOUNT_OF_COLLECTABLE_TEXTURES   = 1;
    public static final int AMOUNT_OF_MOTHERSHIP_TEXTURES    = 1;
    
    public static final int AMOUNT_OF_PLAYER_ANIMATIONS      = 5;
    public static final int AMOUNT_OF_ALLY_ANIMATIONS        = 4;
    public static final int AMOUNT_OF_ENEMY_ANIMATIONS       = 5;
    public static final int AMOUNT_OF_PROJECTILE_ANIMATIONS  = 5;
    public static final int AMOUNT_OF_HUD_ANIMATIONS         = 4;
    public static final int AMOUNT_OF_EFFECT_ANIMATIONS      = 9;
    public static final int AMOUNT_OF_OBSTACLE_ANIMATIONS    = 0;
    public static final int AMOUNT_OF_COLLECTABLE_ANIMATIONS = 2;
    public static final int AMOUNT_OF_MOTHERSHIP_ANIMATIONS  = 0;
    
    /* Latausruudun tekstuurit ja tila */
    private Texture loadingTexture;
    private boolean showLoadingScreen = false;
   
    /* Piirrettävät animaatiot ja objektit */
    public static Texture[]     playerTextures;
    public static Animation[]   playerAnimations;
    public static Texture[][]   allyTextures;
    public static Animation[][] allyAnimations;
    public static Texture[][]   enemyTextures;
    public static Animation[][] enemyAnimations;
    public static Texture[][]   projectileTextures;
    public static Animation[][] projectileAnimations;
    public static Texture[]     mothershipTextures;
    public static Animation[]   mothershipAnimations;
    
    public static Texture[]     hudTextures;
    public static Animation[]   hudAnimations;
    
    public static Animation[]   effectAnimations;
    
    public static Texture[][]   obstacleTextures;
    public static Animation[][] obstacleAnimations;
    
    public static Texture[]     collectableTextures;
    public static Animation[]   collectableAnimations;
    
    public static Texture       starBackgroundTexture;
    
    /* Ohjelman konteksti ja resurssit */
    private Context   context;
    private Resources resources;
    
    /* Tarvittavat oliot */
    private Wrapper       wrapper;
    private GameThread    gameThread = null;
    
    /* Lataustiedot (kertoo, onko tekstuureja vielä ladattu) */
    public        boolean allLoaded     = false;
    public static boolean loadingFailed = false;
    
    /* Animaatiopäivitysten muuttujat */
    private long lastAnimationUpdate;
    private int  updateBeat = 1;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context        Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param Resources      Ohjelman resurssit
     * @param DisplayMetrics Näytön tiedot
     */
    public GLRenderer(Context _context, GLSurfaceView _surface, Resources _resources, DisplayMetrics _dm)
    {
        // Määritetään taulukoiden koot
        playerTextures        = new Texture[AMOUNT_OF_PLAYER_TEXTURES];
        playerAnimations      = new Animation[AMOUNT_OF_PLAYER_ANIMATIONS];
        allyTextures          = new Texture[2][AMOUNT_OF_ALLY_TEXTURES];
        allyAnimations        = new Animation[2][AMOUNT_OF_ALLY_ANIMATIONS];
        enemyTextures         = new Texture[5][AMOUNT_OF_ENEMY_TEXTURES];
        enemyAnimations       = new Animation[5][AMOUNT_OF_ENEMY_ANIMATIONS];
        projectileTextures    = new Texture[5][AMOUNT_OF_PROJECTILE_TEXTURES];
        projectileAnimations  = new Animation[5][AMOUNT_OF_PROJECTILE_ANIMATIONS];
        mothershipTextures    = new Texture[AMOUNT_OF_MOTHERSHIP_TEXTURES];
        mothershipAnimations  = new Animation[AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        hudTextures           = new Texture[AMOUNT_OF_HUD_TEXTURES];
        hudAnimations         = new Animation[AMOUNT_OF_HUD_ANIMATIONS];
        effectAnimations      = new Animation[AMOUNT_OF_EFFECT_ANIMATIONS];
        obstacleTextures      = new Texture[3][AMOUNT_OF_OBSTACLE_TEXTURES];
        obstacleAnimations    = new Animation[3][AMOUNT_OF_OBSTACLE_ANIMATIONS];
        collectableTextures   = new Texture[AMOUNT_OF_COLLECTABLE_TEXTURES];
        collectableAnimations = new Animation[AMOUNT_OF_COLLECTABLE_ANIMATIONS];
        
        // Tallennetaan konteksti ja resurssit
        context   = _context;
        resources = _resources;
        
        // Otetaan Wrapper käyttöön
        wrapper = Wrapper.getInstance();
    }

    /**
     * Määrittää OpenGL-asetukset ja lataa tekstuurit.
     * Android kutsuu tätä automaattisesti.
     * 
     * @param _gl     OpenGL-konteksti
     * @param _config OpenGL-asetukset
     */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan käyttöön shademalli
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirretään tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Määritetään läpinäkyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Määritetään blendausasetukset
        _gl.glEnable(GL10.GL_BLEND);
        _gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        // TODO: Kaksi alempaa riviä jotenkin epäloogisessa paikassa
    	// Ladataan latausruudun tekstuuri
    	loadingTexture = new Texture(_gl, context, R.drawable.loading);
    }

    /**
     * Määrittää pinnan uudet asetukset.
     * Android kutsuu tätä automaattisesti.
     * 
     * @param _gl      OpenGL-konteksti
     * @param _width   Näytön leveys
     * @param _height  Näytön korkeus
     */
    public void onSurfaceChanged(GL10 _gl, int _width, int _height)
    {
        // Estetään nollalla jakaminen
        if (_height == 0) {
            _height = 1;
        }

        // Resetoidaan viewport
        _gl.glViewport(0, 0, _width, _height);

        // Valitaan ja resetoidaan projektiomatriisi
        _gl.glMatrixMode(GL10.GL_PROJECTION);
        _gl.glLoadIdentity();
        
        // Määritetään ruudun koko (OpenGL:ää varten)
        GLU.gluOrtho2D(_gl, -(_width/2), (_width/2), -(_height/2), (_height/2));

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /**
     * Käsittelee pinnan tuhoamisen jälkeiset toiminnot.
     * Android kutsuu tätä automaattisesti.
     */
    public void onSurfaceDestroyed() {
    	// ...
    }

    /**
     * Käy läpi piirtolistat ja piirtää tarvittavat tekstuurit ruudulle.
     * Android kutsuu tätä automaattisesti (maks. 60 kertaa sekunnissa).
     * 
     * @param _gl OpenGL-konteksti
     */
    public void onDrawFrame(GL10 _gl)
    {
        // Otetaan 2D-piirtäminen käyttöön
        _gl.glEnable(GL10.GL_TEXTURE_2D);

        if (showLoadingScreen) {
	    	try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO: Käsittele virhe
			}
			
			showLoadingScreen = false;
        }
        
        // Tyhjätään ruutu ja syvyyspuskuri
        _gl.glClearColor(0, 0, 0, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        /* Tarkastetaan onko tekstuurit ladattu */
        if (allLoaded && gameThread.allLoaded) {
            
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - lastAnimationUpdate >= 40) {
                lastAnimationUpdate = currentTime;
            }
            
            /* Käydään läpi piirtolistat ja päivitetään animaatiot */
        	// Tähtitausta
            for (int i = wrapper.backgroundStars.size()-1; i >= 0; --i) {
            	wrapper.backgroundStars.get(i).draw(_gl);
            }
            
            // Emoalus
            if (wrapper.mothership != null) {
            	if (wrapper.mothership.usedAnimation != -1 && updateBeat % wrapper.mothership.animationSpeed == 0) {
                	wrapper.mothership.update();
                }
            	wrapper.mothership.draw(_gl);
            }
            
            // Aurinko, planeetat ja asteroidit
            for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
                if (wrapper.obstacleStates.get(i) != Wrapper.INACTIVE) {
                	if (wrapper.obstacles.get(i).usedAnimation != -1 && updateBeat % wrapper.obstacles.get(i).animationSpeed == 0) {
                        wrapper.obstacles.get(i).update();
                	}
                	wrapper.obstacles.get(i).draw(_gl);
                }
            }
            
            // Viholliset
            for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	            if (wrapper.enemyStates.get(i) != Wrapper.INACTIVE) {
	                if (wrapper.enemies.get(i).usedAnimation != -1 && updateBeat % wrapper.enemies.get(i).animationSpeed == 0) {
	                	wrapper.enemies.get(i).update();
	                }
	                wrapper.enemies.get(i).draw(_gl);
	            }
            }
            
            // Ammukset
            for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
            	if (wrapper.projectileStates.get(i) != Wrapper.INACTIVE) {
                    if (wrapper.projectiles.get(i).usedAnimation != -1 && updateBeat % wrapper.projectiles.get(i).animationSpeed == 0) {
                        wrapper.projectiles.get(i).update();
                    }
                	wrapper.projectiles.get(i).draw(_gl);
            	}
            }
            
            // Kerättävät esineet
            for (int i = wrapper.collectables.size()-1; i >= 0; --i) {
                if (wrapper.collectableStates.get(i) != Wrapper.INACTIVE) {
                    if (wrapper.collectables.get(i).usedAnimation != -1 && updateBeat % wrapper.collectables.get(i).animationSpeed == 0) {
                        wrapper.collectables.get(i).update();
                    }
                    wrapper.collectables.get(i).draw(_gl);
                }
            }
        
            // Pelaaja
            if (wrapper.player != null && wrapper.playerState != Wrapper.INACTIVE) {
            	if (wrapper.player.usedAnimation != -1 && updateBeat % wrapper.player.animationSpeed == 0) {
                    wrapper.player.update();
                }
                wrapper.player.draw(_gl);
            }
            
            // Efektit
            for (int i = wrapper.effects.size()-1; i >= 0; --i) {
                if (wrapper.effectStates.get(i) != Wrapper.INACTIVE) {
                    if (wrapper.effects.get(i).usedAnimation != -1 && updateBeat % wrapper.effects.get(i).animationSpeed == 0) {
                        wrapper.effects.get(i).update();
                    }
                    wrapper.effects.get(i).draw(_gl);
                }
            }
            
            // HUD
            for (int i = wrapper.guiObjects.size()-1; i >= 0; --i) {
                if (wrapper.guiObjectStates.get(i) != Wrapper.INACTIVE) {
                	if (wrapper.guiObjects.get(i).usedAnimation != -1 && updateBeat % wrapper.guiObjects.get(i).animationSpeed == 0) {
                		wrapper.guiObjects.get(i).update();
                	}
            		wrapper.guiObjects.get(i).draw(_gl);
                }
            }
            // TODO: Napit pitäisi lisätä Wrapperin piirtolistalle, jottei renderöijän
            // tarvitse kutsua sekä pelisäiettä että HUDia nappeja päivittääkseen.
            for (int i = gameThread.hud.buttons.size()-1; i >= 0; --i) {
            	gameThread.hud.buttons.get(i).update();
            }
                
            // Kasvatetaan updateBeat:ia ja aloitetaan kierros alusta, mikäli raja ylitetään.
            // Tällä animaatioiden päivittäminen tahdistetaan; Animaatiot voivat näkyä joko
            // joka kierroksella, joka toisella, joka neljännellä tai joka kahdeksannella
            // kierroksella.
            ++updateBeat;
            
            if (updateBeat > 8) {
                updateBeat = 1;
            }
        }
        /* Tekstuureja ei ole vielä ladattu */
        else if (!allLoaded && gameThread != null) {
        	// Näytetään latausruutu
        	loadingTexture.draw(_gl, 0, 0, 90, 0);
        	showLoadingScreen = true;
        	
            // Ladataan grafiikat ja käynnistetään pelisäie
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: Käsittele virhe PAREMMIN
            	System.exit(0);
            }
        }
    }

    /**
     * Yhdistää renderöijän pelisäikeeseen tallentamalla pelisäikeen pointterin muistiin.
     * 
     * @param _gameThread Osoitin pelisäikeeseen
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
        playerTextures[0]   = new Texture(_gl, context, R.drawable.player_tex_0); 
        playerAnimations[3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        
        /* Ladataan liittolaisten grafiikat */
        // Emoalus
        mothershipTextures[0] = new Texture(_gl, context, R.drawable.mothership_tex_0);
        
        // Liittolainen #1
        allyTextures[0][0]   = new Texture(_gl, context, R.drawable.projectilebomb_destroy_anim_0);
        allyAnimations[0][3] = new Animation(_gl, context, resources, "projectilebomb_destroy", 1);

        /* Ladataan vihollisten grafiikat */
        // Vihollinen #1
        enemyTextures[0][0]   = new Texture(_gl, context, R.drawable.enemy1_tex_0);
        enemyAnimations[0][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        enemyAnimations[0][4] = new Animation(_gl, context, resources, "enemy1_disabled", 20);
        
        // Vihollinen #2
        enemyTextures[1][0]   = new Texture(_gl, context, R.drawable.enemy2_tex_0);
        enemyAnimations[1][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        enemyAnimations[1][4] = new Animation(_gl, context, resources, "enemy1_disabled", 20);
        
        // Vihollinen #3
        enemyTextures[2][0]   = new Texture(_gl, context, R.drawable.enemy3_tex_0);
        enemyAnimations[2][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        enemyAnimations[2][4] = new Animation(_gl, context, resources, "enemy1_disabled", 20);
        
        // Vihollinen #4
        enemyTextures[3][0]   = new Texture(_gl, context, R.drawable.enemy4_tex_0);
        enemyAnimations[3][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        enemyAnimations[3][4] = new Animation(_gl, context, resources, "enemy1_disabled", 20);

        // Vihollinen #4
        enemyTextures[4][0]   = new Texture(_gl, context, R.drawable.enemy5_tex_0);
        enemyAnimations[4][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        enemyAnimations[4][4] = new Animation(_gl, context, resources, "enemy1_disabled", 20);

        /* Ladataan ammusten grafiikat */
        // Vakioase
        projectileTextures[0][0]   = new Texture(_gl, context, R.drawable.projectilelaser_tex_0);
        projectileAnimations[0][3] = new Animation(_gl, context, resources, "projectilelaser_destroy", 5);

        // EMP
        projectileTextures[1][0]   = new Texture(_gl, context, R.drawable.projectileemp_anim_9);
        projectileAnimations[1][3] = new Animation(_gl, context, resources, "projectileemp", 10);

        // Pyörivä laser
        projectileTextures[2][0]   = new Texture(_gl, context, R.drawable.projectilespinninglaser_destroy_anim_0);
        projectileAnimations[2][3] = new Animation(_gl, context, resources, "projectilespinninglaser_destroy", 10);

        // Bomb
        projectileTextures[3][0]   = new Texture(_gl, context, R.drawable.projectilebomb_destroy_anim_0);
        projectileAnimations[3][3] = new Animation(_gl, context, resources, "projectilebomb_destroy", 1);

        // Missile
        projectileTextures[4][0]   = new Texture(_gl, context, R.drawable.projectilemissile_tex_0);
        projectileAnimations[4][3] = new Animation(_gl, context, resources, "projectilemissile_destroy", 1);

        /* Ladataan käyttöliittymän grafiikat */
        // Napit
        hudTextures[0]   = new Texture(_gl, context, R.drawable.button_tex_0);
        hudTextures[1]   = new Texture(_gl, context, R.drawable.button_tex_1);
        hudAnimations[0] = new Animation(_gl, context, resources, "button_clicked", 9);

        // Joystick
        hudTextures[2]  = new Texture(_gl, context, R.drawable.joystick_tex_0);

        // Elämäpalkki
        hudTextures[3]  = new Texture(_gl, context, R.drawable.healthbar_tex_0);
        hudTextures[4]  = new Texture(_gl, context, R.drawable.healthbar_tex_1);
        hudTextures[5]  = new Texture(_gl, context, R.drawable.healthbar_tex_2);
        hudTextures[6]  = new Texture(_gl, context, R.drawable.healthbar_tex_3);
        hudTextures[7]  = new Texture(_gl, context, R.drawable.healthbar_tex_4);
        hudTextures[8]  = new Texture(_gl, context, R.drawable.healthbar_tex_5);
        hudTextures[9]  = new Texture(_gl, context, R.drawable.healthbar_tex_6);
        hudTextures[10] = new Texture(_gl, context, R.drawable.healthbar_tex_7);
        hudTextures[11] = new Texture(_gl, context, R.drawable.healthbar_tex_8);
        hudTextures[12] = new Texture(_gl, context, R.drawable.healthbar_tex_9);
        hudTextures[13] = new Texture(_gl, context, R.drawable.healthbar_tex_10);

        // Cooldown-tekstuurit
        hudTextures[14] = new Texture(_gl, context, R.drawable.cooldown_tex_9);
        hudTextures[15] = new Texture(_gl, context, R.drawable.cooldown_tex_8);
        hudTextures[16] = new Texture(_gl, context, R.drawable.cooldown_tex_7);
        hudTextures[17] = new Texture(_gl, context, R.drawable.cooldown_tex_6);
        hudTextures[18] = new Texture(_gl, context, R.drawable.cooldown_tex_5);
        hudTextures[19] = new Texture(_gl, context, R.drawable.cooldown_tex_4);
        hudTextures[20] = new Texture(_gl, context, R.drawable.cooldown_tex_3);
        hudTextures[21] = new Texture(_gl, context, R.drawable.cooldown_tex_2);
        hudTextures[22] = new Texture(_gl, context, R.drawable.cooldown_tex_1);
        hudTextures[23] = new Texture(_gl, context, R.drawable.cooldown_tex_0);

        //Counter-tekstuurit
        hudTextures[24] = new Texture(_gl, context, R.drawable.counter_text_0);
        hudTextures[25] = new Texture(_gl, context, R.drawable.counter_text_1);
        hudTextures[26] = new Texture(_gl, context, R.drawable.counter_text_2);
        hudTextures[27] = new Texture(_gl, context, R.drawable.counter_text_3);
        hudTextures[28] = new Texture(_gl, context, R.drawable.counter_text_4);
        hudTextures[29] = new Texture(_gl, context, R.drawable.counter_text_5);
        hudTextures[30] = new Texture(_gl, context, R.drawable.counter_text_6);
        hudTextures[31] = new Texture(_gl, context, R.drawable.counter_text_7);
        hudTextures[32] = new Texture(_gl, context, R.drawable.counter_text_8);
        hudTextures[33] = new Texture(_gl, context, R.drawable.counter_text_9);

        // Armorpalkki
        hudTextures[34] = new Texture(_gl, context, R.drawable.armorbar_tex_0);
        hudTextures[35] = new Texture(_gl, context, R.drawable.armorbar_tex_1);
        hudTextures[36] = new Texture(_gl, context, R.drawable.armorbar_tex_2);
        hudTextures[37] = new Texture(_gl, context, R.drawable.armorbar_tex_3);
        hudTextures[38] = new Texture(_gl, context, R.drawable.armorbar_tex_4);
        hudTextures[39] = new Texture(_gl, context, R.drawable.armorbar_tex_5);
        hudTextures[40] = new Texture(_gl, context, R.drawable.armorbar_tex_6);
        hudTextures[41] = new Texture(_gl, context, R.drawable.armorbar_tex_7);
        hudTextures[42] = new Texture(_gl, context, R.drawable.armorbar_tex_8);
        hudTextures[43] = new Texture(_gl, context, R.drawable.armorbar_tex_9);
        hudTextures[44] = new Texture(_gl, context, R.drawable.armorbar_tex_10);
        
        // Aseet
        hudTextures[45] = new Texture(_gl, context, R.drawable.missile_tex_0);
        hudTextures[46] = new Texture(_gl, context, R.drawable.missile_tex_1);
        
        // Kohteen osoittavat nuolet
        hudTextures[47] = new Texture(_gl, context, R.drawable.collectablearrow_tex_0);
        hudTextures[48] = new Texture(_gl, context, R.drawable.mothershiparrow_tex_0);
        
        // Tutka
        hudTextures[49] = new Texture(_gl, context, R.drawable.radar_tex_0);
        hudTextures[50] = new Texture(_gl, context, R.drawable.radar_tex_1);
        hudTextures[51] = new Texture(_gl, context, R.drawable.radar_tex_2);
        hudTextures[52] = new Texture(_gl, context, R.drawable.radar_tex_3);
        hudTextures[53] = new Texture(_gl, context, R.drawable.radar_tex_4);
        hudTextures[54] = new Texture(_gl, context, R.drawable.radar_tex_5);
        hudTextures[55] = new Texture(_gl, context, R.drawable.radar_tex_6);
        hudTextures[56] = new Texture(_gl, context, R.drawable.radar_tex_7);
        hudTextures[57] = new Texture(_gl, context, R.drawable.radar_tex_8);
        hudTextures[58] = new Texture(_gl, context, R.drawable.radar_tex_9);
        hudTextures[59] = new Texture(_gl, context, R.drawable.radar_tex_10);
        hudTextures[60] = new Texture(_gl, context, R.drawable.radar_tex_11);
        hudTextures[61] = new Texture(_gl, context, R.drawable.radar_tex_12);
        
        // Ilmoitukset
        hudTextures[62] = new Texture(_gl, context, R.drawable.outofboundsmessage_tex_0);
        
        /* Ladataan efektien grafiikat */
        // Huutomerkki
        effectAnimations[0] = new Animation(_gl, context, resources, "exclamationmark_effect", 1);
        
        // Kysymysmerkki
        effectAnimations[1] = new Animation(_gl, context, resources, "questionmark_effect", 1);
        
        // Armor-suoja
        effectAnimations[2] = new Animation(_gl, context, resources, "armor_effect", 4);
        
        // Armor-suoja
        effectAnimations[3] = new Animation(_gl, context, resources, "armor_effect", 4);
        
        // Combomultiplier
        effectAnimations[4] = new Animation(_gl, context, resources, "combo2_effect", 1);
        effectAnimations[5] = new Animation(_gl, context, resources, "combo3_effect", 1);
        effectAnimations[6] = new Animation(_gl, context, resources, "combo4_effect", 1);
        effectAnimations[7] = new Animation(_gl, context, resources, "combo5_effect", 1);
        
        /* Ladataan ilmoitusten grafiikat */
        
        /* Ladataan kartan grafiikat */
        obstacleTextures[0][0] = new Texture(_gl, context, R.drawable.star_tex_0);
        obstacleTextures[1][0] = new Texture(_gl, context, R.drawable.asteroid_tex_0);
        obstacleTextures[2][0] = new Texture(_gl, context, R.drawable.star_tex_0);
    	starBackgroundTexture  = new Texture(_gl, context, R.drawable.bgstar_tex_0);
    	
    	/* Ladataan kerättävien esineiden grafiikat */
    	collectableTextures[0] = new Texture(_gl, context, R.drawable.collectable_tex_0);
        
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
     * Käynnistää pelisäikeen.
     */
    private final void startThread()
    {
        if (gameThread != null) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }
}
