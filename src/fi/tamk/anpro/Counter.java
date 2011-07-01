package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ pelaajan pisteiden laskemiseen tarvittavat toimenpiteet.
 * 
 * @extends GuiObject
 */
public class Counter extends GuiObject
{
	// Luodaan taulukko numeroarvoille
	//private int scoreArray[] = null;
	
	private int 	 value;
	private int 	 temp;

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
		
		//scoreArray = new int[4];
		
		usedTexture = GLRenderer.TEXTURE_COUNTER;
		wrapper.guiObjectStates.set(listId, 1);
	}
	
	/**
	 * Palastellaan pisteet yksitt‰isiksi numeroiksi, joiden avulla
	 * piirret‰‰n yksitt‰iset numerot ruudulle pisteiden muodostamiseksi.
	 * 
	 * @param _score
	 */
	public void parseScore(long _score)
	{
		if (_score >= value) {
			int index;
			wrapper.guiObjectStates.set(listId, 1);
			
			// Tehd‰‰n vertailut, mille pistelaskuriobjektille arvo lasketaan.
			if (value == 1000) {
				index = (int) (_score / value);
				//usedTexture = GLRenderer.TEXTURE_COUNTER + index;
				//tempScore = (int)(_score - value * index);
			}
			else if (value == 100 && _score < 1000) {
				index = (int) (_score / value);
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
				int multiplier;
				if (_score >= 10 && _score < 100) {
					multiplier = (int) (_score / 10);
					index = (int) (_score - (10 * multiplier));
				}
				else if (_score >= 100 && _score < 1000) {
					//int multiplier = (int) (_score / 100);
					index = (int) (_score - 100);
					if (index >= 10) {
						multiplier = index / 10;
						index = index - (10 * multiplier);
					}
				}
				else if (_score >= 1000) {
					multiplier = (int) (_score / 1000);
					index = (int) (_score - (1000 * multiplier));
				}
				else {
					index = (int) (_score / value);
				}
				
			}
			
			// M‰‰ritet‰‰n halutun kuvan tunnus ja laitetaan se muuttujaan.
			usedTexture = GLRenderer.TEXTURE_COUNTER + (int)index;
		}
		else {
			wrapper.guiObjectStates.set(listId, 0);
		}
	}

}
