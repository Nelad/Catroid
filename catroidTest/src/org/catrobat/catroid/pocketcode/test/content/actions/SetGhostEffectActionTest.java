/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.pocketcode.test.content.actions;

import org.catrobat.catroid.pocketcode.content.Sprite;
import org.catrobat.catroid.pocketcode.content.actions.ExtendedActions;
import org.catrobat.catroid.pocketcode.content.actions.SetGhostEffectAction;
import org.catrobat.catroid.pocketcode.formulaeditor.Formula;

import android.test.InstrumentationTestCase;

public class SetGhostEffectActionTest extends InstrumentationTestCase {

	private Formula effectValue = new Formula(50.5f);

	public void testGhostEffect() {
		Sprite sprite = new Sprite("testSprite");
		assertEquals("Unexpected initial sprite scale value", 1f, sprite.look.getAlphaValue());
		SetGhostEffectAction action = ExtendedActions.setGhostEffect(sprite, effectValue);
		action.act(1.0f);
		assertEquals("Incorrect sprite scale value after SetGhostEffectBrick executed",
				(100 - effectValue.interpretFloat(sprite)) / 100, sprite.look.getAlphaValue());
	}

	public void testNullSprite() {
		SetGhostEffectAction action = ExtendedActions.setGhostEffect(null, effectValue);
		try {
			action.act(1.0f);
			fail("Execution of SetGhostEffectBrick with null Sprite did not cause a NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}
}