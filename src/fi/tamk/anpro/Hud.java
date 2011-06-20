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
    public static final int SPECIAL_1 = 2;
    public static final int SPECIAL_2 = 2;

    /* Painikkeisiin sijoitettujen aseiden tunnukset (viittaa WeaponManagerin
       asetaulukoiden soluihin */
    public int[] weapons;
    
    /* K�ytt�liittym�n objektit */
    public ArrayList<GuiObject> guiObjects = null;

    /* Osoitin WeaponManageriin (HUDin teht�v�n� on muuttaa k�yt�ss� olevaa
       asetta WeaponManagerista) */
    private final WeaponManager weaponManager;
    
    private static Hud pointerToSelf;

    /**
     * Alustaa luokan muuttujat ja kutsuu XmlReaderia.
     */
    public Hud(Context _context)
    {
        weaponManager = WeaponManager.getConnection();
        weapons = new int[5];
        
        guiObjects = new ArrayList<GuiObject>();
        
        XmlReader reader = new XmlReader(_context);
        reader.readHud(this);
    }

    /**
     * P�ivitt�� cooldownit (HUD:ssa n�kyv�t, ei "oikeita" cooldowneja).
     */
    public final void updateCooldowns()
    {
        for(int i = 4; i >= 0; --i) {
            if(weaponManager.cooldownLeft[weapons[i]] > 0 ) {
                guiObjects.get(i).usedTexture = 1;
            }
            else {
                guiObjects.get(i).usedTexture = 0;
            }
        }
    }

    /**
     * K�sittelee napin painalluksen ja asettaa uuden aseen k�ytt��n WeaponManageriin.
     * 
     * @param int Painetun napin tunnus
     */
    public final void triggerClick(int _buttonId)
    {
    	System.exit(0);
    	
        // Tarkistetaan, onko aseessa cooldownia j�ljell� vai ei
        if (weaponManager.cooldownLeft[weapons[_buttonId]] <=0 ) {
            weaponManager.currentWeapon = weapons[_buttonId];
            guiObjects.get(_buttonId).startAnimation(0, 1);
        }
    }
    
    /**
     * Palauttaa osoittimen t�st� luokasta.
     * 
     * @return Hud Osoitin t�h�n luokkaan.
     */
    public final static Hud getConnection()
    {
        return pointerToSelf;
    }
}
