/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package org.matsim.contrib.taxi.scheduler;

import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.StayTaskEndTimeUpdater;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.contrib.taxi.schedule.TaxiTaskType;

import static org.matsim.contrib.dvrp.schedule.ScheduleUpdater.REMOVE_STAY_TASK;

public class TaxiStayTaskEndTimeUpdater implements StayTaskEndTimeUpdater {

	TaxiConfigGroup taxiConfigGroup;

	public TaxiStayTaskEndTimeUpdater(TaxiConfigGroup taxiConfigGroup) {
		this.taxiConfigGroup = taxiConfigGroup;
	}

	@Override
	public double calcNewEndTime(DvrpVehicle vehicle, Task task, double newBeginTime) {
		if(! (task instanceof StayTask) ){
			throw new IllegalArgumentException();
		}
		switch (((TaxiTaskType)task.getTaskType())) {
			case STAY: {
				if (Schedules.getLastTask(vehicle.getSchedule()).equals(task)) {// last task
					// even if endTime=beginTime, do not remove this task!!! A DRT schedule should end with WAIT
					return Math.max(newBeginTime, vehicle.getServiceEndTime());
				} else {
					// if this is not the last task then some other task (e.g. DRIVE or PICKUP)
					// must have been added at time submissionTime <= t
					double oldEndTime = task.getEndTime();
					if (oldEndTime <= newBeginTime) {// may happen if the previous task is delayed
						return REMOVE_STAY_TASK;// remove the task
					} else {
						return oldEndTime;
					}
				}
			}
			case PICKUP: {
				double t0 = ((TaxiPickupTask)task).getRequest().getEarliestStartTime();
				// the actual pickup starts at max(t, t0)
				return Math.max(newBeginTime, t0) + taxiConfigGroup.getPickupDuration();
			}
			case DROPOFF: {
				// cannot be shortened/lengthen, therefore must be moved forward/backward
				return newBeginTime + taxiConfigGroup.getDropoffDuration();
			}

			default:
				throw new IllegalStateException();
		}
	}
}
