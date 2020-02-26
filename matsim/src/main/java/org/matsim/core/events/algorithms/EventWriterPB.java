package org.matsim.core.events.algorithms;

import org.matsim.api.core.v01.BasicLocation;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.*;
import org.matsim.core.events.handler.BasicEventHandler;
import org.matsim.core.utils.pb.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Event writer for protobuf format according to {@link org.matsim.core.utils.pb.Wireformat}
 */
public class EventWriterPB implements EventWriter, BasicEventHandler {

    private final OutputStream out;


    public EventWriterPB(OutputStream out) {

        this.out = out;
        PBFileHeader header = PBFileHeader.newBuilder()
                .setVersion(PBVersion.EVENTS)
                .setContentType(ContentType.EVENTS)
                .build();

        try {
            header.writeDelimitedTo(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public void closeFile() {
        try {
            out.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void reset(int iteration) {
        // Nothing to do
    }

    @Override
    public void handleEvent(Event event) {
        try {
            convertEvent(event)
                    .writeDelimitedTo(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static ProtoEvents.Event convertEvent(Event event) {

        ProtoEvents.Event.Builder builder = ProtoEvents.Event.newBuilder()
                .setTime(event.getTime());

        if (event instanceof BasicLocation && ((BasicLocation) event).getCoord() != null) {
            builder.getCoordsBuilder()
                    .setX(((BasicLocation) event).getCoord().getX())
                    .setY(((BasicLocation) event).getCoord().getY());
        }

        if (event instanceof GenericEvent) {
            Map<String, String> attrs = event.getAttributes();
            // Checking references is safe here because they are constant
            attrs.keySet().removeIf(key -> key == Event.ATTRIBUTE_X || key == Event.ATTRIBUTE_Y ||
                    key == Event.ATTRIBUTE_TIME || key == Event.ATTRIBUTE_TYPE);

            builder.getGenericBuilder()
                    .setType(event.getEventType())
                    .putAllAttrs(attrs);
        } else if (event instanceof ActivityEndEvent) {
            builder.getActivityEndBuilder()
                    .setLinkId(convertId(((ActivityEndEvent) event).getLinkId()))
                    .setFacilityId(convertId(((ActivityEndEvent) event).getFacilityId()))
                    .setPersonId(convertId(((ActivityEndEvent) event).getPersonId()))
                    .setActtype(((ActivityEndEvent) event).getActType());
        } else if (event instanceof ActivityStartEvent) {
            builder.getActivityStartBuilder()
                    .setLinkId(convertId(((ActivityStartEvent) event).getLinkId()))
                    .setFacilityId(convertId(((ActivityStartEvent) event).getFacilityId()))
                    .setPersonId(convertId(((ActivityStartEvent) event).getPersonId()))
                    .setActtype(((ActivityStartEvent) event).getActType());
        } else if (event instanceof LinkEnterEvent) {
            builder.getLinkEnterBuilder()
                    .setLinkId(convertId(((LinkEnterEvent) event).getLinkId()))
                    .setVehicleId(convertId(((LinkEnterEvent) event).getVehicleId()));
        } else if (event instanceof LinkLeaveEvent) {
            builder.getLinkLeaveBuilder()
                    .setLinkId(convertId(((LinkLeaveEvent) event).getLinkId()))
                    .setVehicleId(convertId(((LinkLeaveEvent) event).getVehicleId()));
        } else if (event instanceof PersonArrivalEvent) {
            builder.getPersonalArrivalBuilder()
                    .setLinkId(convertId(((PersonArrivalEvent) event).getLinkId()))
                    .setLegMode(((PersonArrivalEvent) event).getLegMode())
                    .setPersonId(convertId(((PersonArrivalEvent) event).getPersonId()));
        } else if (event instanceof PersonDepartureEvent) {
            builder.getPersonDepartureBuilder()
                    .setLinkId(convertId(((PersonDepartureEvent) event).getLinkId()))
                    .setLegMode(((PersonDepartureEvent) event).getLegMode())
                    .setPersonId(convertId(((PersonDepartureEvent) event).getPersonId()));
        } else if (event instanceof PersonEntersVehicleEvent) {
            builder.getPersonEntersVehicleBuilder()
                    .setVehicleId(convertId(((PersonEntersVehicleEvent) event).getVehicleId()))
                    .setPersonId(convertId(((PersonEntersVehicleEvent) event).getPersonId()));
        } else if (event instanceof PersonLeavesVehicleEvent) {
            builder.getPersonLeavesVehicleBuilder()
                    .setVehicleId(convertId(((PersonLeavesVehicleEvent) event).getVehicleId()))
                    .setPersonId(convertId(((PersonLeavesVehicleEvent) event).getPersonId()));
        } else if (event instanceof PersonMoneyEvent) {
            builder.getPersonMoneyBuilder()
                    .setPersonId(convertId(((PersonMoneyEvent) event).getPersonId()))
                    .setAmount(((PersonMoneyEvent) event).getAmount())
                    .setPurpose(((PersonMoneyEvent) event).getPurpose())
                    .setTransactionPartner(((PersonMoneyEvent) event).getTransactionPartner());
        } else if (event instanceof PersonStuckEvent) {
            builder.getPersonStuckBuilder()
                    .setLinkId(convertId(((PersonStuckEvent) event).getLinkId()))
                    .setPersonId(convertId(((PersonStuckEvent) event).getPersonId()))
                    .setLegMode(((PersonStuckEvent) event).getLegMode());
        } else if (event instanceof TransitDriverStartsEvent) {
            builder.getTransitDriverStartsBuilder()
                    .setDriverId(convertId(((TransitDriverStartsEvent) event).getDriverId()))
                    .setVehicleId(convertId(((TransitDriverStartsEvent) event).getVehicleId()))
                    .setTransitRouteId(convertId(((TransitDriverStartsEvent) event).getTransitRouteId()))
                    .setTransitLineId(convertId(((TransitDriverStartsEvent) event).getTransitLineId()))
                    .setDepartureId(convertId(((TransitDriverStartsEvent) event).getDepartureId()));
        } else if (event instanceof VehicleAbortsEvent) {
            builder.getVehicleAbortsBuilder()
                    .setVehicleId(convertId(((VehicleAbortsEvent) event).getVehicleId()))
                    .setLinkId(convertId(((VehicleAbortsEvent) event).getLinkId()));
        } else if (event instanceof VehicleEntersTrafficEvent) {
            builder.getVehicleEntersTrafficBuilder()
                    .setDriverId(convertId(((VehicleEntersTrafficEvent) event).getPersonId()))
                    .setLinkId(convertId(((VehicleEntersTrafficEvent) event).getLinkId()))
                    .setVehicleId(convertId(((VehicleEntersTrafficEvent) event).getVehicleId()))
                    .setNetworkMode(((VehicleEntersTrafficEvent) event).getNetworkMode())
                    .setRelativePositionOnLink(((VehicleEntersTrafficEvent) event).getRelativePositionOnLink());
        } else if (event instanceof VehicleLeavesTrafficEvent) {
            builder.getVehicleLeavesTrafficBuilder()
                    .setDriverId(convertId(((VehicleLeavesTrafficEvent) event).getPersonId()))
                    .setLinkId(convertId(((VehicleLeavesTrafficEvent) event).getLinkId()))
                    .setVehicleId(convertId(((VehicleLeavesTrafficEvent) event).getVehicleId()))
                    .setNetworkMode(((VehicleLeavesTrafficEvent) event).getNetworkMode())
                    .setRelativePositionOnLink(((VehicleLeavesTrafficEvent) event).getRelativePositionOnLink());
        } else {
            // TODO: should warn here
            builder.getGenericBuilder()
                    .setType(event.getEventType())
                    .putAllAttrs(event.getAttributes());
        }

        return builder.build();
    }

    /**
     * Convert any id to protobuf equivalent.
     *
     * @return {@link ProtoId} default instance if null
     */
    public static ProtoId convertId(Id<?> id) {

        if (id == null) {
            return ProtoId.getDefaultInstance();
        }

        // TODO: types or indices are not converted yet

        return ProtoId.newBuilder().setId(id.toString()).build();
    }

}
