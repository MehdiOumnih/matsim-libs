/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2015 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.johannes.gsv.popsim;

import playground.johannes.gsv.synPop.sim3.SamplerListener;
import playground.johannes.synpop.data.PlainPerson;

import java.util.Collection;

/**
 * @author johannes
 *
 */
public class DistanceVectorLogger implements SamplerListener {

	/* (non-Javadoc)
	 * @see playground.johannes.gsv.synPop.sim3.SamplerListener#afterStep(java.util.Collection, java.util.Collection, boolean)
	 */
	@Override
	public void afterStep(Collection<PlainPerson> population, Collection<PlainPerson> mutations, boolean accepted) {
		// TODO Auto-generated method stub

	}

}
