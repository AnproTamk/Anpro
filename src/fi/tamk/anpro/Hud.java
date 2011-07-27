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
        
        buttons.get(0).setSelected(true);
        icons.get(0).setState(true);
        
        icons.get(1).state = Wrapper.INACTIVE;
        
        cooldownCounter.state = Wrapper.INACTIVE;
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
	        weaponManager.setCurrentWeapon(0);
	        
    		buttons.get(0).setSelected(true);
	        icons.get(0).setState(true);
	        
    		buttons.get(1).setSelected(false);
	        icons.get(1).setState(false);
	        
	    	// Otetaan uusi ase käyttöön
    	}
    	else if (_buttonId == BUTTON_2) {
	    	if (collectedWeapon > -1) {
		        weaponManager.setCurrentWeapon(collectedWeapon);
		        
	    		buttons.get(0).setSelected(false);
		        icons.get(0).setState(false);
		        
	    		buttons.get(1).setSelected(true);
		        icons.get(1).setState(true);
	        }
    	}
    }
    
    public final void setCollectedWeapon(int _weaponType)
    {
    	collectedWeapon = _weaponType;
    	
        weaponManager.setCurrentWeapon(collectedWeapon);
        
		buttons.get(0).setSelected(false);
        icons.get(0).setState(false);

		buttons.get(1).setSelected(true);
        icons.get(1).setState(true);
        icons.get(1).state = Wrapper.FULL_ACTIVITY;
    }
}
