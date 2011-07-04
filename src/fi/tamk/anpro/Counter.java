package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ pistelaskurin toiminnallisuudet.
 */
public class Counter extends GuiObject
{
	// Laskurin laskemat pistearvot (1000, 100, 10 tai 1)
	private int value;

	/**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     * @param int Laskettavat arvot
     */
	public Counter(int _x, int _y, int _value) {
		super(_x, _y);
		
		value = _value;
		
		usedTexture = GLRenderer.TEXTURE_COUNTER;
		wrapper.guiObjectStates.set(listId, Wrapper.FULL_ACTIVITY);
	}
	
	/**
	 * Palastelee pisteet yksitt‰isiksi numeroiksi, joiden avulla
	 * piirret‰‰n yksitt‰iset numerot ruudulle pisteiden muodostamiseksi.
	 * 
	 * @param int Pisteet
	 */
	public void parseScore(long _score)
	{
		if (_score >= value) {
			int temp = 0;
			int index = 0;
			wrapper.guiObjectStates.set(listId, 1);
			
			// Tehd‰‰n vertailut, mille pistelaskuriobjektille arvo lasketaan.
			if (value == 1000) {
				index = (int) (_score / value);
			}
			else if (value == 100) {
				if (_score < 1000) {
					index = (int) (_score / value);
				}
				else {
					temp = (int)_score - (int) ((_score / 1000) * 1000);
					index = temp / value;
				}
			}
			else if (value == 10) {
				if (_score >= 100) {
					temp = (int) (_score / 100);
					index = (int) (_score - (value * 10 * temp));
					if (index >= value) {
						index /= value;
					}
					else {
						index = 0;
					}
				}
				else {
					index = (int) (_score / value);
				}
			}
			else {
				if (_score >= 10 && _score < 100) {
					temp = (int) (_score / 10);
					index = (int) (_score - (10 * temp));
				}
				else if (_score >= 100 && _score < 1000) {
					//int temp = (int) (_score / 100);
					index = (int) (_score - 100);
					if (index >= 10) {
						temp = index / 10;
						index = index - (10 * temp);
					}
				}
				else if (_score >= 1000) {
					temp = (int) (_score / 1000);
					temp = (int) (_score - 1000 * temp);
					
					temp = temp - ((temp / 100) * 100);
					index = temp - ((temp / 10) * 10);
				}
				else {
					index = (int) (_score / value);
				}
				
			}
			
			// M‰‰ritet‰‰n halutun kuvan tunnus ja laitetaan se muuttujaan.
			usedTexture = GLRenderer.TEXTURE_COUNTER + index;
		}
		else {
			wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
		}
	}

}
