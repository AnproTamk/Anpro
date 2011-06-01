package fi.tamk.anpro;

public class Weapon {
	public WeaponDefault weaponDefault = null;
	
	public int cooldown;
	public int damage;
	public int armorPiercing;
	
	public Weapon(int _cooldown, int _damage, int _armorPiercing){
		cooldown = _cooldown;
		damage = _damage;
		armorPiercing = _armorPiercing;
	}
	
	public void activate() {
		// tarkistaa mikä aseista on alustettu
		if (weaponDefault != null) {
			weaponDefault.activate();
		}
	}
}
