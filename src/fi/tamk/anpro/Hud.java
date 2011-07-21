package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;

/**
 * Hallitsee pelin k�ytt�liittym�� eli HUDia. Ei kuitenkaan tunnista kosketustapahtumia
 * eik� sis�ll� toteutusta aseiden uudelleensijoittamiseen.
 */
public class Hud
{
    /* Painikkeiden tunnukset */
    public static final int BUTTON_1  = 0;
    public static final int BUTTON_2  = 1;
    public static final int BUTTON_3  = 2;
    public static final int SPECIAL_1 = 3;
    public static final int SPECIAL_2 = 4;

    /* Painikkeisiin sijoitettujen aseiden tunnukset (viittaa WeaponManagerin
       asetaulukoiden soluihin */
    public int[] weapons;
    
    /* K�ytt�liittym�n objektit */
    public ArrayList<Button>          buttons                 = null; // TODO: Ei kai kaikkien tarvitsisi olla public?
    public ArrayList<CooldownCounter> cooldownCounter         = null;
    public ArrayList<Icon>	          icons	                  = null;
    public Counter                    scoreCounter            = null;
    public Joystick                   joystick                = null;
    public Bar		                  healthBar               = null;
    public Bar				          armorBar                = null;
    public GuideArrow                 guideArrowToCollectable = null;
    public GuideArrow                 guideArrowToMothership  = null;
    public Radar					  radar_top			      = null;
    public Radar					  radar_left			  = null;
    public Radar					  radar_right			  = null;
    public Radar					  radar_down			  = null;
    
     
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
        // M��ritet��n nappeihin asetetut aseet
        // TODO: Vain 3 nappia!
        weapons = new int[5];
        for (byte i = 0; i < 5; ++i) {
        	weapons[i] = -1;
        }
        weapons[0] = 0; // TODO: Pit�� ladata SkillTreest�
        
        // Alustetaan taulukot
        buttons          = new ArrayList<Button>();
        cooldownCounter	 = new ArrayList<CooldownCounter>();
        icons            = new ArrayList<Icon>();

        /* Luodaan HUD */
        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
        
        guideArrowToCollectable = new GuideArrow(0, 0, GuideArrow.TARGET_COLLECTABLE);
        guideArrowToMothership  = new GuideArrow(0, 0, GuideArrow.TARGET_MOTHERSHIP);
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * P�ivitt�� cooldownit (HUD:ssa n�kyv�t, ei oikeita cooldowneja).
     */
    public final void updateCooldowns()
    {
    	 for (int i = buttons.size()-1; i >= 0; --i) {
            if (weapons[i] > -1) {
	    		if (weaponManager.cooldownLeft[weapons[i]] >= 0) {
	            	cooldownCounter.get(i).update(weaponManager.cooldownLeft[i]);
	            }
            }
        }
    }
    
    /**
     * P�ivitt�� HUD:ssa n�kyv�n pistelaskurin.
     * 
     * @param _score Pisteet
     */
    public final void updateScoreCounter(long _score)
    {
    	scoreCounter.parseScore(_score);
    }

    /**
     * K�sittelee napin painalluksen ja asettaa uuden aseen k�ytt��n WeaponManageriin.
     * 
     * @param _buttonId Painetun napin tunnus
     */
    public final void triggerClick(int _buttonId)
    {
        // Tarkistetaan, onko aseessa cooldownia j�ljell� vai ei
    	if (weapons[_buttonId] > -1) {
	        if (weaponManager.cooldownLeft[weapons[_buttonId]] <= 0) {
	            
	            // Otetaan muut aseet pois k�yt�st�
	            for (Button object : buttons) {
	                object.setSelected(false); // TODO: Poista pelk�st��n valittuna ollut ase k�yt�st�
	            }
	            
	        	// Otetaan uusi ase k�ytt��n
	            weaponManager.setCurrentWeapon(weapons[_buttonId]);
	            icons.get(_buttonId).setState(true);
	            buttons.get(_buttonId).setSelected(true);
	        }
    	}
    }
}
