package fi.tamk.anpro;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.DisplayMetrics;
import android.util.Log;

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
    public static final int ANIMATION_RESPAWN  = 5;
    
    public static final int ANIMATION_COLLECTED = 0;

    /* Vakiot HUDin tekstuureille ja animaatioille */
    public static final int TEXTURE_BUTTON_NOTSELECTED = 0;
    public static final int TEXTURE_BUTTON_SELECTED    = 1;
    public static final int TEXTURE_JOYSTICK           = 2;
    public static final int TEXTURE_HEALTH             = 3;
    public static final int TEXTURE_COOLDOWN           = 14;
    public static final int TEXTURE_COUNTER			   = 24;
    public static final int TEXTURE_ARMOR			   = 34;
    public static final int TEXTURE_MISSILE			   = 45;
    public static final int TEXTURE_GUIDEARROW		   = 47;
    public static final int TEXTURE_RADAR			   = 49;
    
    public static final int ANIMATION_MESSAGE = 2;
    
    public static final int ANIMATION_CLICK         = 0;
    public static final int ANIMATION_RADAR_WARNING = 1;
    
    /* Animaatioiden ja tekstuurien määrät */
    public static final int AMOUNT_OF_PLAYER_TEXTURES        = 5;
    public static final int AMOUNT_OF_ALLY_TEXTURES          = 1;
    public static final int AMOUNT_OF_ENEMY_TEXTURES         = 4;
    public static final int AMOUNT_OF_PROJECTILE_TEXTURES    = 4;
    public static final int AMOUNT_OF_HUD_TEXTURES           = 50;
    public static final int AMOUNT_OF_OBSTACLE_TEXTURES      = 3;
    public static final int AMOUNT_OF_COLLECTABLE_TEXTURES   = 2;
    public static final int AMOUNT_OF_MOTHERSHIP_TEXTURES    = 1;
    
    public static final int AMOUNT_OF_PLAYER_ANIMATIONS      = 6;
    public static final int AMOUNT_OF_ALLY_ANIMATIONS        = 4;
    public static final int AMOUNT_OF_ENEMY_ANIMATIONS       = 5;
    public static final int AMOUNT_OF_PROJECTILE_ANIMATIONS  = 5;
    public static final int AMOUNT_OF_HUD_ANIMATIONS         = 8;
    public static final int AMOUNT_OF_EFFECT_ANIMATIONS      = 12;
    public static final int AMOUNT_OF_OBSTACLE_ANIMATIONS    = 0;
    public static final int AMOUNT_OF_COLLECTABLE_ANIMATIONS = 2;
    public static final int AMOUNT_OF_MOTHERSHIP_ANIMATIONS  = 0;
    
    /* Lataus- ja tarinaruutujen tekstuurit */
    private GLSpriteSet loadingTexture;
    private GLSpriteSet storyTexture;
   
    /* Piirrettävät animaatiot ja objektit */
    public static GLSpriteSet[]   playerTextures;
    public static GLSpriteSet[]   playerAnimations;
    public static GLSpriteSet[][] allyTextures;
    public static GLSpriteSet[][] allyAnimations;
    public static GLSpriteSet[][] enemyTextures;
    public static GLSpriteSet[][] enemyAnimations;
    public static GLSpriteSet[][] projectileTextures;
    public static GLSpriteSet[][] projectileAnimations;
    public static GLSpriteSet[]   mothershipTextures;
    public static GLSpriteSet[]   mothershipAnimations;
    
    public static GLSpriteSet[]   hudTextures;
    public static GLSpriteSet[]   hudAnimations;
    
    public static GLSpriteSet[]   effectAnimations;
    
    public static GLSpriteSet[][] obstacleTextures;
    public static GLSpriteSet[][] obstacleAnimations;
    
    public static GLSpriteSet[]   collectableTextures;
    public static GLSpriteSet[]   collectableAnimations;
    
    public static GLSpriteSet     starBackgroundTexture;
    
    public static GLSpriteSet	  boundaryTexture;
    
    /* Ohjelman konteksti */
    private Context context;
    
    /* Tarvittavat oliot */
    private Wrapper    wrapper;
    private GameThread gameThread = null;
    
    /* Lataustiedot (kertoo, onko tekstuureja vielä ladattu) */
    public        boolean loadingStarted = false;
    public static boolean loadingFailed  = false;
    public        boolean allLoaded      = false;
    
    /* Animaatiopäivitysten muuttujat */
    private long    lastAnimationUpdate;
    private int     updateBeat = 1;
    private boolean updateAnimations = true;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context        Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param Resources      Ohjelman resurssit
     * @param DisplayMetrics Näytön tiedot
     */
    public GLRenderer(Context _context, GLSurfaceView _surface, DisplayMetrics _dm)
    {
        // Määritetään taulukoiden koot
        playerTextures        = new GLSpriteSet[AMOUNT_OF_PLAYER_TEXTURES];
        playerAnimations      = new GLSpriteSet[AMOUNT_OF_PLAYER_ANIMATIONS];
        allyTextures          = new GLSpriteSet[2][AMOUNT_OF_ALLY_TEXTURES];
        allyAnimations        = new GLSpriteSet[2][AMOUNT_OF_ALLY_ANIMATIONS];
        enemyTextures         = new GLSpriteSet[5][AMOUNT_OF_ENEMY_TEXTURES];
        enemyAnimations       = new GLSpriteSet[5][AMOUNT_OF_ENEMY_ANIMATIONS];
        projectileTextures    = new GLSpriteSet[6][AMOUNT_OF_PROJECTILE_TEXTURES];
        projectileAnimations  = new GLSpriteSet[6][AMOUNT_OF_PROJECTILE_ANIMATIONS];
        mothershipTextures    = new GLSpriteSet[AMOUNT_OF_MOTHERSHIP_TEXTURES];
        mothershipAnimations  = new GLSpriteSet[AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        hudTextures           = new GLSpriteSet[AMOUNT_OF_HUD_TEXTURES];
        hudAnimations         = new GLSpriteSet[AMOUNT_OF_HUD_ANIMATIONS];
        effectAnimations      = new GLSpriteSet[AMOUNT_OF_EFFECT_ANIMATIONS];
        obstacleTextures      = new GLSpriteSet[3][AMOUNT_OF_OBSTACLE_TEXTURES];
        obstacleAnimations    = new GLSpriteSet[3][AMOUNT_OF_OBSTACLE_ANIMATIONS];
        collectableTextures   = new GLSpriteSet[AMOUNT_OF_COLLECTABLE_TEXTURES];
        collectableAnimations = new GLSpriteSet[AMOUNT_OF_COLLECTABLE_ANIMATIONS];
        
        // Tallennetaan konteksti
        context   = _context;
        
        // Otetaan Wrapper käyttöön
        wrapper = Wrapper.getInstance();
    }

    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
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
        _gl.glShadeModel(GL10.GL_FLAT); // GL_FLAT tai GL_SMOOTH

        // Piirretään tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Määritetään läpinäkyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Määritetään blendausasetukset
        _gl.glEnable(GL10.GL_BLEND);
        _gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        // Määritetään syvyysasetukset
        _gl.glEnable(GL10.GL_DEPTH_TEST);
        _gl.glClearDepthf(1.0f);
        
        // Otetaan 2D-piirtäminen käyttöön
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        
        // Avataan tekstuuri- ja vektoritaulukot käyttöön
        _gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
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
    }

    /**
     * Käy läpi piirtolistat ja piirtää tarvittavat tekstuurit ruudulle.
     * Android kutsuu tätä automaattisesti (maks. 60 kertaa sekunnissa).
     * 
     * @param _gl OpenGL-konteksti
     */
    public void onDrawFrame(GL10 _gl)
    {
    	if (gameThread != null) {
	        // Tyhjätään ruutu ja syvyyspuskuri
    		_gl.glClearColor(0.0f, 0.0f, 0.0f, 0);
	        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        
	        if (gameThread.gameState == GameThread.GAMESTATE_LOADING_RESOURCES) {
	        	if (loadingTexture == null) {
	                // Ladataan latausruudun ja tarinan tekstuurit
	            	loadingTexture = new GLSpriteSet(_gl, context, R.drawable.loading, 1, false);
	            	storyTexture   = new GLSpriteSet(_gl, context, R.drawable.story1, 1, false);
	        	}
	            loadingTexture.draw(_gl, 0, 0, 90, 0);
	            
	            if (!loadingStarted && !allLoaded) {
	            	loadingStarted = true;
	            }
	            else if (loadingStarted && !allLoaded) {
	            	if (loadTextures(_gl)) {
	            		allLoaded = true;
	            	}
	            	else {
	            		System.exit(0);
	            	}
	            }
	        }
	        else if (gameThread.gameState == GameThread.GAMESTATE_STORY) {
	            storyTexture.draw(_gl, 0, 0, 90, 0);
	        }
	        else if (gameThread.gameState == GameThread.GAMESTATE_GAME) {
		        /* Tarkastetaan onko tekstuurit ladattu */
		        if (allLoaded && gameThread.allLoaded) {
		        	renderScene(_gl);
		        }
	        }
    	}
    }
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */        
    /**
     * Yhdistää renderöijän pelisäikeeseen tallentamalla pelisäikeen pointterin muistiin.
     * 
     * @param _gameThread Osoitin pelisäikeeseen
     */
    public final void connectToGameThread(GameThread _gameThread)
    {
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
        playerTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.player_tex_0, 1, false); 
        playerAnimations[3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        playerAnimations[5] = new GLSpriteSet(_gl, context, R.drawable.player_respawn_anim, 4, false);
        
        /* Ladataan liittolaisten grafiikat */
        // Emoalus
        mothershipTextures[0] = new GLSpriteSet(_gl, context, R.drawable.mothership_tex_0, 1, false);
        
        // Liittolainen #1
        allyTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.allyturret_tex_0, 1, false);
        allyAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1, false);

        /* Ladataan vihollisten grafiikat */
        // Vihollinen #1
        enemyTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy1_tex_0, 1, false);
        enemyAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        enemyAnimations[0][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20, false);
        
        // Vihollinen #2
        enemyTextures[1][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy2_tex_0, 1, false);
        enemyAnimations[1][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        enemyAnimations[1][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20, false);
        
        // Vihollinen #3
        enemyTextures[2][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy3_tex_0, 1, false);
        enemyAnimations[2][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        enemyAnimations[2][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20, false);
        
        // Vihollinen #4
        enemyTextures[3][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy4_tex_0, 1, false);
        enemyAnimations[3][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        enemyAnimations[3][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20, false);

        // Vihollinen #4
        enemyTextures[4][0]   = new GLSpriteSet(_gl, context, R.drawable.enemy5_tex_0, 1, false);
        enemyAnimations[4][3] = new GLSpriteSet(_gl, context, R.drawable.enemy1_destroy_anim, 20, false);
        enemyAnimations[4][4] = new GLSpriteSet(_gl, context, R.drawable.enemy1_disabled_anim, 20, false);

        /* Ladataan ammusten grafiikat */
        // Vakioase
        projectileTextures[0][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilelaser_tex_0, 1, false);
        projectileAnimations[0][3] = new GLSpriteSet(_gl, context, R.drawable.projectilelaser_destroy_anim, 5, false);

        // EMP
        projectileTextures[1][0]   = new GLSpriteSet(_gl, context, R.drawable.projectileemp_destroy_anim, 1, false);
        projectileAnimations[1][3] = new GLSpriteSet(_gl, context, R.drawable.projectileemp_destroy_anim, 10, false);

        // Pyörivä laser
        projectileTextures[2][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilespinninglaser_destroy_anim, 1, false);
        projectileAnimations[2][3] = new GLSpriteSet(_gl, context, R.drawable.projectilespinninglaser_destroy_anim, 10, false);

        // Bomb
        projectileTextures[3][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1, false);
        projectileAnimations[3][3] = new GLSpriteSet(_gl, context, R.drawable.projectilebomb_destroy_anim, 1, false);

        // Missile
        projectileTextures[4][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilemissile_tex_0, 1, false);
        projectileAnimations[4][3] = new GLSpriteSet(_gl, context, R.drawable.projectilemissile_destroy_anim, 1, false);

        // Spitfire
        projectileTextures[5][0]   = new GLSpriteSet(_gl, context, R.drawable.projectilespitfire_tex_0, 1, false);
        projectileAnimations[5][3] = new GLSpriteSet(_gl, context, R.drawable.projectilemissile_destroy_anim, 1, false);

        /* Ladataan käyttöliittymän grafiikat */
        // Napit
        hudTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.button_tex_0, 1, false);
        hudTextures[1]   = new GLSpriteSet(_gl, context, R.drawable.button_tex_1, 1, false);
        
        hudAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.button_clicked_anim, 9, false);

        // Joystick
        hudTextures[2]  = new GLSpriteSet(_gl, context, R.drawable.joystick_tex_0, 1, false);

        // Elämäpalkki
        hudTextures[3]  = new GLSpriteSet(_gl, context, R.drawable.healthbar_texs, 11, false);

        // Cooldown-tekstuurit
        hudTextures[14] = new GLSpriteSet(_gl, context, R.drawable.cooldown_texs, 10, false);

        //Counter-tekstuurit
        hudTextures[24] = new GLSpriteSet(_gl, context, R.drawable.counter_texs, 10, false);

        // Armorpalkki
        hudTextures[34] = new GLSpriteSet(_gl, context, R.drawable.armorbar_texs, 11, false);
        
        // Aseet
        hudTextures[45] = new GLSpriteSet(_gl, context, R.drawable.missile_tex_0, 1, false);
        hudTextures[46] = new GLSpriteSet(_gl, context, R.drawable.missile_tex_1, 1, false);
        
        // Kohteen osoittavat nuolet
        hudTextures[47] = new GLSpriteSet(_gl, context, R.drawable.collectablearrow_tex_0, 1, false);
        hudTextures[48] = new GLSpriteSet(_gl, context, R.drawable.mothershiparrow_tex_0, 1, false);
        
        // Tutka
        hudTextures[49]  = new GLSpriteSet(_gl, context, R.drawable.radar_tex_0, 1, false);
        hudAnimations[1] = new GLSpriteSet(_gl, context, R.drawable.radar_warning_anim, 3, false);
        
        // Ilmoitukset
        hudAnimations[2] = new GLSpriteSet(_gl, context, R.drawable.outofboundsmessage_left_anim, 9, false);
        hudAnimations[3] = new GLSpriteSet(_gl, context, R.drawable.outofboundsmessage_right_anim, 9, false);
        hudAnimations[4] = new GLSpriteSet(_gl, context, R.drawable.armorsoffmessage_left_anim, 9, false);
        hudAnimations[5] = new GLSpriteSet(_gl, context, R.drawable.armorsoffmessage_right_anim, 9, false);
        hudAnimations[6] = new GLSpriteSet(_gl, context, R.drawable.enemybattleshipnearbymessage_left_anim, 9, false);
        hudAnimations[7] = new GLSpriteSet(_gl, context, R.drawable.enemybattleshipnearbymessage_right_anim, 9, false);
        
        /* Ladataan efektien grafiikat */
        // Huutomerkki
        effectAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.exclamationmark_effect_anim, 1, false);
        
        // Kysymysmerkki
        effectAnimations[1] = new GLSpriteSet(_gl, context, R.drawable.questionmark_effect_anim, 1, false);
        
        // Armor-suoja
        effectAnimations[2] = new GLSpriteSet(_gl, context, R.drawable.armor_effect_anim, 9, false);
        
        // Combomultiplier
        effectAnimations[3] = new GLSpriteSet(_gl, context, R.drawable.combo2_effect_anim, 6, false);
        effectAnimations[4] = new GLSpriteSet(_gl, context, R.drawable.combo3_effect_anim, 6, false);
        effectAnimations[5] = new GLSpriteSet(_gl, context, R.drawable.combo4_effect_anim, 6, false);
        effectAnimations[6] = new GLSpriteSet(_gl, context, R.drawable.combo5_effect_anim, 6, false);
        
        // Jälkipoltto
        effectAnimations[8]  = new GLSpriteSet(_gl, context, R.drawable.playertrail_effect_anim, 1, false);
        effectAnimations[11] = new GLSpriteSet(_gl, context, R.drawable.enemytrail_effect_anim, 1, false);
        
        // Armor-kilven välähdys
        effectAnimations[9] = new GLSpriteSet(_gl, context, R.drawable.armor_hit_effect_anim, 3, false);
        
        // Health-sydämen välähdys
        effectAnimations[10] = new GLSpriteSet(_gl, context, R.drawable.health_hit_effect_anim, 3, false);
        
        // Räjähdys
        effectAnimations[7] = new GLSpriteSet(_gl, context, R.drawable.explosion_effect_anim, 5, false);
        
        /* Ladataan kartan grafiikat */
        obstacleTextures[0][0] = new GLSpriteSet(_gl, context, R.drawable.planetearth_tex_0, 1, false);
        obstacleTextures[0][1] = new GLSpriteSet(_gl, context, R.drawable.planet_tex_0, 1, false);
        obstacleTextures[1][0] = new GLSpriteSet(_gl, context, R.drawable.asteroid_tex_0, 1, false);
        obstacleTextures[2][0] = new GLSpriteSet(_gl, context, R.drawable.star_tex_0, 1, false);
    	starBackgroundTexture  = new GLSpriteSet(_gl, context, R.drawable.bgstar_tex_0, 1, false);
    	
    	/* Ladataan kerättävien esineiden grafiikat */
    	collectableTextures[0]   = new GLSpriteSet(_gl, context, R.drawable.collectable_tex_0, 1, false);
    	collectableAnimations[0] = new GLSpriteSet(_gl, context, R.drawable.collectable_collected_anim, 12, false);
    	collectableTextures[1]   = new GLSpriteSet(_gl, context, R.drawable.particle, 1, false);
    	collectableAnimations[1] = new GLSpriteSet(_gl, context, R.drawable.collectable_collected_anim, 12, false);
    	
    	/* Kentän rajan tekstuuri */
    	boundaryTexture = new GLSpriteSet(_gl, context, R.drawable.boundary_tex_0, 1, true);

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
     * Piirtää pelitilan objektit.
     * 
     * @param _gl OpenGL-konteksti
     */
    private void renderScene(GL10 _gl)
    {
    	long currentTime = android.os.SystemClock.uptimeMillis();
        
        if (currentTime - lastAnimationUpdate >= 40) {
            lastAnimationUpdate = currentTime;
            updateAnimations = true;
        }
        
        for (GfxObject object : wrapper.drawables) {
        	if (object.state == Wrapper.FULL_ACTIVITY || object.state == Wrapper.ONLY_ANIMATION ||
        		object.state == Wrapper.ANIMATION_AND_MOVEMENT) {
        		object.draw(_gl);
        	}
        	
        	if (updateAnimations && object.usedAnimation > -1 && object.state != Wrapper.INACTIVE ||
            	object.state == Wrapper.ANIMATION_AND_MOVEMENT) {
        		object.updateAnimation();
        	}
        }
        
        updateAnimations = false;
            
        // Kasvatetaan updateBeat:ia ja aloitetaan kierros alusta, mikäli raja ylitetään.
        // Tällä animaatioiden päivittäminen tahdistetaan; Animaatiot voivat näkyä joko
        // joka kierroksella, joka toisella, joka neljännellä tai joka kahdeksannella
        // kierroksella.
        ++updateBeat;
        
        if (updateBeat > 32) {
            updateBeat = 1;
        }
    }
}
