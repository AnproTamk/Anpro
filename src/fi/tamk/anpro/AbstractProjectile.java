package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää ammusten yhteiset ominaisuudet.
 * 
 * @extends GameObject
 */
abstract public class AbstractProjectile extends AiObject
{
    /* Ammusten toimintatavat */
    public static final int MULTIPLY_ON_TIMER = 1; // TODO: Totetutus puuttuu
    public static final int MULTIPLY_ON_TOUCH = 2; // TODO: Totetutus puuttuu
    public static final int EXPLODE_ON_TOUCH  = 3;
    public static final int DAMAGE_ON_TOUCH   = 4;
    
    /* Aseen tiedot */
    protected int projectileId;
    
    // Vahinko ja sen tyyppi
    protected int damageOnTouch   = 1;
    protected int damageOnExplode = 0;
    protected int damageType      = DAMAGE_ON_TOUCH; // Arvoksi EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
    
    // Räjähtäminen kohteen päällä
    protected boolean explodeOnTarget = false;
    
    // Panssarien läpäisykyky (lisää tehtyä vahinkoa)
    protected int armorPiercing = 0;
    
    // Passiivinen AoE-vahinko
    protected boolean causePassiveDamage = false;
    protected int     damageOnRadius     = 0;
    protected int     damageRadius       = 0;
    
    // Räjähdykset
    protected boolean triggersExplosionEffect = false;
    private int  explodeTime  = 0;
    private long startTime    = 0;
    private long currentTime  = 0;
    
    // Räjähdyksen vaikutusalue
    protected int explosionRadius = 0;
    
    /* Muut tarvittavat oliot */
    protected Wrapper wrapper;
    protected AbstractWeapon parent;
    
    /* Kohteen tiedot */
    protected float targetX;
    protected float targetY;
    
    /* Ammuksen tila */
    public    boolean active = false; // Aktiivisuusmuuttuja aseluokkia varten
    protected int     userType;		  // Ammuksen käyttäjän tyyppi

    /**
     * Alustaa luokan muuttujat ja lisää ammuksen piirtolistalle.
     * 
     * @param _ai Tekoälyn tyyppi
     * @param _userType Ammuksen käyttäjä (pelaaja, liittolainen tai vihollinen)
     */
    public AbstractProjectile(int _ai, int _userType)
    {
        super(15); // TODO: Tämä pitäisi mieluummin ladata jostain tai ottaa vastaan parametrina.
        
        /* Tallennetaan muuttujat */
        userType = _userType;
        
        /* Otetaan tarvittavat luokat käyttöön */
        wrapper = Wrapper.getInstance();

        /* Määritetään objektin tila (piirtolista ja tekoäly) */
        wrapper.addToDrawables(this);
        state = Wrapper.INACTIVE;
        
        if (_ai == AbstractAi.NO_AI) {
        	ai = null;
        }
        else if (_ai == AbstractAi.LINEAR_PROJECTILE_AI) {
        	ai = new LinearProjectileAi(this, _userType);
        }
        else if (_ai == AbstractAi.TRACKING_PROJECTILE_AI) {
        	ai = new TrackingProjectileAi(this, _userType);
        }
    }
    
    /**
     * Määrittää ammuksen aktiiviseksi.
     */
    @Override
    final public void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
        active = true;
    }
    
    /**
     * Määrittää ammuksen epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    @Override
    final public void setUnactive()
    {
        state = Wrapper.INACTIVE;
        active = false;
        
        if (ai != null) {
        	ai.setUnactive();
        }
    }

    /**
     * Piirtää ammuksen ruudulle.
     * 
     * @param _gl OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0) {
            GLRenderer.projectileAnimations[projectileId][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else {
            GLRenderer.projectileTextures[projectileId][usedTexture].draw(_gl, x, y, direction, currentFrame);
        }
    }
    
    /**
     * Aktivoi ammuksen, eli määrittää sen aloituspisteen, kohteen, suunnan ja lisää
     * ammuksen Wrapperin piirtolistalle. Aktivoi myös tekoälyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param _targetX     		 Kohteen X-koordinaatti
     * @param _targetY     		 Kohteen Y-koordinaatti
     * @param _explodeOnTarget   Onko ammuksen tarkoitus räjähtää kohteessa?
     * @param _autoSpecial 	     Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param _parent            Ammuksen omistava ase
     * @param _startX            Ammuksen aloituspisteen X-koordinaatti
     * @param _startY			 Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(float _targetX, float _targetY, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mikäli ammuksen on räjähdettävä tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan isäntäluokan osoitisn 
        parent = _parent;
        
        // Tallennetaan kohteen koordinaatit
        targetX = _targetX;
        targetY = _targetY;
        
        // Otetaan kohteessa räjähtäminen käyttöön
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        state = Wrapper.FULL_ACTIVITY;
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
     * Aktivoi ammuksen, eli määrittää sen aloituspisteen, kohteen, suunnan ja lisää
     * ammuksen Wrapperin piirtolistalle. Aktivoi myös tekoälyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param _direction     	Ammuksen aloitussuunta
     * @param _explodeOnTarget 	Onko ammuksen tarkoitus räjähtää kohteessa?
     * @param _autoSpecial 	    Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param _parent           Ammuksen omistava ase
     * @param _startX           Ammuksen aloituspisteen X-koordinaatti
     * @param _startY    		Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(int _direction, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mikäli ammuksen on räjähdettävä tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan isäntäluokan osoitisn 
        parent = _parent;
        
        // Määritetään aloitussuunta
        direction = _direction;
        
        // Poistetaan kohteessa räjähtäminen käytöstä
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        state = Wrapper.FULL_ACTIVITY;
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
     * Aktivoi ammuksen, eli määrittää sen aloituspisteen, kohteen, suunnan ja lisää
     * ammuksen Wrapperin piirtolistalle. Aktivoi myös tekoälyn ja kutsuu haluttaessa
     * erikoistoimintoa.
     * 
     * @param _path  		     Ammuksen reitti
     * @param _explodeOnTarget	 Onko ammuksen tarkoitus räjähtää kohteessa?
     * @param _autoSpecial 	     Onko ammuksen tarkoitus aktivoida erikoistoiminto heti?
     * @param _parent            Ammuksen omistava ase
     * @param _startX            Ammuksen aloituspisteen X-koordinaatti
     * @param _startY			 Ammuksen aloituspisteen Y-koordinaatti
     */
    public final void activate(int[][] _path, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mikäli ammuksen on räjähdettävä tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan isäntäluokan osoitisn 
        parent = _parent;
        
        // Poistetaan kohteessa räjähtäminen käytöstä
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        state = Wrapper.FULL_ACTIVITY;
        active = true;
        ai.setActive(_path);
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    /**
     * Käsittelee ammuksen törmäystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistetaan räjähdys kohteessa */
    	if (explodeOnTarget) {
    		double distance = Utility.getDistance(x, y, targetX, targetY);
    		
    		if (distance - collisionRadius - 20 <= 0) {
    			setUnactive();
	    	    active = false;
	    		parent.triggerClusterExplosion(8, x, y);
    		}
    	}
    	
    	// TODO: Lyhennä alemmat kolme osiota (yhdistä samankaltaiset toiminnot)
        /* Tarkistetaan osumat vihollisiin */
    	if(userType == Wrapper.CLASS_TYPE_PLAYER || userType == Wrapper.CLASS_TYPE_ALLY) {
	        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	            
	            // Tarkistetaan, onko vihollinen aktiivinen
	            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
	            	
	            	// Tarkistetaan, onko ammuksen ja vihollisen välinen etäisyys riittävän pieni
	            	// tarkkoja osumatarkistuksia varten
	            	if (Math.abs(wrapper.enemies.get(i).x - x) <= Wrapper.gridSize) {
		            	if (Math.abs(wrapper.enemies.get(i).y - y) <= Wrapper.gridSize) {
	                
			                // Tarkistetaan osuma
			        		if (Utility.isColliding(wrapper.enemies.get(i), this)) {
			        			
			        			// Asetetaan tila
			                    state = Wrapper.ONLY_ANIMATION;

				                // Aiheutetaan osuma
			                    if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
			                    	if (userType == Wrapper.CLASS_TYPE_PLAYER) { 
			                    		wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PLAYERPROJECTILE, damageOnTouch, armorPiercing);
			                    	}
			                    	else {
			                    		wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_ALLYPROJECTILE, damageOnTouch, armorPiercing);
			                    	}
			
			                        // Aiheutetaan räjähdys kohteeessa
			                    	if (explodeOnTarget) {
			                		    setUnactive();
			            	    	    active = false;
			            	    		parent.triggerClusterExplosion(8, x, y);
			                    	}
			                    	
			                        triggerDestroyed();
			                    }
			                    // Aiheutetaan räjähdys
			                    else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
	
			                        triggerExplosion();
			                    }
			                    
			                    break;
			                }
			                
			                // Käsitellään passiivinen vahinko
			        		if (causePassiveDamage) {
				        		if (Utility.isInDamageRadius(this, wrapper.enemies.get(i))) {
				                    Utility.checkDamage(wrapper.enemies.get(i), damageOnRadius, armorPiercing);
				                }
			        		}
		            	}
	            	}
	            }
	        }
    	}
    	/* Tarkistetaan osumat pelaajaan ja liittolaisiin */
    	else if(userType == Wrapper.CLASS_TYPE_ENEMY) {
    		
    		// Tarkistetaan osumat pelaajaan
    		if(wrapper.player.state == Wrapper.FULL_ACTIVITY) {
    			
    			// Tarkistetaan, onko ammuksen ja pelaajan välinen etäisyys riittävän pieni
            	// tarkkoja osumatarkistuksia varten
    			if (Math.abs(wrapper.player.x - x) <= Wrapper.gridSize) {
	            	if (Math.abs(wrapper.player.y - y) <= Wrapper.gridSize) {
	            		
	            		// Lasketaan etäisyys pelaajaan
	            		double distance = Utility.getDistance(x, y, wrapper.player.x, wrapper.player.y);
	            		
	            		if (distance - wrapper.player.collisionRadius - collisionRadius <= 0) {
		                    state = Wrapper.ONLY_ANIMATION;
		                    
		                    if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
		                        wrapper.player.triggerCollision(COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
		
		                    	if (explodeOnTarget) {
		                		    setUnactive();
		            	    	    active = false;
		            	    		parent.triggerClusterExplosion(8, x, y);
		                    	}
		                    	
		                    	triggerDestroyed();
		                    }
		                    else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
		                       
		                        triggerExplosion();
		                    }
	            		}
	            		
	            		// Käsitellään passiivinen vahinko
	            		if (causePassiveDamage) {
			                if (distance - wrapper.player.collisionRadius - damageRadius <= 0) {
			                	Utility.checkDamage(wrapper.player, damageOnRadius, armorPiercing);
			                }
	            		}
	            	}
    			}
    		}
    		
    		// Tarkistetaan osumat pelaajaan
	        for (int i = wrapper.allies.size()-1; i >= 0; --i) {
	    		if (wrapper.allies.get(i).state == Wrapper.FULL_ACTIVITY) {
	            	
	            	// Tarkistetaan, onko ammuksen ja vihollisen välinen etäisyys riittävän pieni
	            	// tarkkoja osumatarkistuksia varten
	            	if (Math.abs(wrapper.allies.get(i).x - x) <= Wrapper.gridSize) {
		            	if (Math.abs(wrapper.allies.get(i).y - y) <= Wrapper.gridSize) {
	                
			                // Tarkistetaan osuma
			        		if (Utility.isColliding(wrapper.allies.get(i), this)) {
			        			
			        			// Asetetaan tila
			                    state = Wrapper.ONLY_ANIMATION;
	
				                // Aiheutetaan osuma
			                    if (damageType == DAMAGE_ON_TOUCH) {
			                        wrapper.allies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
			
			                        // Aiheutetaan räjähdys kohteeessa
			                    	if (explodeOnTarget) {
			                		    setUnactive();
			            	    	    active = false;
			            	    		parent.triggerClusterExplosion(8, x, y);
			                    	}
			                    	
			                    	triggerDestroyed();
			                    }
			                    // Aiheutetaan räjähdys
			                    else if (damageType == EXPLODE_ON_TOUCH) {
			                        
			                        triggerExplosion();
			                    }
			                    
			                    break;
			                }
			                
			                // Käsitellään passiivinen vahinko
			        		if (causePassiveDamage) {
				        		if (Utility.isInDamageRadius(this, wrapper.enemies.get(i))) {
				                    Utility.checkDamage(wrapper.enemies.get(i), damageOnRadius, armorPiercing);
				                }
			        		}
		            	}
	            	}
	    		}
	        }
    	}

        /* Tarkistetaan ajastetut räjähdykset */
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();

            if (currentTime - startTime >= explodeTime) {
                state = Wrapper.ONLY_ANIMATION;

                triggerExplosion();
            }
        }

        /* Tarkistetaan suunta ja kääntyminen */
        //...

        /* Käsitellään reuna-alueet panosten tuhoamiseksi */
        if (wrapper.player.x - x < -Options.scaledScreenWidth - 100 || wrapper.player.x - x > Options.scaledScreenWidth + 100 ||
            wrapper.player.y - y < -Options.scaledScreenHeight - 100 || wrapper.player.y - y > Options.scaledScreenHeight + 100 ) {
            setUnactive();
        }
    }

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * Käytetään seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lisäksi voi antaa myös jonkin animaation ruudun järjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siinä ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sisältää myös
     *     <b>GfxObject</b>issa), joka käynnistää animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> päivittää animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siitä aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota käytetään esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsensä
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja käynnistää yllä esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun päätyttyä.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sisällä.
     * 
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // Tuhotaan ammus
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
            active = false;
        }
    }
    
    /**
     * Käsitelee törmäykset.
     * 
     * @param int Törmäystyyppi
     * @param int Vahinko
     * @param int Panssarinläpäisykyky
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_OBSTACLE) {
        	triggerDestroyed();
        }
    }
    
    /**
     * Aiheuttaa objektin tuhoutumisen asettamalla toiminnon (ks. setAction GfxObject-luokasta)
     * ja hidastamalla objektia.
     */
	public void triggerDestroyed()
	{
		if(triggersExplosionEffect) {
			EffectManager.showExplosionEffect(x, y);
		}
		
    	state = Wrapper.ONLY_ANIMATION;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, GfxObject.ACTION_DESTROYED, 0, 0);
	}
    
    /**
     * Etsii räjähdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    public final void triggerExplosion()
    {
    	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1, 0, 0);
    	
        // Tarkistetaan etäisyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size() - 1; i >= 0; --i) {
            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY || wrapper.enemies.get(i).state == Wrapper.ANIMATION_AND_MOVEMENT) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));

                if (distance - wrapper.enemies.get(i).collisionRadius - explosionRadius <= 0) {
                    // Osuma ja räjähdys
                    wrapper.enemies.get(i).triggerImpact(damageOnExplode);
                }
            }
        }
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    public void triggerSpecialAction() { }
}
