package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� ammusten yhteiset ominaisuudet.
 * 
 * @extends GameObject
 */
abstract public class AbstractProjectile extends GameObject
{
    /* Ammusten toimintatavat */
    public static final int MULTIPLY_ON_TIMER = 1; // TODO: Totetutus puuttuu
    public static final int MULTIPLY_ON_TOUCH = 2; // TODO: Totetutus puuttuu
    public static final int EXPLODE_ON_TOUCH  = 3;
    public static final int DAMAGE_ON_TOUCH   = 4;
    
    /* Aseen tiedot */
    protected int projectileId;
    
    // Vahinko ja sen tyyppi
    public int damageOnTouch   = 2;
    public int damageOnExplode = 0;
    public int damageType      = DAMAGE_ON_TOUCH; // Arvoksi EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
    
    // R�j�ht�minen kohteen p��ll�
    public boolean explodeOnTarget = false;
    
    // Panssarien l�p�isykyky (lis�� tehty� vahinkoa)
    public int armorPiercing = 0;
    
    // Passiivinen AoE-vahinko
    public boolean causePassiveDamage = false;
    public int     damageOnRadius     = 0;
    public int     damageRadius       = 0;
    
    // R�j�hdyksen ajastus
    private int  explodeTime  = 0;
    private long startTime    = 0;
    private long currentTime  = 0;
    
    // R�j�hdyksen vaikutusalue
    protected int explosionRadius = 0;
    
    /* Muut tarvittavat oliot */
    protected Wrapper wrapper;
    protected AbstractWeapon parent;
    
    /* Kohteen tiedot */
    protected int targetX;
    protected int targetY;
    
    /* Ammuksen tila */
    public    boolean active = false; // Aktiivisuusmuuttuja aseluokkia varten
    protected int     listId;         // Tunnus Wrapperin piirtolistalla
    protected int     userType;		  // Ammuksen k�ytt�j�n tyyppi
    
    /* Ammuksen teko�ly */
    protected AbstractAi ai;

    /**
     * Alustaa luokan muuttujat ja lis�� ammuksen piirtolistalle.
     * 
     * @param int Teko�lyn tyyppi
     * @param int Ammuksen k�ytt�j� (pelaaja, liittolainen tai vihollinen)
     */
    public AbstractProjectile(int _ai, int _userType)
    {
        super(4); // TODO: T�m� pit�isi mieluummin ladata jostain tai ottaa vastaan parametrina.
        
        // Tallenne taan k�ytt�j�n tyyppi
        userType = _userType;
        
        // Otetaan Wrapper k�ytt��n
        wrapper = Wrapper.getInstance();
        
        // Lis�t��n ammus piirtolistalle ja ladataan teko�ly
        if (_ai == AbstractAi.NO_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE, 1);
        	ai = null;
        }
        else if (_ai == AbstractAi.LINEAR_PROJECTILE_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE, 1);
        	ai = new LinearProjectileAi(listId, _userType);
        }
        else if (_ai == AbstractAi.TRACKING_PROJECTILE_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE, 4);
        	ai = new TrackingProjectileAi(listId, _userType);
        }
    }
    
    /**
     * M��ritt�� ammuksen aktiiviseksi.
     */
    @Override
    final public void setActive()
    {
        wrapper.projectileStates.set(listId, Wrapper.FULL_ACTIVITY);
        active = true;
    }
    
    /**
     * M��ritt�� ammuksen ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
    @Override
    final public void setUnactive()
    {
        wrapper.projectileStates.set(listId, Wrapper.INACTIVE);
        active = false;
        
        if (ai != null) {
        	ai.setUnactive();
        }
    }

    /**
     * Piirt�� ammuksen ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0) {
            GLRenderer.projectileAnimations[projectileId][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else {
            GLRenderer.projectileTextures[projectileId][usedTexture].draw(_gl, x, y, direction);
        }
    }
    
    /**
     * Aktivoi ammuksen, eli m��ritt�� sen aloituspisteen, kohteen, suunnan ja lis��
     * ammuksen Wrapperin piirtolistalle. Aktivoi my�s teko�lyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param int     		 Kohteen X-koordinaatti
     * @param int     		 Kohteen Y-koordinaatti
     * @param boolean 	     Onko ammuksen tarkoitus r�j�ht�� kohteessa?
     * @param boolean 	     Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param AbstractWeapon Ammuksen omistava ase
     * @param float          Ammuksen aloituspisteen X-koordinaatti
     * @param float			 Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(int _targetX, int _targetY, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mik�li ammuksen on r�j�hdett�v� tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan is�nt�luokan osoitisn 
        parent = _parent;
        
        // Tallennetaan kohteen koordinaatit
        targetX = _targetX;
        targetY = _targetY;
        
        // Otetaan kohteessa r�j�ht�minen k�ytt��n
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, 1);
        active = true;
        if (ai != null) {
        	ai.setActive(_targetX, _targetY);
        }
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    /**
     * Aktivoi ammuksen, eli m��ritt�� sen aloituspisteen, kohteen, suunnan ja lis��
     * ammuksen Wrapperin piirtolistalle. Aktivoi my�s teko�lyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param int     		 Ammuksen aloitussuunta
     * @param boolean 	     Onko ammuksen tarkoitus r�j�ht�� kohteessa?
     * @param boolean 	     Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param AbstractWeapon Ammuksen omistava ase
     * @param float          Ammuksen aloituspisteen X-koordinaatti
     * @param float			 Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(int _direction, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mik�li ammuksen on r�j�hdett�v� tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan is�nt�luokan osoitisn 
        parent = _parent;
        
        // M��ritet��n aloitussuunta
        direction = _direction;
        
        // Poistetaan kohteessa r�j�ht�minen k�yt�st�
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, Wrapper.FULL_ACTIVITY);
        active = true;
        if (ai != null) {
        	ai.setActive(_direction);
        }
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    /**
     * Aktivoi ammuksen, eli m��ritt�� sen aloituspisteen, kohteen, suunnan ja lis��
     * ammuksen Wrapperin piirtolistalle. Aktivoi my�s teko�lyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param int[][]  		 Ammuksen reitti
     * @param boolean 	     Onko ammuksen tarkoitus r�j�ht�� kohteessa?
     * @param boolean 	     Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param AbstractWeapon Ammuksen omistava ase
     * @param float          Ammuksen aloituspisteen X-koordinaatti
     * @param float			 Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(int[][] _path, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mik�li ammuksen on r�j�hdett�v� tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan is�nt�luokan osoitisn 
        parent = _parent;
        
        // Poistetaan kohteessa r�j�ht�minen k�yt�st�
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, Wrapper.FULL_ACTIVITY);
        active = true;
        ai.setActive(_path);
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    /**
     * K�sittelee ammuksen t�rm�ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistetaan r�j�hdys kohteessa */
    	if (explodeOnTarget) {
    		double distance = Math.sqrt(Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2));
    		
    		if (distance - collisionRadius - 20 <= 0) {
    			setUnactive();
	    	    active = false;
	    		parent.triggerClusterExplosion(8, x, y);
    		}
    	}
    		
        /* Tarkistetaan osumat vihollisiin */
    	if(userType == Wrapper.CLASS_TYPE_PLAYER) {
	        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	            
	            // Tarkistetaan, onko vihollinen aktiivinen
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	            	
	            	// Tarkistetaan, onko ammuksen ja vihollisen v�linen et�isyys riitt�v�n pieni
	            	// tarkkoja osumatarkistuksia varten
	            	if (Math.abs(wrapper.enemies.get(i).x - x) <= Wrapper.gridSize) {
		            	if (Math.abs(wrapper.enemies.get(i).y - y) <= Wrapper.gridSize) {
	                
			                // Tarkistetaan osuma
			        		if (Utility.isColliding(wrapper.enemies.get(i), this)) {
			        			
			        			// Asetetaan tila
			                    wrapper.projectileStates.set(listId, 2);

				                // Aiheutetaan osuma
			                    if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
			                        wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
			
			                        // Aiheutetaan r�j�hdys kohteeessa
			                    	if (explodeOnTarget) {
			                		    setUnactive();
			            	    	    active = false;
			            	    		parent.triggerClusterExplosion(8, x, y);
			                    	}
			                    	
			                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
			                    }
			                    // Aiheutetaan r�j�hdys
			                    else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
			                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
			
			                        triggerExplosion();
			                    }
			                    
			                    break;
			                }
			                
			                // K�sitell��n passiivinen vahinko
			        		if (Utility.isInDamageRadius(this, wrapper.enemies.get(i))) {
			                    wrapper.enemies.get(i).health -= (damageRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
			                }
		            	}
	            	}
	            }
	        }
    	}
    	
    	else if(userType == Wrapper.CLASS_TYPE_ENEMY) {
    		
    		// Tarkistetaan, onko pelaaja aktiivinen
    		if(wrapper.playerState == 1) {
    			
    			// Tarkistetaan, onko ammuksen ja pelaajan v�linen et�isyys riitt�v�n pieni
            	// tarkkoja osumatarkistuksia varten
    			if (Math.abs(wrapper.player.x - x) <= Wrapper.gridSize) {
	            	if (Math.abs(wrapper.player.y - y) <= Wrapper.gridSize) {
	            		
	            		// Lasketaan et�isyys pelaajaan
	            		double distance = Math.sqrt(Math.pow(x - wrapper.player.x, 2) + Math.pow(y - wrapper.player.y, 2));
	            		
	            		if (distance - wrapper.player.collisionRadius - collisionRadius <= 0) {
		                    wrapper.projectileStates.set(listId, 2);
		                    
		                    if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
		                        wrapper.player.triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
		
		                    	if (explodeOnTarget) {
		                		    setUnactive();
		            	    	    active = false;
		            	    		parent.triggerClusterExplosion(8, x, y);
		                    	}
		                    	
		                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
		                    }
		                    else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
		                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
		
		                        triggerExplosion();
		                    }
	            		}
	            		
	            		// K�sitell��n passiivinen vahinko
		                if (distance - wrapper.player.collisionRadius - damageRadius <= 0) {
		                    wrapper.player.health -= (damageOnRadius * (1 - 0.15 * wrapper.player.defence));
		                }
	            	}
    			}
    		}
    	}
        
        /* Tarkistetaan ajastetut r�j�hdykset */
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                wrapper.projectileStates.set(listId, 2);
                setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
                
                triggerExplosion();
            }
        }
        
        /* Tarkistetaan suunta ja k��ntyminen */
        //...
        
        /* K�sitell��n reuna-alueet panosten tuhoamiseksi */
        if (wrapper.player.x + x < -Options.scaledScreenWidth || wrapper.player.x + x > Options.scaledScreenWidth ||
            wrapper.player.y + y < -Options.scaledScreenHeight || wrapper.player.y + y > Options.scaledScreenHeight ) {
            setUnactive();
        }
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.
     * 
     * K�ytet��n esimerkiksi objektin tuhoutuessa. Objektille m��ritet��n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m��ritet��n 2 (piirret��n, mutta
     * p�ivitet��n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T�ll�in GameThread p�ivitt�� objektin animaation, Renderer piirt�� sen, ja kun
     * animaatio p��ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T�ss�
     * funktiossa objekti k�sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m��ritt�� itsens� ep�aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill� toimintoja voi olla
     * useita. Objekteilla on my�s k�yt�ss��n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T�m� arvo kertoo objektille, mink� toiminnon se juuri suoritti.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // Tuhotaan ammus
        if (actionId == 1) {
            setUnactive();
            active = false;
        }
    }
    
    /**
     * Etsii r�j�hdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    private final void triggerExplosion()
    {
        // Tarkistetaan et�isyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size() - 1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1 || wrapper.enemyStates.get(i) == 3) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));

                if (distance - wrapper.enemies.get(i).collisionRadius - explosionRadius <= 0) {
                    // Osuma ja r�j�hdys
                    wrapper.enemies.get(i).triggerImpact(damageOnExplode);
                }
            }
        }
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    protected void triggerSpecialAction() { }
}
