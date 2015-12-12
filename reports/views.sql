DROP VIEW ActivityYearMonth;
CREATE VIEW ActivityYearMonth AS
SELECT
  activity.name as activityName,
  activity.region_name as regionName,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth
FROM
  activity
WHERE
  activity.classifier = 'ACTIVITY'
GROUP BY
  activity.name,
  activity.region_name,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);

DROP VIEW InactiveParticipant;
CREATE VIEW InactiveParticipant AS
SELECT 
  person.surname, 
  person.firstname AS firstName, 
  person.birthdate AS birthDate, 
  activity.name AS activityName, 
  activity.region_name AS regionName, 
  datediff(now(),attended.startdatetime) AS daysSinceLastAttended 
FROM 
  attended, 
  participant, 
  person, 
  activity 
WHERE 
  participant.participant_id = attended.participant_participant_id AND 
  activity.activity_id = attended.activity_activity_id AND 
  participant.person_person_id = person.person_id AND 
  participant.status = 'ACTIVE'
GROUP BY 
  participant.participant_id, 
  activity.activity_id 
ORDER BY 
 daysSinceLastAttended DESC;

DROP VIEW MailMergeData;
CREATE VIEW MailMergeData AS
SELECT 
  person.salutation_name AS salutation, 
  person.surname, 
  person.firstname AS firstName, 
  person.middlename AS middleName, 
  person.preferredname AS preferredName, 
  person.birthdate AS birthDate, 
  person.homephonenumber AS homePhoneNumber, 
  person.mobilephonenumber AS mobilePhoneNumber, 
  person.emailaddress AS emailAddress, 
  person.region_name AS regionOfPerson, 
  participant.status AS participantStatus, 
  volunteer.status AS volunteerStatus, 
  location.street1, 
  location.street2, 
  location.suburb, 
  location.postcode 
FROM 
  person 
LEFT OUTER JOIN 
  participant 
ON 
  participant.person_person_id = person.person_id  
LEFT OUTER JOIN 
  volunteer 
ON 
  volunteer.person_person_id = person.person_id 
LEFT OUTER JOIN 
  location 
ON 
  location.location_id = person.mailaddress_location_id;

DROP VIEW ParticipantActivityByMonth;
CREATE VIEW ParticipantActivityByMonth AS
SELECT
  person.surname,
  person.firstname AS firstName,
  person.birthdate AS birthDate,
  person.region_name AS regionName,
  activity.name AS activityName,
  participant.status AS participantStatus,
  EXTRACT(YEAR_MONTH FROM attended.startdatetime) as yearMonth,
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,attended.startdatetime,attended.enddatetime))/60,1) as hoursAttended
FROM
  activity,
  attended,						
  participant,
  person
WHERE
  attended.activity_activity_id = activity.activity_id AND
  participant.participant_id = attended.participant_participant_id AND
  person.person_id = participant.person_person_id AND							
  participant.status <> 'EXITED'
GROUP BY
  participant.participant_id,
  activity.activity_id,
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);

DROP VIEW ParticipantPerson;
CREATE VIEW ParticipantPerson AS
SELECT
  activity.name AS activityName,
  activity.startDateTime,
  activity.region_name AS regionName,
  person.surname,
  person.firstname AS firstName,
  person.preferredname AS preferredName,
  concat(location.street1,' ' ,location.street2, ' ', location.suburb) as address,
  person.homephonenumber AS homePhoneNumber,
  person.mobilephonenumber AS mobilePhoneNumber,
  socialfactors.limitinghealthissues AS limitingHealthIssues,
  socialfactors.otherlimitingfactors AS otherLimitingFactors
FROM
  activity
JOIN
  participation
ON
  participation.activity_activity_id = activity.activity_id
JOIN
  participant
ON
  participant.participant_id = participation.participant_participant_id
JOIN
  person
ON
  person.person_id = participant.person_person_id
LEFT JOIN
  socialfactors
ON
  socialfactors.participant_participant_id =  participant.participant_id
LEFT JOIN
  location
ON
  location.location_id =  person.streetaddress_location_id
ORDER BY
  person.surname, person.firstname;

DROP VIEW VolunteeredTimeByVolunteerAndRoleAndYearMonth;
CREATE VIEW VolunteeredTimeByVolunteerAndRoleAndYearMonth AS
SELECT
  person.surname,
  person.firstname AS firstName,  
  person.birthdate AS birthDate,  
  person.region_name AS regionName,  
  volunteer.status AS volunteerStatus, 
  CASE volunteered_time.role  
    WHEN 'VTACTIVITY' THEN 'ACTIVITIES' 
    ELSE volunteered_time.role 
  END AS volunteerRole, 
  EXTRACT(YEAR_MONTH FROM volunteered_time.startdatetime) AS yearMonth,  
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) AS hoursVolunteered  
FROM  
  volunteered_time,  
  volunteer,  
  person  
WHERE  
  volunteer.volunteer_id = volunteered_time.volunteer_volunteer_id AND  
  volunteer.person_person_id = person.person_id AND   
  volunteer.status <> 'EXITED'  
GROUP BY  
  volunteer.volunteer_id,  
  volunteered_time.role,  
  EXTRACT(YEAR_MONTH FROM volunteered_time.startdatetime);

DROP VIEW VolunteeredTimeForActivitesByVolunteerAndRoleAndYearMonth;
CREATE VIEW VolunteeredTimeForActivitesByVolunteerAndRoleAndYearMonth AS 
SELECT  
  activity.name as activityName, 						
  activity.region_name as activityRegion,  	
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as activityYearMonth,  
  person.surname,  
  person.firstname AS firstName,  
  person.birthdate AS birthDate,  
  volunteer.status AS volunteerStatus, 
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) as hoursVolunteered  
FROM  
  activity, 	
  volunteered_time,  
  volunteer,  
  person  
WHERE  
  volunteered_time.activity_activity_id = activity.activity_id AND 						
  volunteer.volunteer_id  = volunteered_time.volunteer_volunteer_id AND  
  person.person_id = volunteer.person_person_id 
GROUP BY  
  activity.name,  
  activity.region_name, 
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);

DROP VIEW VolunteeredTimeForActivityByYearMonth;
CREATE VIEW VolunteeredTimeForActivityByYearMonth AS 
SELECT 
  activity.name AS activityName, 
  activity.region_name AS regionName, 
  EXTRACT(YEAR_MONTH FROM activity.startdatetime) AS yearMonth, 
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) AS hoursVolunteered 
FROM  
  activity, 
  volunteered_time 
WHERE 
  volunteered_time.activity_activity_id = activity.activity_id  
GROUP BY 
  activity.name, 
  activity.region_name, 
  EXTRACT(YEAR_MONTH FROM activity.startdatetime);

DROP VIEW VolunteeredTimeForActivityByYearMonthWithZeroValues;
CREATE VIEW VolunteeredTimeForActivityByYearMonthWithZeroValues AS 
SELECT  aym.*,
  CASE 
  WHEN isnull(vtaym.hoursVolunteered) THEN 0
  ELSE vtaym.hoursVolunteered 
  END AS hoursVolunteered 
FROM 
  activityyearmonth AS aym
LEFT OUTER JOIN 
  volunteeredtimeforactivitybyyearmonth AS vtaym
ON 
  aym.activityName = vtaym.activityName 
  AND 
  aym.regionName = vtaym.regionName 
  AND 
  aym.yearMonth = vtaym.yearMonth;

DROP VIEW VolunteeredTimeForCallsByVolunteerAndYearMonth;
CREATE VIEW VolunteeredTimeForCallsByVolunteerAndYearMonth AS 
SELECT  
  person.surname,
  person.firstname AS firstName,
  person.birthdate AS birthDate,
  person.region_name AS region,
  volunteer.status AS volunteerStatus, 
  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate) AS callScheduleYearMonth,  
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) AS totalVolunteeredHours,  
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,scheduledcall.startdatetime,scheduledcall.enddatetime))/60,1) AS totalCallHours  
FROM  
  calendardaycallschedule, 						
  volunteer, 
  person, 	
  volunteered_time, 						
  scheduledcall 
WHERE  
  volunteer.volunteer_id = calendardaycallschedule.allocatedvolunteer_volunteer_id AND 
  volunteered_time.volunteer_volunteer_id = volunteer.volunteer_id AND 
  person.person_id = volunteer.person_person_id AND 
  volunteered_time.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id AND 						
  calendardaycallschedule.allocatedvolunteer_volunteer_id = volunteer.volunteer_id 
GROUP BY  
  volunteer.volunteer_id,  
  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate);

DROP VIEW VolunteeredTimeForCallsByYearMonth;
CREATE VIEW VolunteeredTimeForCallsByYearMonth AS 
SELECT  
  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate) AS callScheduleYearMonth,  
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) AS hoursVolunteered,  
  ROUND(SUM(TIMESTAMPDIFF(MINUTE,scheduledcall.startdatetime,scheduledcall.enddatetime))/60,1) AS hoursOnCalls  
FROM  
  calendardaycallschedule, 						
  volunteered_time, 						
  scheduledcall 
WHERE  
  volunteered_time.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id AND 						
  scheduledcall.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id 
GROUP BY  
  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate);