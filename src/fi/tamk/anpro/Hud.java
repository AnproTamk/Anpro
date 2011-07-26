package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

/**
 * Hallitsee pelin käyttöliittymää eli HUDia. Ei kuitenkaan tunnista kosketustapahtumia
 * eikä sisällä toteutusta aseiden uudelleensijoittamiseen.
 */
public class Hud
{
    /* Painikkeiden tunnukset */
    public static final int BUTTON_1  = 0;
    public static final int BUTTON_2  = 1;

    /* Kerätyn aseen tunnus (viittaa WeaponManagerin asetaulukkoon) */
    public static int collectedWeapon;
    
    /* Käyttöliittymän objektit */
    public ArrayList<Button> buttons                 = null;
    public CooldownCounter   cooldownCounter         = null;
    public ArrayList<Icon>	 icons	                 = null;
    public Counter           scoreCounter            = null;
    public Joystick          joystick                = null;
    public Bar		         healthBar               = null;
    public Bar				 armorBar                = null;
    public GuideArrow        guideArrowToCollectable = null;
    public GuideArrow        guideArrowToWeapon      = null;
    public Radar			 radar_top			     = null;
    public Radar			 radar_left			     = null;
    public Radar			 radar_right			 = null;
    public Radar			 radar_down			     = null;
    
     
    /* Osoittimet tarvittaviin luokkiin */
    private final WeaponManager weaponManager;

    /**
     * Alustaa luokan muuttujat ja lukee Hudin ulkoasun XmlReaderin avulla.
     * 
     * @param _context       Ohjelman konteksti
     * @param _weaponManager Osoitin WeaponManageriin
     */
    public Hud(Context _context, WeaponManager _weaponManager)
    {
    	/* Tallennetaan muuttujat */
        weaponManager = _weaponManager;
        
        /* Alustetaan muuttujat */
        collectedWeapon = -1;
        
        // Alustetaan taulukot
        buttons = new ArrayList<Button>();
        icons   = new ArrayList<Icon>();

        /* Luodaan HUD */
        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
        
        guideArrowToCollectable = new GuideArrow(0, 0, GuideArrow.TARGET_COLLECTABLE);
        guideArrowToWeapon      = new GuideArrow(0, 0, GuideArrow.TARGET_WEAPON);
        
    	// Otetaan uusi ase käyttöön
        icons.get(BUTTON_1).setState(true);
        buttons.get(BUTTON_1).setSelected(true);
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Päivittää cooldownit (HUD:ssa näkyvät, ei oikeita cooldowneja).
     */
    public final void updateCooldowns()
    {
        if (collectedWeapon > -1) {
    		if (weaponManager.cooldownLeft[collectedWeapon] >= 0) {
            	cooldownCounter.updateCounter(weaponManager.cooldownLeft[collectedWeapon]);
            }
        }
    }
    
    /**
     * Päivittää HUD:ssa näkyvän pistelaskurin.
     * 
     * @param _score Pisteet
     */
    public final void updateScoreCounter(long _score)
    {
    	scoreCounter.parseScore(_score);
    }

    /**
     * Käsittelee napin painalluksen ja asettaa uuden aseen käyttöön WeaponManageriin.
     * 
     * @param _buttonId Painetun napin tunnus
     */
    public final void triggerClick(int _buttonId)
    {
    	if (_buttonId == BUTTON_1) {
            // Otetaan muut aseet pois käytöstä
    		for (Button object : buttons) {
    			object.setSelected(false);
	        }
	        
	    	// Otetaan uusi ase käyttöön
	        weaponManager.setCurrentWeapon(0);
	        icons.get(_buttonId).setState(true);
	        buttons.get(_buttonId).setSelected(true);
    	}
    	else if (_buttonId == BUTTON_2) {
	    	if (collectedWeapon > -1) {
	            // Otetaan muut aseet pois käytöstä
	            for (Button object : buttons) {
	                object.setSelected(false);
	            }
	            
	        	// Otetaan uusi ase käyttöön
	            weaponManager.setCurrentWeapon(collectedWeapon);
	            icons.get(_buttonId).setState(true);
	            buttons.get(_buttonId).setSelected(true);
	        }
    	}
    }
    
    public final void setCollectedWeapon(int _weaponType)
    {
    	collectedWeapon = _weaponType;
    	
        for (Button object : buttons) {
            object.setSelected(false);
        }
        icons.get(BUTTON_2).setState(true);
        buttons.get(BUTTON_2).setSelected(true);
        
        weaponManager.setCurrentWeapon(collectedWeapon);
    }
}
