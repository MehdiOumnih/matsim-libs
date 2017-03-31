/* *********************************************************************** *
 * project: org.matsim.*
 * PlanLinkIdentifierUtils.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
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
package org.matsim.contrib.socnetsim.usage.replanning;

import org.matsim.contrib.socnetsim.usage.PlanLinkConfigGroup;
import org.matsim.contrib.socnetsim.framework.population.SocialNetwork;
import org.matsim.contrib.socnetsim.framework.replanning.CompositePlanLinkIdentifier;
import org.matsim.contrib.socnetsim.framework.replanning.SocialNetworkPlanLinkIdentifier;
import org.matsim.contrib.socnetsim.framework.replanning.modules.PlanLinkIdentifier;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.matsim.contrib.socnetsim.jointactivities.replanning.JoinableActivitiesPlanLinkIdentifier;
import org.matsim.contrib.socnetsim.jointtrips.replanning.JointTripsPlanLinkIdentifier;
import org.matsim.contrib.socnetsim.sharedvehicles.replanning.VehicularPlanBasedIdentifier;

/**
 * @author thibautd
 */
public class PlanLinkIdentifierUtils {
	private PlanLinkIdentifierUtils() {}

	public static class LinkIdentifierProvider implements Provider<PlanLinkIdentifier> {
		private final PlanLinkConfigGroup configGroup;
		private final SocialNetwork socialNetwork;

		@Inject
		public LinkIdentifierProvider(
				final PlanLinkConfigGroup configGroup,
				final SocialNetwork socialNetwork ) {
			this.configGroup = configGroup;
			this.socialNetwork = socialNetwork;
		}

		@Override
		public PlanLinkIdentifier get() {
			final CompositePlanLinkIdentifier id = new CompositePlanLinkIdentifier();

			id.addAndComponent( new SocialNetworkPlanLinkIdentifier( socialNetwork ) );

			if ( configGroup.getLinkJointTrips().isWeak() ) {
				id.addOrComponent( new JointTripsPlanLinkIdentifier() );
			}

			if ( configGroup.getLinkVehicles().isWeak() ) {
				id.addOrComponent( new VehicularPlanBasedIdentifier() );
			}

			if ( configGroup.getLinkJoinableActivities().isWeak() ) {
				for ( String activityType : configGroup.getJoinableTypes() ) {
					id.addOrComponent( new JoinableActivitiesPlanLinkIdentifier( activityType ) );
				}
			}

			return id;
		}
	}

	public static class WeakLinkIdentifierProvider implements Provider<PlanLinkIdentifier> {
		private final PlanLinkConfigGroup configGroup;
		private final SocialNetwork socialNetwork;

		@Inject
		public WeakLinkIdentifierProvider(
				final PlanLinkConfigGroup configGroup,
				final SocialNetwork socialNetwork ) {
			this.configGroup = configGroup;
			this.socialNetwork = socialNetwork;
		}

		@Override
		public PlanLinkIdentifier get() {
			final CompositePlanLinkIdentifier id =
				new CompositePlanLinkIdentifier();

			id.addAndComponent( new SocialNetworkPlanLinkIdentifier( socialNetwork ) );

			if ( configGroup.getLinkJointTrips().isStrong() ) {
				id.addOrComponent( new JointTripsPlanLinkIdentifier() );
			}

			if ( configGroup.getLinkVehicles().isStrong() ) {
				id.addOrComponent( new VehicularPlanBasedIdentifier() );
			}

			if ( configGroup.getLinkJoinableActivities().isStrong() ) {
				for ( String activityType : configGroup.getJoinableTypes() ) {
					id.addOrComponent( new JoinableActivitiesPlanLinkIdentifier( activityType ) );
				}
			}

			return id;
		}
	}

}

