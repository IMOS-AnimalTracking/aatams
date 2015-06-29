package au.org.emii.aatams

import org.hibernatespatial.GeometryUserType
import org.joda.time.*
import org.joda.time.contrib.hibernate.*

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point


/**
 * Receiver deployment is the process of deploying a receiver in the ocean. The
 * receiver is attached to a mooring and sits under the surface at a pre-defined
 * depth.  It has an additional device attached that's used to release the
 * receiver from its main connection to the mooring so it can float to the
 * surface.  A separate tether keeps the receiver attached to the mooring.
 */
class ReceiverDeployment
{
    static belongsTo = [station: InstallationStation, receiver: Receiver]
    static transients = [ 'scrambledLocation', 'active', 'latitude', 'longitude', 'deploymentInterval', 'deploymentNumber' ]
    static auditable = true

    static hasMany = [ events: ValidReceiverEvent ]

    DateTime initialisationDateTime

    DateTime deploymentDateTime = new DateTime(Person.defaultTimeZone())

    static mapping = {
        initialisationDateTime type: PersistentDateTimeTZ,
        {
            column name: "initialisationDateTime_timestamp"
            column name: "initialisationDateTime_zone"
        }

        deploymentDateTime type: PersistentDateTimeTZ,
        {
            column name: "deploymentDateTime_timestamp"
            column name: "deploymentDateTime_zone"
        }

        comments type: 'text'

        // Speed up candidateEntitiesService.
        cache: true
        station cache:true

        location type: GeometryUserType
    }

    static searchable = {
        receiver(component:true)
        station(component:true)
    }

    /**
     * Date the receiver is scheduled to be recovered.
     */
    Date recoveryDate

    /**
     * An identifier used during recovery to remotely release the tether
     * allowing the receiver to come to the surface.
     */
    String acousticReleaseID

    MooringType mooringType

    String mooringDescriptor

    /**
     * Depth to bottom (m)
     */
    Float bottomDepthM

    /**
     * Depth below surface (m)
     */
    Float depthBelowSurfaceM

    /**
     * TODO: probably should be enum - what are possible values?
     */
    ReceiverOrientation receiverOrientation

    Point location

    Integer batteryLifeDays

    String comments

    /**
     * Will initially be NULL, until the recovery occurs.  This relationship
     * is here for the "receiver recovery" list view, which is actually a list
     * of receiver deployments, including those with and without associated
     * recoveries.
     */
    static hasOne = [recovery:ReceiverRecovery]

    /**
     * Date when data from this deployment is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */

    static constraints = {
        receiver()
        station()
        initialisationDateTime(nullable: true, validator: conflictingDeploymentValidator)
        deploymentDateTime()
        recoveryDate(nullable: true, validator: recoveryDateValidator)
        acousticReleaseID(nullable: true)
        mooringType()
        mooringDescriptor(nullable: true, blank: true)
        bottomDepthM(nullable: true, min: 0F)
        depthBelowSurfaceM(nullable: true, min: 0F)
        receiverOrientation(nullable: true)
        location(nullable: true)
        comments(nullable: true)
        recovery(nullable: true)
        batteryLifeDays(nullable: true)
    }

    static def recoveryDateValidator = {
        recoveryDate, obj ->

        if (recoveryDate)
        {
            return recoveryDate.after(obj.deploymentDateTime.toDate())
        }

        return true
    }

    static def conflictingDeploymentValidator = { initialisationDateTime, deployment ->
        // All non-null deployment intervals for receiver, other than 'this' deployment's.
        def conflictingDeployments = deployment.receiver?.deployments.findAll {
            !it.same(deployment) && it.undeployableInterval
        }.findAll {
            def checkInterval = it.undeployableInterval
            checkInterval.contains([
                deployment.initialisationDateTime,
                deployment.deploymentDateTime
            ]) ||

            // deployment.deployedInterval?.overlaps(checkInterval)
            deployment.undeployableInterval?.overlaps(checkInterval)
        }

        if (conflictingDeployments.isEmpty()) {
            return true
        }

        [
            'receiverDeployment.initialisationDateTime.conflictingDeployment',
            deployment.receiver,
            conflictingDeployments
        ]
    }

    // Don't want to override 'equals()' as this causes unexpected behaviour with GORM.
    boolean same(Object other) {
        if (other == null) {
            return false
        }

        return (this.receiver.same(other.receiver) && this.undeployableInterval == other.undeployableInterval)
    }

    String toString() {
        return String.valueOf(receiver) + " - " + String.valueOf(undeployableInterval)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation() {
        return GeometryUtils.scrambleLocation(location ?: station?.location)
    }

    double getLatitude() {
        return getScrambledLocation()?.coordinate?.y
    }

    double getLongitude() {
        return getScrambledLocation()?.coordinate?.x
    }

    private DateTime now() {
        return new DateTime()
    }

    boolean isActive() {
        isActive(now())
    }

    boolean isActive(dateTime) {
        if (!recovery) {
            return !deploymentDateTime.isAfter(dateTime)
        }

        return (!deploymentDateTime.isAfter(dateTime)) && dateTime.isBefore(recovery?.recoveryDateTime)
    }

    def getUndeployableInterval() {
        def startDateTime = initialisationDateTime ? initialisationDateTime : deploymentDateTime

        if (startDateTime) {
            if (recovery?.status == DeviceStatus.RECOVERED) {
                return new Interval(startDateTime, recovery.recoveryDateTime)
            }

            if (recovery?.status) {
                return new OpenInterval(startDateTime)
            }
        }
    }

    def getDeployedInterval() {
        if (undeployableInterval instanceof Interval) {
            return undeployableInterval
        }
    }

    def getDeploymentNumber() {
        receiver?.deployments?.sort { it.deploymentDateTime }.findIndexOf {
            it.same(this)
        } + 1
    }
}
